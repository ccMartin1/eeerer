package mx.com.vepormas.banca.notificaciones.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import mx.com.vepormas.banca.notificaciones.endpoint.NotificationsEndpoint;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BulkMailInput", propOrder = {
    "email",
    "parameters"
}, namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI)
public class BulkMailInput {

	@XmlElement( name="email", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private String email;
	
	@XmlElement( name="parameters", namespace=NotificationsEndpoint.SCHEMA_NAMESPACE_URI )
	private List<ParametersInput> parameters;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<ParametersInput> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParametersInput> parameters) {
		this.parameters = parameters;
	}
	
}
