
package mx.com.vepormas.banca.notificaciones.bean.wscli;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import mx.com.vepormas.banca.notificaciones.bean.wscli.base.Request;


/**
 * <p>Java class for SendSMSNotificationInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendSMSNotificationInput">
 *   &lt;complexContent>
 *     &lt;extension base="{http://vepormas.com/web/schemas/base}Request">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://vepormas.com/web/schemas/notifications}NotificationSMS"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendSMSNotificationByReceiverRequest", propOrder = {
    "data"
})
@XmlRootElement(name = "sendSMSNotificationByReceiverRequest", namespace = "http://vepormas.com/web/schemas/notifications")
public class SendSMSNotificationByReceiverRequest extends Request{

	@XmlElement(name = "data", namespace = "http://vepormas.com/web/schemas/notifications")
    private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
