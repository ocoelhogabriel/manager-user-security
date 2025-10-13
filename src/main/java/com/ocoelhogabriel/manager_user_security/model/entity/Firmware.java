package com.ocoelhogabriel.manager_user_security.model.entity;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Firmware {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long fircod;
	@Column(nullable = false)
	private String firmod;
	@Column(nullable = false)
	private String firnam;
	private String firdesc;
	@Column(nullable = false)
	private byte[] firarq;

	public Long getFircod() {
		return fircod;
	}

	public void setFircod(Long fircod) {
		this.fircod = fircod;
	}

	public String getFirmod() {
		return firmod;
	}

	public void setFirmod(String firmod) {
		this.firmod = firmod;
	}

	public String getFirnam() {
		return firnam;
	}

	public void setFirnam(String firnam) {
		this.firnam = firnam;
	}

	public String getFirdesc() {
		return firdesc;
	}

	public void setFirdesc(String firdesc) {
		this.firdesc = firdesc;
	}

	public byte[] getFirarq() {
		return firarq;
	}

	public void setFirarq(byte[] firarq) {
		this.firarq = firarq;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Firmware [");
		if (fircod != null) {
			builder.append("fircod=").append(fircod).append(", ");
		}
		if (firmod != null) {
			builder.append("firmod=").append(firmod).append(", ");
		}
		if (firnam != null) {
			builder.append("firnam=").append(firnam).append(", ");
		}
		if (firdesc != null) {
			builder.append("firdesc=").append(firdesc).append(", ");
		}
		if (firarq != null) {
			builder.append("firarq=").append(Arrays.toString(firarq));
		}
		builder.append("]");
		return builder.toString();
	}

	public Firmware(Long fircod, String firmod, String firnam, String firdesc, byte[] firarq) {
		super();
		this.fircod = fircod;
		this.firmod = firmod;
		this.firnam = firnam;
		this.firdesc = firdesc;
		this.firarq = firarq;
	}

	public Firmware() {
		super();

	}

}
