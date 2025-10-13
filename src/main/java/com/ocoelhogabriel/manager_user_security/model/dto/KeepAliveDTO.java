package com.ocoelhogabriel.manager_user_security.model.dto;

public class KeepAliveDTO {

	private String dataKeepAlive;
	private ItensPendenciasDTO pendencias;

	public KeepAliveDTO(String dataKeepAlive, ItensPendenciasDTO pendencias) {
		super();
		this.dataKeepAlive = dataKeepAlive;
		this.pendencias = pendencias;
	}

	public KeepAliveDTO() {
		super();

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeepAliveDTO [");
		if (dataKeepAlive != null) {
			builder.append("dataKeepAlive=").append(dataKeepAlive).append(", ");
		}
		if (pendencias != null) {
			builder.append("pendencias=").append(pendencias);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getDataKeepAlive() {
		return dataKeepAlive;
	}

	public void setDataKeepAlive(String dataKeepAlive) {
		this.dataKeepAlive = dataKeepAlive;
	}

	public ItensPendenciasDTO getPendencias() {
		return pendencias;
	}

	public void setPendencias(ItensPendenciasDTO pendencias) {
		this.pendencias = pendencias;
	}

}
