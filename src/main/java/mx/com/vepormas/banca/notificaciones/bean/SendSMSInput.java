package mx.com.vepormas.banca.notificaciones.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import mx.com.vepormas.banca.notificaciones.endpoint.NotificationsEndpoint;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendSMSInput", propOrder = {
    "phoneNumber",
    "notificationId",
    "parameters"
}, namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI)
public class SendSMSInput {

	@XmlElement( name="phoneNumber", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private String phoneNumber;
	
	@XmlElement( name="notificationId", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private String notificationId;
	
	@XmlElement( name="parameters", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private List<ParametersInput> parameters;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public List<ParametersInput> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParametersInput> parameters) {
		this.parameters = parameters;
	}
}
