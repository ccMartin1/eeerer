package mx.com.vepormas.banca.notificaciones.business;

import java.util.List;

import mx.com.vepormas.banca.notificaciones.bean.Notification;
import mx.com.vepormas.banca.notificaciones.bean.SendSMSRequest;
import mx.com.vepormas.banca.notificaciones.dto.CommentInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.NotificationInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoOutputDTO;
import mx.com.vepormas.banca.lib.bean.entity.Correo;
import mx.com.vepormas.banca.lib.exception.MissingParameterException;
import mx.com.vepormas.banca.lib.exception.business.BusinessException;

public interface NotificacionesBO {
	
	UserInfoOutputDTO getUserInformationForNotificationBO(UserInfoInputDTO infoOut) throws BusinessException, MissingParameterException;

	List <Notification> getNotificationsCatalogBO(String notificationType) throws BusinessException, MissingParameterException;

	void sendNotificationBO(NotificationInputDTO notificationInput) throws BusinessException, MissingParameterException;
	
	List<Correo> getCorreosPendientes();
	
	void sendAsyncNotifications(Correo correo);
	
	void sendCommentBO(CommentInputDTO commentInputDTO) throws BusinessException, MissingParameterException;
	
	void markAsWorkingMail(Correo correo);
	
	void sendSMSNOtification(SendSMSRequest input) throws Exception;
}
