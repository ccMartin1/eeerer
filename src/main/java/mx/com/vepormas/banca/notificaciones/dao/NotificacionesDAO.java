package mx.com.vepormas.banca.notificaciones.dao;

import java.io.Serializable;
import java.util.List;

import mx.com.vepormas.banca.lib.bean.entity.Correo;
import mx.com.vepormas.banca.lib.bean.entity.Plantilla;
import mx.com.vepormas.banca.lib.exception.MissingPropertyException;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoInputDTO;
import mx.com.vepormas.banca.notificaciones.dto.UserInfoOutputDTO;
import mx.com.vepormas.banca.notificaciones.wscli.user.information.ws_vpm_email_tel.ConsultaemailynumtelResponse;

public interface NotificacionesDAO {

	ConsultaemailynumtelResponse getUserInformationForNotificationDAO(UserInfoInputDTO infoOut)  throws MissingPropertyException;
	
	List<Plantilla> getNotificationsCatalogDAO(Integer plantillaType);
	
	Plantilla getPlantillaById(Integer idPlantilla);
	
	Plantilla getPlantillaByReference(String reference);
	
	Serializable save(Serializable entity);
	
	void update(Serializable entity);
	
	List<Correo> getCorreoListByEstadoCorreo(Integer estadoCorreo);
	
	Long getUserIDFromDB(String principal);
	
	UserInfoOutputDTO getCorreoFomrDB(UserInfoInputDTO input);
}
