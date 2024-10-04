package mx.com.vepormas.banca.notificaciones.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import mx.com.vepormas.banca.lib.bean.Request;
import mx.com.vepormas.banca.notificaciones.endpoint.NotificationsEndpoint;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SendBulkMailRequest", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI)
@XmlSeeAlso( value={ BulkMailInput.class })
public class SendBulkMailRequest extends Request<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 32817526263094207L;


	@XmlElement( name="notificationId", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private String notificationId;
	
	@XmlElement( name="bulkMailInput", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private List<BulkMailInput> bulkMailInput;

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public List<BulkMailInput> getBulkMailInput() {
		return bulkMailInput;
	}

	public void setBulkMailInput(List<BulkMailInput> bulkMailInput) {
		this.bulkMailInput = bulkMailInput;
	}
}
