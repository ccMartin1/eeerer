package mx.com.vepormas.banca.notificaciones.dto;

import java.util.List;
import java.util.Map;

public class NotificationInputDTO {

	private String principal;
	private String clienteID;
	private String emailTo;
	private String emailFrom;
	private String notificationReference;
	private Map<String, Object> parameters;
	private List<AttachmentDTO> adjuntos;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getClienteID() {
		return clienteID;
	}
	public void setClienteID(String clienteID) {
		this.clienteID = clienteID;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getNotificationReference() {
		return notificationReference;
	}
	public void setNotificationReference(String notificationReference) {
		this.notificationReference = notificationReference;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public List<AttachmentDTO> getAdjuntos() {
		return adjuntos;
	}
	public void setAdjuntos(List<AttachmentDTO> adjuntos) {
		this.adjuntos = adjuntos;
	}
	
}
