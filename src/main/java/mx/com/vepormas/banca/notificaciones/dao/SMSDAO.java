package mx.com.vepormas.banca.notificaciones.dao;

import mx.com.vepormas.banca.lib.exception.MissingPropertyException;
import mx.com.vepormas.banca.notificaciones.bean.wscli.SendSMSNotificationByReceiverRequest;

public interface SMSDAO {

	public void sendSMSNotification(SendSMSNotificationByReceiverRequest request) throws MissingPropertyException;
}
