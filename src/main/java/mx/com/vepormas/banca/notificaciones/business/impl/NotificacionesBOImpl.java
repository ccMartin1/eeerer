package mx.com.vepormas.banca.notificaciones.business.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import mx.com.vepormas.banca.lib.bean.entity.Auditoria;
import mx.com.vepormas.banca.lib.bean.entity.Correo;
import mx.com.vepormas.banca.lib.bean.entity.CorreoAdjunto;
import mx.com.vepormas.banca.lib.bean.entity.CorreoUsuario;
import mx.com.vepormas.banca.lib.bean.entity.EstadoCorreo;
import mx.com.vepormas.banca.lib.bean.entity.Parametro;
import mx.com.vepormas.banca.lib.bean.entity.Plantilla;
import mx.com.vepormas.banca.lib.business.AbstractBusinessObject;
import mx.com.vepormas.banca.lib.exception.MissingParameterException;
import mx.com.vepormas.banca.lib.exception.business.BusinessException;
import mx.com.vepormas.banca.lib.utils.VelocityTemplateUtils;
import mx.com.vepormas.banca.notificaciones.bean.Notification;
import mx.com.vepormas.banca.notificaciones.bean.Parameters;
import mx.com.vepormas.banca.notificaciones.bean.SendSMSRequest;
import mx.com.vepormas.banca.notificaciones.bean.wscli.Data;
import mx.com.vepormas.banca.notificaciones.bean.wscli.Receiver;
import mx.com.vepormas.banca.notificaciones.bean.wscli.SendSMSNotificationByReceiverRequest;
import mx.com.vepormas.banca.notificaciones.bean.wscli.Template;
import mx.com.vepormas.banca.notificaciones.bean.wscli.base.Session;
import mx.com.vepormas.banca.notificaciones.business.NotificacionesBO;
import mx.com.vepormas.banca.notificaciones.dao.NotificacionesDAO;
import mx.com.vepormas.banca.notificaciones.dao.SMSDAO;
import mx.com.vepormas.banca.notificaciones.dto.AttachmentDTO;
import mx.com.vepormas.banca.notificaciones.dto.CommentInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.NotificationInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoOutputDTO;
import mx.com.vepormas.lib.resources.ApplicationProperties;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class NotificacionesBOImpl extends AbstractBusinessObject implements NotificacionesBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionesBOImpl.class);
	private NotificacionesDAO notificacionesDAO;
	private JavaMailSender mailSender;
	
	private SMSDAO smsDao;
	private ObjectMapper mapperObj;

	public NotificacionesBOImpl(ApplicationProperties appProps,NotificacionesDAO notificacionesDAO, JavaMailSender mailSender, SMSDAO smsDao) {

		super(appProps);
		this.notificacionesDAO = notificacionesDAO;
		this.mailSender = mailSender;
		this.smsDao = smsDao;
		mapperObj= new ObjectMapper();
	}

	public UserInfoOutputDTO getUserInformationForNotificationBO(UserInfoInputDTO infoOut) 
																throws BusinessException, MissingParameterException {

		UserInfoOutputDTO userInfo = null;
		infoOut.setUserNameT24(appProps.getString("constant.t24.usuario"));
		infoOut.setPasswordT24(appProps.getString("constant.t24.password"));
		try {
			if (StringUtils.isBlank(infoOut.getId())) {
				throw new MissingParameterException(
						appProps.getString("error.missing.parameter.usuario"));
			}

			if (StringUtils.isBlank(infoOut.getClienteID())) {
				throw new MissingParameterException(
						appProps.getString("error.missing.parameter.cliente"));
			}

			userInfo = notificacionesDAO.getCorreoFomrDB(infoOut);

		}  catch (Exception e) {

			throw new BusinessException(e);
		}

		return userInfo;
	}

	public List<Notification> getNotificationsCatalogBO(String notificationType)
			throws BusinessException, MissingParameterException {
		List<Notification> notlist = null;
		try {
			if (StringUtils.isBlank(notificationType)) {
				throw new MissingParameterException(appProps.getString("error.missing.parameter.notificationType"));
			}
			List<Plantilla> response = notificacionesDAO.getNotificationsCatalogDAO(new Integer(notificationType));
			if (ObjectUtils.notEqual(response, null)) {
				notlist = new ArrayList<Notification>();
				for (Plantilla plantilla : response) {
					Notification not = new Notification();
					not.setNotificationId(plantilla.getReferencia());
					not.setDescription(plantilla.getDescripcion());
					List<Parameters> parameters = new ArrayList<Parameters>();
					for (Parametro para : plantilla.getParametroList()) {
						Parameters param = new Parameters();
						param.setVariableName(para.getNombreVariable());
						param.setDescripcion(para.getDescripcion());
						parameters.add(param);
					}
					not.setParameters(parameters);
					notlist.add(not);
				}

			} else {

				throw new BusinessException(
						appProps.getString("error.no.response.database"));
			}
		} catch (Exception e) {

			throw new BusinessException(e);
		}

		return notlist;
	}

	public void sendNotificationBO(NotificationInputDTO notificationInput)
			throws BusinessException, MissingParameterException {

		if (StringUtils.isBlank(notificationInput.getPrincipal())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.clienteID"));
		}
		if (StringUtils.isBlank(notificationInput.getEmailTo())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.email"));
		}
		if (StringUtils.isBlank(notificationInput.getNotificationReference())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.notificationID"));
		}
		if (notificationInput.getParameters().isEmpty()) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.parameters"));
		}
		
		Long userID = notificacionesDAO.getUserIDFromDB( notificationInput.getPrincipal() );
		
		if(userID != null){
			
			notificationInput.setClienteID( String.valueOf( userID ) );
		}
		
			
		notificationInput.setEmailFrom(appProps.getString("mail.electronic.banking.support"));
		//notificationInput.setEmailFrom("Atención a Clientes BX+<atencion.clientes@vepormas.mx>");
		if(!notificationInput.getParameters().containsKey(appProps.getString("constant.bo.key.static.resources.server"))){
			notificationInput.getParameters().put(appProps.getString("constant.bo.key.static.resources.server"),appProps.getString("constant.bo.value.static.resources.server"));
		}
		
		
		saveNotificationGral(notificationInput);

	}

	public void sendCommentBO(CommentInputDTO commentInputDTO)
			throws BusinessException, MissingParameterException {

		NotificationInputDTO notificationInput = new NotificationInputDTO();

		if (StringUtils.isBlank(commentInputDTO.getPrincipal())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.clienteID"));
		}
		if (StringUtils.isBlank(commentInputDTO.getName())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.name"));
		}
		if (StringUtils.isBlank(commentInputDTO.getTelephone())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.telephone"));
		}
		if (StringUtils.isBlank(commentInputDTO.getEmail())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.email"));
		}
		if (StringUtils.isBlank(commentInputDTO.getService())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.service"));
		}
		if (StringUtils.isBlank(commentInputDTO.getComment())) {
			throw new MissingParameterException(
					appProps.getString("error.missing.parameter.comment"));
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(appProps.getString("constant.bo.key.Name"),commentInputDTO.getName());
		parameter.put(appProps.getString("constant.bo.key.Telephone"),commentInputDTO.getTelephone());
		parameter.put(appProps.getString("constant.bo.key.Email"),commentInputDTO.getEmail());
		parameter.put(appProps.getString("constant.bo.key.Service"),commentInputDTO.getService());
		parameter.put(appProps.getString("constant.bo.key.Comment"),commentInputDTO.getComment());
		parameter.put(appProps.getString("constant.bo.key.subject"),appProps.getString("mail.subject"));
		parameter.put(appProps.getString("constant.bo.key.static.resources.server"),appProps.getString("constant.bo.value.static.resources.server"));
		notificationInput.setParameters(parameter);
		
		
		Long userID = notificacionesDAO.getUserIDFromDB( commentInputDTO.getPrincipal() );
		
		if(userID != null){
			
			notificationInput.setClienteID( String.valueOf( userID ));
		}
		

		notificationInput.setEmailFrom(commentInputDTO.getEmail());
		notificationInput.setEmailTo(appProps.getString("mail.electronic.banking.support.contact"));
		notificationInput.setNotificationReference(appProps.getString("constant.bo.notificacion.plantilla.mensaje"));

		saveNotificationGral(notificationInput);
	}

	@Async
	public void sendAsyncNotifications(Correo correo) {
		LOGGER.debug("#################################");
		LOGGER.debug("## Iniciando sendNotifications ##");
		LOGGER.debug("#################################");
		if (correo == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.objeto.null"));
			return;
		}
		if (correo.getIdCorreo() == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.entity.null"));
		}
		if (correo.getAuditoria() == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.entity.null"));
		}

		if (correo.getEmailFrom() == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.from.null"));
		}
		if (correo.getEmailTo() == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.to.null"));
		}
		if (correo.getBody() == null) {
			LOGGER.error(appProps.getString("notificacion.mensaje.body.null"));
		}
		if (correo.getSubject() == null) {
			LOGGER.error(appProps
					.getString("notificacion.mensaje.subjecct.null"));
		}		
		correo.setEstadoCorreo(new EstadoCorreo(appProps
				.getInt("constant.dao.estado.correo.enviado")));
		try {
			MimeMessage mimeMessage = this.prepareMimeMessage(correo);
			mailSender.send((MimeMessagePreparator) mimeMessage);
		} catch (Exception e) {
			int maximosIntentos = appProps.getInt("constant.correo.maximo.intento");
			final String exception = e.toString();
			correo.setExcepcion(exception!=null && exception.length()>=4000 ? exception.substring(0, 3998):exception);
			if (correo.getIntentos() < maximosIntentos ) {
				correo.setEstadoCorreo(new EstadoCorreo(appProps
					.getInt("constant.dao.estado.correo.pendiente")));
			}else{
				correo.setEstadoCorreo(new EstadoCorreo(appProps
						.getInt("constant.dao.estado.correo.error")));
			}
			LOGGER.error("Ha sucedido una excepcion no controlada", e);
		}
		try {
			notificacionesDAO.update(correo);
		} catch (Exception e) {
			LOGGER.error("No se ha logrado actualizar el estado del correo", e);
		}		
		LOGGER.debug("##################################");
		LOGGER.debug("## Terminando sendNotifications ##");
		LOGGER.debug("##################################");
	}

	private MimeMessage prepareMimeMessage(Correo correo)
            throws MessagingException{
		LOGGER.debug("Iniciando prepareMimeMessage {}", correo);
           MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		if (correo.getCorreoAdjuntoList() != null
				&& !correo.getCorreoAdjuntoList().isEmpty()) {
			for (CorreoAdjunto ca : correo.getCorreoAdjuntoList()) {
				byte[] stream = ca.getAdjunto();
				if (stream.length > 0) {
					helper.addAttachment(ca.getNombreArchivo()+"."+ca.getExtencion(), new ByteArrayResource(stream));
				}
			}
		}

		helper.setSubject(correo.getSubject());
		helper.setFrom(correo.getEmailFrom());
		helper.setTo(correo.getEmailTo());

		if (StringUtils.isNotEmpty(correo.getEmailCc())) {
			helper.setCc(correo.getEmailCc());
		}
		if (StringUtils.isNotEmpty(correo.getEmailCco())) {
			helper.setBcc(correo.getEmailCco());
		}

		helper.setText(correo.getBody(), true);
		LOGGER.debug("Terminando prepareMimeMessage {}", message);
		return message;
	}

	public List<Correo> getCorreosPendientes() {
 
		int estadoCorreo = appProps.getInt("constant.dao.estado.correo.pendiente");
		List<Correo> correosPendientes = notificacionesDAO.getCorreoListByEstadoCorreo(Integer.valueOf(estadoCorreo));
		
		return correosPendientes;
				
	}

	private void saveNotificationGral(NotificationInputDTO notificationInput)
			throws BusinessException {

		try {

			Plantilla plantilla = notificacionesDAO.getPlantillaByReference(notificationInput.getNotificationReference());
			InputStream templateStream = new ByteArrayInputStream(plantilla.getPlantilla());
			String htmBody = VelocityTemplateUtils.mergeVelocityTemplateWithParams(templateStream, notificationInput.getParameters());
			Correo correo = new Correo();
			CorreoUsuario CorreoUsuario = new CorreoUsuario();

			Auditoria auditoria = new Auditoria();
            auditoria.setFechaHoraCreacion(new Date());
			auditoria.setUsuarioCreacion(Long.valueOf(notificationInput.getClienteID()));

			if (notificationInput.getParameters().containsKey(appProps.getString("constant.bo.key.subject"))) {
				correo.setSubject(notificationInput.getParameters().get(appProps.getString("constant.bo.key.subject")).toString());
			} else {
				correo.setSubject(appProps.getString("mail.subject2"));
			}
			correo.setBody(htmBody);
			correo.setEmailTo(notificationInput.getEmailTo());
			correo.setEmailFrom(notificationInput.getEmailFrom());
			correo.setIntentos(appProps.getInt("constant.bo.correo.intentos.inicio"));
			correo.setEstadoCorreo(new EstadoCorreo(appProps.getInt("constant.dao.estado.correo.pendiente")));
			correo.setAuditoria(auditoria);

            long identificador = (long) notificacionesDAO.save(correo);

			CorreoUsuario.setIdCorreo(Long.valueOf(identificador));
			CorreoUsuario.setIdUsuario(Long.valueOf(notificationInput.getClienteID()));

			notificacionesDAO.save(CorreoUsuario);
			
			if(notificationInput.getAdjuntos()!=null && 
					!notificationInput.getAdjuntos().isEmpty()){
				for(AttachmentDTO adjunto : notificationInput.getAdjuntos()){
					CorreoAdjunto correoAdjunto = new CorreoAdjunto();
					correoAdjunto.setAdjunto(adjunto.getAttachment());
					correoAdjunto.setAuditoria(auditoria);
					correoAdjunto.setCorreo(correo);
					correoAdjunto.setDescripcion(adjunto.getDescription());
					correoAdjunto.setExtencion(adjunto.getExtension());
					correoAdjunto.setNombreArchivo(adjunto.getFileName());
					notificacionesDAO.save(correoAdjunto);
				}
			}

		} catch (ParseErrorException e) {
			throw new BusinessException(e);
		} catch (MethodInvocationException e) {
			throw new BusinessException(e);
		} catch (ResourceNotFoundException e) {
			throw new BusinessException(e);
		} catch (VelocityException e) {
			throw new BusinessException(e);
		}

	}
	
	@Transactional(propagation=Propagation.NEVER)
	public void markAsWorkingMail(Correo correo){		
		correo.setIntentos(correo.getIntentos() + 1);
		correo.getAuditoria().setFechaHoraModificacion(new Date());
		correo.setEstadoCorreo(new EstadoCorreo(appProps.getInt("constant.dao.estado.correo.trabajando")));
		notificacionesDAO.update(correo);
	}

	
	
	public void sendSMSNOtification(SendSMSRequest input) throws Exception{
		SendSMSNotificationByReceiverRequest request = new SendSMSNotificationByReceiverRequest();
		
		/*Generamos la sesión del SMS*/
		mx.com.vepormas.banca.notificaciones.bean.wscli.base.Session sessionSMS = new Session(); 
		sessionSMS.setUserId("");
		sessionSMS.setSessionId("");
		sessionSMS.setSourceURL("");
		sessionSMS.setHostIpAddress(appProps.getString("constant.notificaciones.wscli.ip.id"));
		sessionSMS.setChannelId(appProps.getString("constant.notificaciones.wscli.chanel.id"));
		sessionSMS.setHostName(appProps.getString("constant.notificaciones.wscli.hostname.id"));
		
		request.setSession( sessionSMS );
		
		/*Telefono*/
		Receiver receiver = new Receiver();
		receiver.setLoginId("");
		receiver.setTelephoneList(input.getData().getPhoneNumber());
		
		/*Plantilla*/
		Template templateSMS = new Template();
		templateSMS.setTemplateId(input.getData().getNotificationId());
		
		Data dataSMS = new Data();
		dataSMS.setReceiver(receiver);
		dataSMS.setTemplate(templateSMS);
		
		/*validamos los parametros*/
		if(!mapperObj.writeValueAsString(input.getData().getParameters()).contains("parametro_vacio"))
            dataSMS.setParameters(input.getData().getParameters());
		
		request.setData(dataSMS);
		
		smsDao.sendSMSNotification(request);
	}
	
}
