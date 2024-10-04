package mx.com.vepormas.banca.notificaciones.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import mx.com.vepormas.banca.lib.bean.Request;
import mx.com.vepormas.banca.notificaciones.endpoint.NotificationsEndpoint;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SendSMSRequest", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI)
public class SendSMSRequest extends Request<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319554764614246404L;
	
	@XmlElement( name="data", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private SendSMSInput data;

	public SendSMSInput getData() {
		return data;
	}

	public void setData(SendSMSInput data) {
		this.data = data;
	}
}
