
package mx.com.vepormas.banca.notificaciones.bean.wscli;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Receiver complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Receiver">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Receiver", propOrder = {
    "loginId",
    "telephoneList"
})
public class Receiver {

	@XmlElement(name = "loginId", namespace = "http://vepormas.com/web/schemas/notifications")
    private String loginId;
	
	@XmlElement(name = "telephoneList", namespace = "http://vepormas.com/web/schemas/notifications")
	private List<String> telephoneList;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<String> getTelephoneList() {
		return telephoneList;
	}

	public void setTelephoneList(String number) {
		this.telephoneList = new ArrayList<String>();
		this.telephoneList.add(number);
	}
}
