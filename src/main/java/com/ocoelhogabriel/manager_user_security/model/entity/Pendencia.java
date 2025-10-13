package com.ocoelhogabriel.manager_user_security.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.criteria.Predicate;

@Entity
public class Pendencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long pencod;
	@Column(nullable = false)
	private String pentip;
	@Column(nullable = false)
	private String pensta;
	private String pendes;
	@Column(nullable = false)
	private Date penini;
	private Date penfim;
	private Long smocod;
	@ManyToOne
	@JoinColumn(name = "fircod")
	private Firmware firmware;
	@Column(nullable = false)
	private Integer pendel;

	public Long getPencod() {
		return pencod;
	}

	public void setPencod(Long pencod) {
		this.pencod = pencod;
	}

	public String getPentip() {
		return pentip;
	}

	public void setPentip(String pentip) {
		this.pentip = pentip;
	}

	public String getPensta() {
		return pensta;
	}

	public void setPensta(String pensta) {
		this.pensta = pensta;
	}

	public String getPendes() {
		return pendes;
	}

	public void setPendes(String pendes) {
		this.pendes = pendes;
	}

	public Date getPenini() {
		return penini;
	}

	public void setPenini(Date penini) {
		this.penini = penini;
	}

	public Date getPenfim() {
		return penfim;
	}

	public void setPenfim(Date penfim) {
		this.penfim = penfim;
	}

	public Long getSmocod() {
		return smocod;
	}

	public void setSmocod(Long smocod) {
		this.smocod = smocod;
	}

	public Integer getPendel() {
		return pendel;
	}

	public void setPendel(Integer pendel) {
		this.pendel = pendel;
	}

	public Firmware getFirmware() {
		return firmware;
	}

	public void setFirmware(Firmware firmware) {
		this.firmware = firmware;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pendencia [");
		if (pencod != null) {
			builder.append("pencod=").append(pencod).append(", ");
		}
		if (pentip != null) {
			builder.append("pentip=").append(pentip).append(", ");
		}
		if (pensta != null) {
			builder.append("pensta=").append(pensta).append(", ");
		}
		if (pendes != null) {
			builder.append("pendes=").append(pendes).append(", ");
		}
		if (penini != null) {
			builder.append("penini=").append(penini).append(", ");
		}
		if (penfim != null) {
			builder.append("penfim=").append(penfim).append(", ");
		}
		if (smocod != null) {
			builder.append("smocod=").append(smocod).append(", ");
		}
		if (firmware != null) {
			builder.append("fircod=").append(firmware).append(", ");
		}
		if (pendel != null) {
			builder.append("pendel=").append(pendel);
		}
		builder.append("]");
		return builder.toString();
	}

	public Pendencia(Long pencod, String pentip, String pensta, String pendes, Date penini, Date penfim, Long smocod, Firmware fircod, Integer pendel) {
		super();
		this.pencod = pencod;
		this.pentip = pentip;
		this.pensta = pensta;
		this.pendes = pendes;
		this.penini = penini;
		this.penfim = penfim;
		this.smocod = smocod;
		this.firmware = fircod;
		this.pendel = pendel;
	}

	public Pendencia() {
		super();

	}

	public static Specification<Pendencia> filterByFields(String searchTerm, Long modulo, Integer pendel) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (pendel != null) {
				predicates.add(criteriaBuilder.equal(root.get("pendel"), pendel));
			}
			if (modulo != null) {
				predicates.add(criteriaBuilder.equal(root.get("smocod"), modulo));
			}

			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";
				List<Predicate> searchPredicates = new ArrayList<>();

				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("pentip")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("pensta")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("pendes")), likePattern));

				try {
					Long searchTermLong = Long.valueOf(searchTerm);

					searchPredicates.add(criteriaBuilder.equal(root.get("pencod"), searchTermLong));
					// searchPredicates.add(criteriaBuilder.equal(root.get("fircod"),
					// searchTermLong));
				} catch (NumberFormatException e) {
					//
				}

				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
					LocalDateTime searchTermDate = LocalDateTime.parse(searchTerm, formatter);
					searchPredicates.add(criteriaBuilder.equal(root.get("penini"), searchTermDate));
					searchPredicates.add(criteriaBuilder.equal(root.get("penfim"), searchTermDate));
				} catch (DateTimeParseException e) {
					//
				}

				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("penini"), criteriaBuilder.literal("YYYY-MM-DD")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("penfim"), criteriaBuilder.literal("YYYY-MM-DD")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("penini"), criteriaBuilder.literal("HH24:MI")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("penfim"), criteriaBuilder.literal("HH24:MI")), likePattern));

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
}
