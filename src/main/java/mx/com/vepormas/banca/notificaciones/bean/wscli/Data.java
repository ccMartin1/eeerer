package mx.com.vepormas.banca.notificaciones.bean.wscli;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "data", propOrder = {
    "template",
    "parameters",
    "receiver"
})
public class Data {

	@XmlElement(name = "template", namespace = "http://vepormas.com/web/schemas/notifications")
	private Template template;
	
	@XmlElement(name = "parameters", namespace = "http://vepormas.com/web/schemas/notifications")
	private List<Parameter> parameters;
	
	@XmlElement(name = "receiver", namespace = "http://vepormas.com/web/schemas/notifications")
	private Receiver receiver;

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<mx.com.vepormas.banca.notificaciones.bean.ParametersInput> parameters) throws Exception {
		this.parameters = new ArrayList<Parameter>();
		for (mx.com.vepormas.banca.notificaciones.bean.ParametersInput tmp : parameters) {
			Parameter parameterSMS = new Parameter();
			parameterSMS.setKey(tmp.getKey());
			parameterSMS.setValue(tmp.getValue());
			
			this.parameters.add(parameterSMS);
		}
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
}
