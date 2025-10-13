package com.ocoelhogabriel.manager_user_security.model.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recurso")
public class Recurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reccod", nullable = false)
	private Long reccod;

	@Column(name = "recnom", nullable = false, unique = true)
	private String recnom;

	@Column(name = "recdes")
	private String recdes;

	public Long getReccod() {
		return reccod;
	}

	public void setReccod(Long reccod) {
		this.reccod = reccod;
	}

	public String getRecnom() {
		return recnom;
	}

	public void setRecnom(String recnom) {
		this.recnom = recnom;
	}

	public String getRecdes() {
		return recdes;
	}

	public void setRecdes(String recdes) {
		this.recdes = recdes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reccod, recdes, recnom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recurso other = (Recurso) obj;
		return Objects.equals(reccod, other.reccod) && Objects.equals(recdes, other.recdes) && Objects.equals(recnom, other.recnom);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Recurso [");
		if (reccod != null) {
			builder.append("reccod=").append(reccod).append(", ");
		}
		if (recnom != null) {
			builder.append("recnom=").append(recnom).append(", ");
		}
		if (recdes != null) {
			builder.append("recdes=").append(recdes);
		}
		builder.append("]");
		return builder.toString();
	}

	public Recurso(Long reccod, String recnom, String recdes) {
		super();
		this.reccod = reccod;
		this.recnom = recnom;
		this.recdes = recdes;
	}

	public Recurso() {
		super();
	}

}
