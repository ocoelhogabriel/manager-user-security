package com.ocoelhogabriel.manager_user_security.model.dto;

import java.util.List;

public class ItensPendenciasDTO {

	private List<Long> firmware;

	public ItensPendenciasDTO(List<Long> firmware) {
		super();
		this.firmware = firmware;
	}

	public ItensPendenciasDTO() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItensPendenciasDTO [");
		if (firmware != null) {
			builder.append("firmware=").append(firmware);
		}
		builder.append("]");
		return builder.toString();
	}

	public List<Long> getFirmware() {
		return firmware;
	}

	public void setFirmware(List<Long> firmware) {
		this.firmware = firmware;
	}

}
