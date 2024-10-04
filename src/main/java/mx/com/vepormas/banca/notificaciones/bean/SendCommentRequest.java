//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.13 at 10:50:27 AM CDT 
//


package mx.com.vepormas.banca.notificaciones.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import mx.com.vepormas.banca.lib.bean.Request;
import mx.com.vepormas.banca.notificaciones.endpoint.NotificationsEndpoint;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://vepormas.com.mx/banca/schemas/base}Request">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://vepormas.com.mx/banca/schemas/generanotificaciones}SendCommentDataInput"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SendCommentRequest", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI)
@XmlSeeAlso( value={ SendCommentDataInput.class })
public class SendCommentRequest
    extends Request<SendCommentDataInput>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 32817526103094207L;
	@XmlElement( name="data", required=false, nillable=true, namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
    protected SendCommentDataInput data;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link SendCommentDataInput }
     *     
     */
    public SendCommentDataInput getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendCommentDataInput }
     *     
     */
    public void setData(SendCommentDataInput value) {
        this.data = value;
    }

}
