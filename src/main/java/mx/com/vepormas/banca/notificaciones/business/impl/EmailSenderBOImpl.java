package mx.com.vepormas.banca.notificaciones.business.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.vepormas.banca.lib.bean.entity.Correo;
import mx.com.vepormas.banca.lib.business.AbstractBusinessObject;
import mx.com.vepormas.banca.notificaciones.business.EmailSenderBO;
import mx.com.vepormas.banca.notificaciones.business.NotificacionesBO;
import mx.com.vepormas.lib.resources.ApplicationProperties;


public class EmailSenderBOImpl extends AbstractBusinessObject implements EmailSenderBO {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderBOImpl.class);
	private NotificacionesBO notificacionesBO;
	
	
	
	public EmailSenderBOImpl(ApplicationProperties appProps,NotificacionesBO notificacionesBO) {
		super(appProps);
		this.notificacionesBO = notificacionesBO;
	}

	public void readAndSendCorreos() {
		LOGGER.debug("Iniciando el scheduler para envio de correos");
		List<Correo> correosPendientes = notificacionesBO.getCorreosPendientes();
		if(correosPendientes!=null && !correosPendientes.isEmpty()){
			for(Correo correo: correosPendientes){
				notificacionesBO.markAsWorkingMail(correo);
				notificacionesBO.sendAsyncNotifications(correo);
			}
		}
		LOGGER.debug("Terminando el scheduler para envio de correos con {} correos pendientes enviados", correosPendientes!=null ? correosPendientes.size() : 0);
	}

}
