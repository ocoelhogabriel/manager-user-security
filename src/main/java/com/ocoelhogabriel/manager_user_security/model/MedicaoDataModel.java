package com.ocoelhogabriel.manager_user_security.model;

public class MedicaoDataModel {

	private Double analogInput;
	private Double barometer;
	private Double humidity;
	private Double temperature;
	private Double illuminance;

	public MedicaoDataModel(Double analogInput, Double barometer, Double humidity, Double temperature, Double illuminance) {
		super();
		this.analogInput = analogInput;
		this.barometer = barometer;
		this.humidity = humidity;
		this.temperature = temperature;
		this.illuminance = illuminance;
	}

	public MedicaoDataModel() {
		super();

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MedicaoDataModel [");
		if (analogInput != null) {
			builder.append("analogInput=").append(analogInput).append(", ");
		}
		if (barometer != null) {
			builder.append("barometer=").append(barometer).append(", ");
		}
		if (humidity != null) {
			builder.append("humidity=").append(humidity).append(", ");
		}
		if (temperature != null) {
			builder.append("temperature=").append(temperature).append(", ");
		}
		if (illuminance != null) {
			builder.append("illuminance=").append(illuminance);
		}
		builder.append("]");
		return builder.toString();
	}

	public Double getAnalogInput() {
		return analogInput;
	}

	public void setAnalogInput(Double analogInput) {
		this.analogInput = analogInput;
	}

	public Double getBarometer() {
		return barometer;
	}

	public void setBarometer(Double barometer) {
		this.barometer = barometer;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(Double illuminance) {
		this.illuminance = illuminance;
	}

}
