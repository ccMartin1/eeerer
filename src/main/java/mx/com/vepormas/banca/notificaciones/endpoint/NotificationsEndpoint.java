package mx.com.vepormas.banca.notificaciones.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.vepormas.banca.lib.dao.LogDao;
import mx.com.vepormas.banca.lib.dao.NotificationDao;
import mx.com.vepormas.banca.lib.endpoint.AbstractEndpoint;
import mx.com.vepormas.banca.lib.exception.MissingParameterException;
import mx.com.vepormas.banca.lib.exception.business.BusinessException;
import mx.com.vepormas.banca.notificaciones.bean.AttachmentsInput;
import mx.com.vepormas.banca.notificaciones.bean.GetNotificationCatalogResponse;
import mx.com.vepormas.banca.notificaciones.bean.GetUserInformationForNotificationResponse;
import mx.com.vepormas.banca.notificaciones.bean.Notification;
import mx.com.vepormas.banca.notificaciones.bean.ParametersInput;
import mx.com.vepormas.banca.notificaciones.bean.SendNotificationAndAttachmentsRequest;
import mx.com.vepormas.banca.notificaciones.bean.SendSMSRequest;
import mx.com.vepormas.banca.notificaciones.bean.UserInformationOutput;
import mx.com.vepormas.banca.notificaciones.business.EmailSenderBO;
import mx.com.vepormas.banca.notificaciones.business.NotificacionesBO;
import mx.com.vepormas.banca.notificaciones.dto.AttachmentDTO;
import mx.com.vepormas.banca.notificaciones.dto.CommentInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.NotificationInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoOutputDTO;
import mx.com.vepormas.lib.resources.ApplicationProperties;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class NotificationsEndpoint extends AbstractEndpoint {

	static Logger logger = LogManager.getLogger(NotificationsEndpoint.class);

	public static final String BASE_NAMESPACE_URI = "http://vepormas.com.mx/banca/schemas/base";
	public static final String SCHEMA_NAMESPACE_URI = "http://vepormas.com.mx/banca/schemas/generanotificaciones";
	
	private Namespace namespaceGeneraNotificaciones;
	//private Namespace namespaceBase;
	
	private NotificacionesBO notificacionesBO;
	private EmailSenderBO emailSenderBO;

	@Autowired(required = true)
	public NotificationsEndpoint(
			@Qualifier("appProps") ApplicationProperties appProps,
			@Qualifier("notificacionesBO") NotificacionesBO notificacionesBO,
			@Qualifier("emailSenderBO") EmailSenderBO emailSenderBO,
			@Qualifier("notifJaxb2Marshaller") Marshaller marshaller,
			@Qualifier("notifJaxb2Marshaller") Unmarshaller unmarshaller,
			@Qualifier("notificationDao") NotificationDao notificationDao, 
			@Qualifier("logDao") LogDao logDao) {
		super(appProps,marshaller,unmarshaller,notificationDao,logDao);

		this.notificacionesBO = notificacionesBO;
		this.emailSenderBO = emailSenderBO;
		namespaceGeneraNotificaciones = Namespace.getNamespace("schema",SCHEMA_NAMESPACE_URI);
		//namespaceBase = Namespace.getNamespace("case", BASE_NAMESPACE_URI);
	}

	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "GetNotificationCatalogRequest")
	@ResponsePayload
	public GetNotificationCatalogResponse getNotificationsCatalog(
			@RequestPayload Element request) throws Exception {

		String message = null;
		boolean status = false;
		int code = appProps.getInt("constant.endpoint.code.fail");
		String coreUser = null;
		String exception = null;
		GetNotificationCatalogResponse response = new GetNotificationCatalogResponse();
		List<Notification> output = null;
		
		try {
			Element subject = request.getChild("subject", baseNamespace);
			Element principal = subject.getChild("principal", baseNamespace);
			coreUser = principal.getValue();
			Element clientInformation = request.getChild("data", namespaceGeneraNotificaciones);
			Element notificationType = clientInformation.getChild("notificationType", namespaceGeneraNotificaciones);
			
			output = notificacionesBO.getNotificationsCatalogBO(notificationType.getValue());

			if (ObjectUtils.notEqual(output, null)) {

				status = true;
				code = appProps.getInt("constant.endpoint.code.success");
				message = appProps
						.getString("message.consulta.notification.catalog.success");

			} else {
				status = false;
				code = appProps.getInt("constant.endpoint.code.success");
				message = appProps
						.getString("message.consulta.notification.catalog.success.nodata");
			}

		} catch (MissingParameterException mpe) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.catalog.missing"));
			sb.appendSeparator(" ");
			sb.append("data");
			sb.appendSeparator(" ");
			sb.append("[").append(mpe.getMessage()).append("]");

			logger.error(sb.toString(), mpe);
			message = sb.toString();
			exception= sb.toString();
		} catch (BusinessException be) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.catalog.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(be.getMessage());

			logger.error(sb.toCharArray(), be);
			message = sb.toString();
			exception= sb.toString();
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.exception"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());

			logger.error(sb.toString(), e);
			message = sb.toString();
			exception= sb.toString();
		}

		response.setStatus(status);
		response.setCode(code);
		response.setMessage(message);
		response.setData(output);
//		GetNotificationCatalogRequest requestObj = (GetNotificationCatalogRequest) unmarshalXMLElement(request);
//		super.bitacorizaOperacion(requestObj, response, "1", "getNotificationsCatalog", exception, coreUser, status?1:0, this.getClass());
		return response;
	
	}

	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "GetUserInformationForNotificationRequest")
	@ResponsePayload
	public GetUserInformationForNotificationResponse getUserInformationForNotification(
			@RequestPayload Element request) throws Exception {

		String message = null;
		String exception = null;
		String coreUser = null;
		boolean status = false;
		int code = appProps.getInt("constant.endpoint.code.fail");

		UserInfoInputDTO infoIn = null;
		UserInfoOutputDTO infoOut = null;
		UserInformationOutput output = null;
		GetUserInformationForNotificationResponse response = new GetUserInformationForNotificationResponse();

		try {
			Element clientInformation = request.getChild("data",namespaceGeneraNotificaciones);
			Element id = clientInformation.getChild("userIdCore", namespaceGeneraNotificaciones);
			Element customerId = clientInformation.getChild("clientIdCore",namespaceGeneraNotificaciones);
			
			infoIn = new UserInfoInputDTO();
			infoIn.setId(id.getValue());
			infoIn.setClienteID(customerId.getValue());
			
			coreUser = id.getValue();

			infoOut = new UserInfoOutputDTO();
			infoOut = notificacionesBO.getUserInformationForNotificationBO(infoIn);

			if (ObjectUtils.notEqual(infoOut, null)) {

				output = new UserInformationOutput();
				output.setEmail(infoOut.getEmail());
				output.setTelephone(infoOut.getTelephone());

				status = true;
				code = appProps.getInt("constant.endpoint.code.success");
				message = appProps
						.getString("message.consulta.user.information.success");

			} else {
				status = false;
				code = appProps.getInt("constant.endpoint.code.success");
				message = appProps
						.getString("message.consulta.user.information.success.nodata");
			}

		} catch (MissingParameterException mpe) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.user.information.missing"));
			sb.appendSeparator(" ");
			sb.append("data");
			sb.appendSeparator(" ");
			sb.append("[").append(mpe.getMessage()).append("]");

			logger.error(sb.toString(), mpe);
			message = sb.toString();
			exception = sb.toString();

		} catch (BusinessException be) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(be.getMessage());

			logger.error(sb.toCharArray(), be);
			message = sb.toString();
			exception = sb.toString();
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.exception"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());

			logger.error(sb.toString(), e);
			message = sb.toString();
			exception = sb.toString();
		}

		response.setStatus(status);
		response.setCode(code);
		response.setMessage(message);

		response.setData(output);
//		GetUserInformationForNotificationRequest requestObj = (GetUserInformationForNotificationRequest) unmarshalXMLElement(request);
//		super.bitacorizaOperacion(requestObj, response, "1", "getUserInformationForNotification", exception, coreUser, status?1:0, this.getClass());
		return response;
	}

	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "SendNotificationRequest")
	@ResponsePayload
	public void sendNotification(@RequestPayload Element request) throws Exception {
		
		NotificationInputDTO commentInput = null;
		String exception = null;
		int status = 0;
		String coreUser = null;
		try {
			Element subject = request.getChild("subject",baseNamespace);
			Element principal = subject.getChild("principal",baseNamespace);
			
			Element clientInformation = request.getChild("data",namespaceGeneraNotificaciones);
			Element email = clientInformation.getChild("email", namespaceGeneraNotificaciones);
			Element notificationId = clientInformation.getChild("notificationId", namespaceGeneraNotificaciones);
			List<Element> mapaDatos = clientInformation.getChildren("parameters", namespaceGeneraNotificaciones);
			
			commentInput = new NotificationInputDTO();
			
			coreUser = principal.getValue();
			
			commentInput.setPrincipal(principal.getValue());
			commentInput.setNotificationReference(notificationId.getValue());
			commentInput.setEmailTo(email.getValue());
			
			
			Map<String, Object> parameter = new HashMap<String, Object>();
			for(Element input: mapaDatos){
				
				parameter.put(input.getChild("key", namespaceGeneraNotificaciones).getValue(), 
						input.getChild("value", namespaceGeneraNotificaciones).getValue());				
			}

			commentInput.setParameters(parameter);
			notificacionesBO.sendNotificationBO(commentInput);
			status = 1;
			
		} catch (BusinessException be) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(be.getMessage());

			logger.error(sb.toCharArray(), be);
			exception = sb.toString();
			status = 0;
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.exception"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());

			logger.error(sb.toString(), e);
			exception = sb.toString();
			status = 0;
		}
//		SendNotificationRequest requestObj = (SendNotificationRequest) unmarshalXMLElement(request);
//		super.bitacorizaOperacion(requestObj, null, "1", "sendNotification", exception, coreUser, status, this.getClass());
	}
	
	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "SendNotificationAndAttachmentsRequest")
	@ResponsePayload
	public void sendNotificationAndAttachments(@RequestPayload Element request)
			throws Exception {
		NotificationInputDTO commentInput = null;
		SendNotificationAndAttachmentsRequest requestObj = null;
		String exception = null;
		int status = 0;
		String coreUser = null;
		try {
			requestObj =  (SendNotificationAndAttachmentsRequest) unmarshalXMLElement(request);
			commentInput = new NotificationInputDTO();			
			
			commentInput.setPrincipal(requestObj.getSubject().getPrincipal());
			commentInput.setNotificationReference(requestObj.getData().getNotificationId());
			commentInput.setEmailTo(requestObj.getData().getEmail());
			Map<String, Object> parameter = new HashMap<String, Object>();
			for(ParametersInput input: requestObj.getData().getParameters()){				
				parameter.put(input.getKey(), input.getValue());				
			}
			commentInput.setParameters(parameter);	
			List<AttachmentDTO> adjuntos = new ArrayList<AttachmentDTO>();
			for(AttachmentsInput attachment:requestObj.getAttachments()){
				AttachmentDTO adjunto = new AttachmentDTO();
				adjunto.setAttachment(attachment.getAttachment());
				adjunto.setDescription(attachment.getDescription());
				adjunto.setExtension(attachment.getExtension());
				adjunto.setFileName(attachment.getFileName());
				adjuntos.add(adjunto);
			}
			commentInput.setAdjuntos(adjuntos);
			notificacionesBO.sendNotificationBO(commentInput);
			coreUser = requestObj.getSession().getUserId();
			status = 1;
		} catch (BusinessException be) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(be.getMessage());

			logger.error(sb.toCharArray(), be);
			exception = sb.toString();
			status = 0;
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.exception"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());

			logger.error(sb.toString(), e);
			exception = sb.toString();
			status = 0;
		}
//		super.bitacorizaOperacion(requestObj, null, "1", "sendNotificationAndAttachments", exception, coreUser, status, this.getClass());
	}
	
	
	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "SendCommentRequest")
	@ResponsePayload
	public void sendComment(@RequestPayload Element request) throws Exception {

		CommentInputDTO commentInputDTO = null;
		String exception = null;
		int status = 0;
		
		try {

			//Element subject = request.getChild("subject", baseNamespace);
			
			//Element principal = subject.getChild("principal", baseNamespace);
			
			Element session = request.getChild("session", baseNamespace);
            
            Element userPrincipal = session.getChild("userPrincipal", baseNamespace);

			Element clientInformation = request.getChild("data",namespaceGeneraNotificaciones);
			Element name = clientInformation.getChild("name", namespaceGeneraNotificaciones);
			Element telephone = clientInformation.getChild("telephone", namespaceGeneraNotificaciones);
			Element email = clientInformation.getChild("email", namespaceGeneraNotificaciones);
			Element service = clientInformation.getChild("service", namespaceGeneraNotificaciones);
			Element comment = clientInformation.getChild("comment", namespaceGeneraNotificaciones);

			commentInputDTO = new CommentInputDTO();
			
			commentInputDTO.setPrincipal(userPrincipal.getValue());
			commentInputDTO.setName(name.getValue());
			commentInputDTO.setTelephone(telephone.getValue());
			commentInputDTO.setEmail(email.getValue());
			commentInputDTO.setService(service.getValue());
			commentInputDTO.setComment(comment.getValue());
			
			notificacionesBO.sendCommentBO(commentInputDTO);
			status = 1;
		} catch (BusinessException be) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(be.getMessage());

			logger.error(sb.toCharArray(), be);
			exception = sb.toString();
			status = 0;
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps
					.getString("error.consulta.notification.exception"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());

			logger.error(sb.toString(), e);
			exception = sb.toString();
			status = 0;
		}
//		SendCommentRequest requestObj = (SendCommentRequest) unmarshalXMLElement(request);
//		super.bitacorizaOperacion(requestObj, null, "1", "sendComment", exception, coreUser, status, this.getClass());
	}
	
	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "ExecuteSendPendingMailRequest")
	@ResponsePayload
	public void executeSendPendingMail(@RequestPayload Element request) throws Exception {
		logger.info("Se ha invocado el envio sobre demanda de los correos en estado pendiente");
		Element subject = request.getChild("subject", baseNamespace);
		Element principal = subject.getChild("principal", baseNamespace);
		if(principal!=null){
			emailSenderBO.readAndSendCorreos();
		}
//		ExecuteSendPendingMailRequest requestObj = (ExecuteSendPendingMailRequest) unmarshalXMLElement(request);
//		super.bitacorizaOperacion(requestObj, null, "1", "executeSendPendingMail", null, principal.getValue(), 1, this.getClass());
	}
	
	
	@PayloadRoot(namespace = SCHEMA_NAMESPACE_URI, localPart = "SendSMSRequest")
	@ResponsePayload
	public void sendSMSRequest(@RequestPayload Element request) throws Exception{
		
		try {
			
			SendSMSRequest requestObj =  (SendSMSRequest) unmarshalXMLElement(request);
			
			if( requestObj == null ){
				throw new MissingParameterException("No se encontro el objeto SendSMSRequest");
			}
			
			if(requestObj.getData() == null){
				throw new MissingParameterException("No se encontro el objeto SendSMSRequest.data");
			}
			
			if(StringUtils.isBlank(requestObj.getData().getPhoneNumber())){
				throw new MissingParameterException("No se encontro el objeto SendSMSRequest.data.phoneNumber");
			}
			
			if(StringUtils.isBlank(requestObj.getData().getNotificationId())){
				throw new MissingParameterException("No se encontro el objeto SendSMSRequest.data.notificationId");
			}

			if(requestObj.getData().getParameters() == null && requestObj.getData().getParameters().size() > 0 ){
				throw new MissingParameterException("No se encontro el objeto SendSMSRequest.data.Parameters");
			}
			
			notificacionesBO.sendSMSNOtification( requestObj );
			
		}catch (MissingParameterException mi){
			StrBuilder sb = new StrBuilder();
			sb.append(appProps.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(mi.getMessage());

			logger.info(sb.toCharArray(), mi);
		} catch (Exception e) {
			StrBuilder sb = new StrBuilder();
			sb.append(appProps.getString("error.consulta.user.information.business"));
			sb.appendSeparator(" ");
			sb.append("cause");
			sb.appendSeparator(" ");
			sb.append(e.getMessage());
			
			logger.info(sb.toCharArray(), e);
		}
		
	}

}
