package com.ocoelhogabriel.manager_user_security.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Medição")
public class MedicaoPayloadModel {

	private String applicationID;
	private String applicationName;
	private String devEUI;
	private String deviceName;
	private Integer timestamp;

	@Schema(name = "fCnt")
	private Long fCnt;

	@Schema(name = "fPort")
	private Long fPort;

	private String data;
	private String data_encode;
	private MedicaoDataModel object;
	private Boolean adr;
	private List<Object> rxInfo;
	private Object txInfo;

	public MedicaoPayloadModel(String applicationID, String applicationName, String devEUI, String deviceName,
			Integer timestamp, Long fCnt, Long fPort, String data, String data_encode, MedicaoDataModel object,
			Boolean adr, List<Object> rxInfo, Object txInfo) {
		super();
		this.applicationID = applicationID;
		this.applicationName = applicationName;
		this.devEUI = devEUI;
		this.deviceName = deviceName;
		this.timestamp = timestamp;
		this.fCnt = fCnt;
		this.fPort = fPort;
		this.data = data;
		this.data_encode = data_encode;
		this.object = object;
		this.adr = adr;
		this.rxInfo = rxInfo;
		this.txInfo = txInfo;
	}

	public MedicaoPayloadModel() {
		super();

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MedicaoDeviceModel [");
		if (applicationID != null) {
			builder.append("applicationID=").append(applicationID).append(", ");
		}
		if (applicationName != null) {
			builder.append("applicationName=").append(applicationName).append(", ");
		}
		if (devEUI != null) {
			builder.append("devEUI=").append(devEUI).append(", ");
		}
		if (deviceName != null) {
			builder.append("deviceName=").append(deviceName).append(", ");
		}
		if (timestamp != null) {
			builder.append("timestamp=").append(timestamp).append(", ");
		}
		if (fCnt != null) {
			builder.append("fCnt=").append(fCnt).append(", ");
		}
		if (fPort != null) {
			builder.append("fPort=").append(fPort).append(", ");
		}
		if (data != null) {
			builder.append("data=").append(data).append(", ");
		}
		if (data_encode != null) {
			builder.append("data_encode=").append(data_encode).append(", ");
		}
		if (object != null) {
			builder.append("object=").append(object).append(", ");
		}
		if (adr != null) {
			builder.append("adr=").append(adr).append(", ");
		}
		if (rxInfo != null) {
			builder.append("rxInfo=").append(rxInfo).append(", ");
		}
		if (txInfo != null) {
			builder.append("txInfo=").append(txInfo);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getDevEUI() {
		return devEUI;
	}

	public void setDevEUI(String devEUI) {
		this.devEUI = devEUI;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public Long getfCnt() {
		return fCnt;
	}

	public void setfCnt(Long fCnt) {
		this.fCnt = fCnt;
	}

	public Long getfPort() {
		return fPort;
	}

	public void setfPort(Long fPort) {
		this.fPort = fPort;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData_encode() {
		return data_encode;
	}

	public void setData_encode(String data_encode) {
		this.data_encode = data_encode;
	}

	public MedicaoDataModel getObject() {
		return object;
	}

	public void setObject(MedicaoDataModel object) {
		this.object = object;
	}

	public Boolean getAdr() {
		return adr;
	}

	public void setAdr(Boolean adr) {
		this.adr = adr;
	}

	public List<Object> getRxInfo() {
		return rxInfo;
	}

	public void setRxInfo(List<Object> rxInfo) {
		this.rxInfo = rxInfo;
	}

	public Object getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(Object txInfo) {
		this.txInfo = txInfo;
	}

}
