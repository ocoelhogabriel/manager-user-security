package com.ocoelhogabriel.manager_user_security.model.dto;

public class ResponseDeviceDTO {

	private String token;
	private String data;
	private String expiration;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseDeviceDTO [");
		if (token != null) {
			builder.append("token=").append(token).append(", ");
		}
		if (data != null) {
			builder.append("data=").append(data).append(", ");
		}
		if (expiration != null) {
			builder.append("expiration=").append(expiration);
		}
		builder.append("]");
		return builder.toString();
	}

	public ResponseDeviceDTO(String token, String data, String expiration) {
		super();
		this.token = token;
		this.data = data;
		this.expiration = expiration;
	}

	public ResponseDeviceDTO() {
		super();

	}

}
