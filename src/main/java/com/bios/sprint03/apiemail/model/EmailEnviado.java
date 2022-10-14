package com.bios.sprint03.apiemail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("emailEnviado")
public class EmailEnviado extends Email {

	@Id
	protected String id;

	protected String assuntoEmail;
	protected String corpoEmail;
	protected String dataEnvio;
	protected boolean enviadoComSucesso;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssuntoEmail() {
		return assuntoEmail;
	}

	public void setAssuntoEmail(String assuntoEmail) {
		this.assuntoEmail = assuntoEmail;
	}

	public String getCorpoEmail() {
		return corpoEmail;
	}

	public void setCorpoEmail(String corpoEmail) {
		this.corpoEmail = corpoEmail;
	}

	public String getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(String dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public boolean getEnviadoComSucesso() {
		return enviadoComSucesso;
	}

	public void setEnviadoComSucesso(boolean enviadoComSucesso) {
		this.enviadoComSucesso = enviadoComSucesso;
	}

}
