
package mx.com.vepormas.banca.notificaciones.bean.wscli.base;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Session complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Session">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="channelId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hostIpAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Session", propOrder = {
    "userId",
    "sessionId",
    "channelId",
    "hostName",
    "hostIpAddress",
    "sourceURL"
})
public class Session {

	@XmlElement(name = "userId", namespace = "http://vepormas.com/web/schemas/base")
    private String userId;
    
    @XmlElement(name = "sessionId", namespace = "http://vepormas.com/web/schemas/base")
    private String sessionId;
    
    @XmlElement(name = "channelId", namespace = "http://vepormas.com/web/schemas/base")
    private String channelId;
   
    @XmlElement(name = "hostName", namespace = "http://vepormas.com/web/schemas/base")
    private String hostName;
    
    @XmlElement(name = "hostIpAddress", namespace = "http://vepormas.com/web/schemas/base")
    private String hostIpAddress;
   
    @XmlElement(name = "sourceURL", namespace = "http://vepormas.com/web/schemas/base")
    private String sourceURL;

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the channelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets the value of the channelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelId(String value) {
        this.channelId = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the hostIpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostIpAddress() {
        return hostIpAddress;
    }

    /**
     * Sets the value of the hostIpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostIpAddress(String value) {
        this.hostIpAddress = value;
    }

    /**
     * Gets the value of the sourceURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public String getSourceURL() {
        return sourceURL;
    }

    /**
     * Sets the value of the sourceURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSourceURL(String value) {
        this.sourceURL = value;
    }

}
