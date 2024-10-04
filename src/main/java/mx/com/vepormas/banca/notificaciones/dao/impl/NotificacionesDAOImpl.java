package mx.com.vepormas.banca.notificaciones.dao.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import mx.com.vepormas.banca.lib.bean.entity.Correo;
import mx.com.vepormas.banca.lib.bean.entity.Plantilla;
import mx.com.vepormas.banca.lib.dao.AbstractDataAccessObjectWSCli;
import mx.com.vepormas.banca.lib.exception.MissingPropertyException;
import mx.com.vepormas.banca.notificaciones.dao.NotificacionesDAO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoOutputDTO;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.Consultaemailynumtel;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.ConsultaemailynumtelResponse;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.EnquiryInput;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.EnquiryInputCollection;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.WebRequestCommon;
import mx.com.vepormas.lib.exception.InitializationException;
import mx.com.vepormas.lib.resources.ApplicationProperties;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

public class NotificacionesDAOImpl extends AbstractDataAccessObjectWSCli
		implements NotificacionesDAO {
	private static final String TIPO_NOTIFICACION = "tipo.notificacion.reference";
	private static final String CATALOGO_REGISTRO_ACTIVO = "catalogo.registro.activo";
	public NotificacionesDAOImpl(ApplicationProperties appProps,
			SessionFactory sessionFactory, WebServiceTemplate webServiceTemplate)
			throws InitializationException {
		super(appProps, sessionFactory, webServiceTemplate);
	}

	@SuppressWarnings("unchecked")
	public ConsultaemailynumtelResponse getUserInformationForNotificationDAO(
			UserInfoInputDTO infoOut) throws MissingPropertyException {

		Consultaemailynumtel request = new Consultaemailynumtel();
		WebRequestCommon common = new WebRequestCommon();

		common.setCompany(infoOut.getCompanyT24());
		common.setPassword(infoOut.getPasswordT24());
		common.setUserName(infoOut.getUserNameT24());

		List<EnquiryInputCollection> listParameter = new ArrayList<EnquiryInputCollection>();
		EnquiryInputCollection parameter = new EnquiryInputCollection();
		parameter.setColumnName(appProps
				.getString("constant.wscli.t24.request.columnname.id"));
		parameter.setCriteriaValue(infoOut.getId());

		parameter.setOperand(appProps
				.getString("constant.wscli.t24.request.operand.equals"));

		listParameter.add(parameter);

		parameter = new EnquiryInputCollection();
		parameter
				.setColumnName(appProps
						.getString("constant.wscli.t24.request.columnname.customer.id"));
		parameter.setCriteriaValue(infoOut.getClienteID());

		parameter.setOperand(appProps
				.getString("constant.wscli.t24.request.operand.equals"));

		listParameter.add(parameter);

		request.setWebRequestCommon(common);

		EnquiryInput input = new EnquiryInput();
		input.setEnquiryInputCollection(listParameter);
		request.setVPMEMAILTELNOTIFType(input);

		/* Llamada a web service T24 */
		JAXBElement<ConsultaemailynumtelResponse> responseJaxB = (JAXBElement<ConsultaemailynumtelResponse>) getWebServiceTemplate().marshalSendAndReceive(request);
		
		return responseJaxB.getValue();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Plantilla> getNotificationsCatalogDAO(Integer idSubTipoPlantilla) {
		List<Plantilla> listEntity = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Plantilla.class);
		criteria.createAlias("subTipoPlantilla", "sp");
		criteria.createAlias("sp.tipoPlantilla", "p");
		criteria.add(Restrictions.eq("p.referencia", appProps.getString(TIPO_NOTIFICACION)));
		criteria.add(Restrictions.eq("sp.idSubTipoPlantilla", idSubTipoPlantilla));
		criteria.add(Restrictions.eq("estado", appProps.getByte(CATALOGO_REGISTRO_ACTIVO)));
		criteria.setFetchMode("parametroList", FetchMode.JOIN);
		criteria.setFetchMode("subTipoPlantilla", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		listEntity = (List<Plantilla>) criteria.list();
		return listEntity;
	}

	@Transactional
	public Plantilla getPlantillaById(Integer idPlantilla) {
		Plantilla entity = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Plantilla.class);
		criteria.add(Restrictions.eq("idPlantilla", idPlantilla));
		criteria.add(Restrictions.eq("estado", appProps.getByte(CATALOGO_REGISTRO_ACTIVO)));
		entity = (Plantilla) criteria.uniqueResult();
		return entity;
	}
	
	@Transactional
	public Plantilla getPlantillaByReference(String reference) {
		Plantilla entity = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Plantilla.class);
		criteria.add(Restrictions.eq("referencia", reference));
		criteria.add(Restrictions.eq("estado", appProps.getByte(CATALOGO_REGISTRO_ACTIVO)));
		entity = (Plantilla) criteria.uniqueResult();
		return entity;
	}


	@Transactional
	public Serializable save(Serializable entity){
		return (Serializable) sessionFactory.getCurrentSession().save(entity);
	}
	
	@Transactional
	public void update(Serializable entity){
		sessionFactory.getCurrentSession().update(entity);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Correo> getCorreoListByEstadoCorreo(Integer estadoCorreo) {
		List<Correo> listEntity = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Correo.class);
		criteria.add(Restrictions.eq("estadoCorreo.idEstadoCorreo", estadoCorreo));
		criteria.setFetchMode("correoAdjuntoList", FetchMode.JOIN);
		listEntity = criteria.list();
		return listEntity;
	}

	@Override
	protected String getWebServiceTemplateBaseURI() {
		return "application.notificaciones.wscli.informacion.usuario.endpoint";
	}

	
	@Transactional
	public Long getUserIDFromDB(String principal) {
		String queryString = "SELECT ID_USUARIO FROM USUARIO WHERE PRINCIPAL = :principal AND ID_IDM = 1";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery( queryString );
		query.setParameter("principal", principal);
		
		BigInteger tmp = (BigInteger)query.uniqueResult();
		
		if(tmp != null){
			
			return tmp.longValue();
		}else{
			
			return null;
		}
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public UserInfoOutputDTO getCorreoFomrDB(UserInfoInputDTO input){
		
		UserInfoOutputDTO dto = new UserInfoOutputDTO();
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery( appProps.getString("constant.notificaciones.dao.email.idcore.cliente") );
		query.setParameter("idCore", input.getId());
		query.setParameter("idCliente", input.getClienteID());
		
		List<Object[]> listResult = (List<Object[]>)query.list();
		
		if( listResult != null && listResult.size() > 0  ){
			
			Object[] obj = listResult.get(0);
			
			if(obj[0] != null){
				dto.setEmail(obj[0].toString());
			}
			
			if(obj[1] != null){
				dto.setTelephone(obj[1].toString());
			}
			
		}else{
			
			query = sessionFactory.getCurrentSession().createSQLQuery( appProps.getString("constant.notificaciones.dao.email.idcore") );
			query.setParameter("idCore", input.getId());
			
			listResult = (List<Object[]>)query.list();
			
			if( listResult != null && listResult.size() > 0  ){
				
				Object[] obj = listResult.get(0);
				
				if(obj[0] != null){
					dto.setEmail(obj[0].toString());
				}
				
				if(obj[1] != null){
					dto.setTelephone(obj[1].toString());
				}
			}
		}
		
		return dto;
	}

}
