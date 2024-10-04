package mx.com.vepormas.banca.notificaciones.dao.impl;

import org.springframework.ws.client.core.WebServiceTemplate;

import mx.com.vepormas.banca.lib.dao.AbstractDataAccessObjectWSCli;
import mx.com.vepormas.banca.lib.exception.MissingPropertyException;
import mx.com.vepormas.banca.notificaciones.bean.wscli.SendSMSNotificationByReceiverRequest;
import mx.com.vepormas.banca.notificaciones.dao.SMSDAO;
import mx.com.vepormas.lib.resources.ApplicationProperties;

public class SMSDAOImpl extends AbstractDataAccessObjectWSCli implements SMSDAO{

	public SMSDAOImpl(ApplicationProperties appProps, WebServiceTemplate webServiceTemplate) {
		super(appProps, webServiceTemplate);
	}
	
	
	/**
	 * 
	 * @param request
	 * @throws MissingPropertyException
	 */
	public void sendSMSNotification(SendSMSNotificationByReceiverRequest request) throws MissingPropertyException{
		
		getWebServiceTemplate(
					appProps.getString("aplication.notificaciones.wscli.sms.usuario"), 
					appProps.getString("constant.notificaciones.wscli.sms.password")
				).marshalSendAndReceive(request);
		
	}
	

	@Override
	protected String getWebServiceTemplateBaseURI() {

		return "application.notificaciones.wscli.sms.endpoint";
	}

}
