
package mx.com.vepormas.banca.notificaciones.bean.wscli.base;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <pJava class for Request complex type.
 * 
 * <pThe following schema fragment specifies the expected content contained within this class.
 * 
 * <pre
 * &lt;complexType name="Request"
 *   &lt;complexContent
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"
 *       &lt;sequence
 *         &lt;element name="subject" type="{http://vepormas.com/web/schemas/base}Subject" minOccurs="0"/
 *         &lt;element name="session" type="{http://vepormas.com/web/schemas/base}Session" minOccurs="0"/
 *       &lt;/sequence
 *     &lt;/restriction
 *   &lt;/complexContent
 * &lt;/complexType
 * </pre
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Request", propOrder = {
    "subject",
    "session"
})
public class Request {

    @XmlElement(name = "subject", namespace = "http://vepormas.com/web/schemas/base")
    private Subject subject;
    @XmlElement(name = "session", namespace = "http://vepormas.com/web/schemas/base")
    private Session session;

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Subject }{@code }
     *     
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Subject }{@code }
     *     
     */
    public void setSubject(Subject value) {
        this.subject = value;
    }

    /**
     * Gets the value of the session property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Session }{@code }
     *     
     */
    public Session getSession() {
        return session;
    }

    /**
     * Sets the value of the session property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Session }{@code }
     *     
     */
    public void setSession(Session value) {
        this.session = value;
    }

}
