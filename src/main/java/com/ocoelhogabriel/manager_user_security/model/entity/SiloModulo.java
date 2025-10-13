package com.ocoelhogabriel.manager_user_security.model.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "silo_modulo")
public class SiloModulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long smocod;
	@ManyToOne
	@JoinColumn(name = "silcod", nullable = false)
	private Silo silo;
	@Column(nullable = false)
	private String smodes;
	@Column(nullable = false)
	private Long smotse;
	@Column(nullable = false, unique = true)
	private String smonse;
	@Column(nullable = false)
	private Long smotke = Long.valueOf(0);
	@Column(nullable = false)
	private Long smotme = Long.valueOf(0);
	@Column(nullable = false)
	private Integer smogmt;
	private Date smohke;
	private Date smohme;
	@Column(nullable = false)
	private String smocke = "#ff0000";
	@Column(nullable = false)
	private String smocme = "#ffff00";
	@Column(nullable = false)
	private String smosta;

	public SiloModulo(Long smocod, Silo silo, String smodes, Long smotse, String smonse, Long smotke, Long smotme, Date smohke, Date smohme, Integer smogmt, String smocke, String smocme, String smosta) {
		super();
		this.smocod = smocod;
		this.silo = silo;
		this.smodes = smodes;
		this.smotse = smotse;
		this.smonse = smonse;
		this.smotke = smotke;
		this.smotme = smotme;
		this.smohke = smohke;
		this.smohme = smohme;
		this.smogmt = smogmt;
		this.smocke = smocke;
		this.smocme = smocme;
		this.smosta = smosta;
	}

	public SiloModulo() {
		super();
	}

	public SiloModulo sireneModuloRegisterKeep(Date keepalive) {
		this.smohke = keepalive;
		return this;
	}

	public SiloModulo sireneModuloRegisterMedicao(Date medicao) {
		this.smohme = medicao;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiloModulo [");
		if (smocod != null) {
			builder.append("smocod=").append(smocod).append(", ");
		}
		if (silo != null) {
			builder.append("silo=").append(silo).append(", ");
		}
		if (smodes != null) {
			builder.append("smodes=").append(smodes).append(", ");
		}
		if (smotse != null) {
			builder.append("smotse=").append(smotse).append(", ");
		}
		if (smonse != null) {
			builder.append("smonse=").append(smonse).append(", ");
		}
		if (smotke != null) {
			builder.append("smotke=").append(smotke).append(", ");
		}
		if (smotme != null) {
			builder.append("smotme=").append(smotme).append(", ");
		}
		if (smohke != null) {
			builder.append("smohke=").append(smohke).append(", ");
		}
		if (smohme != null) {
			builder.append("smohme=").append(smohme).append(", ");
		}
		if (smogmt != null) {
			builder.append("smogmt=").append(smogmt).append(", ");
		}
		if (smocke != null) {
			builder.append("smocke=").append(smocke).append(", ");
		}
		if (smocme != null) {
			builder.append("smocme=").append(smocme).append(", ");
		}
		if (smosta != null) {
			builder.append("smosta=").append(smosta);
		}
		builder.append("]");
		return builder.toString();
	}

	public Long getSmocod() {
		return smocod;
	}

	public void setSmocod(Long smocod) {
		this.smocod = smocod;
	}

	public Silo getSilo() {
		return silo;
	}

	public void setSilo(Silo silo) {
		this.silo = silo;
	}

	public String getSmodes() {
		return smodes;
	}

	public void setSmodes(String smodes) {
		this.smodes = smodes;
	}

	public Long getSmotse() {
		return smotse;
	}

	public void setSmotse(Long smotse) {
		this.smotse = smotse;
	}

	public String getSmonse() {
		return smonse;
	}

	public void setSmonse(String smonse) {
		this.smonse = smonse;
	}

	public Long getSmotke() {
		return smotke;
	}

	public void setSmotke(Long smotke) {
		this.smotke = smotke;
	}

	public Long getSmotme() {
		return smotme;
	}

	public void setSmotme(Long smotme) {
		this.smotme = smotme;
	}

	public Date getSmohke() {
		return smohke;
	}

	public void setSmohke(Date smohke) {
		this.smohke = smohke;
	}

	public Date getSmohme() {
		return smohme;
	}

	public void setSmohme(Date smohme) {
		this.smohme = smohme;
	}

	public Integer getSmogmt() {
		return smogmt;
	}

	public void setSmogmt(Integer smogmt) {
		this.smogmt = smogmt;
	}

	public String getSmocke() {
		return smocke;
	}

	public void setSmocke(String smocke) {
		this.smocke = smocke;
	}

	public String getSmocme() {
		return smocme;
	}

	public void setSmocme(String smocme) {
		this.smocme = smocme;
	}

	public String getSmosta() {
		return smosta;
	}

	public void setSmosta(String smosta) {
		this.smosta = smosta;
	}

	public static Specification<SiloModulo> filterByFields(String searchTerm, List<Long> listSmocod) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtragem por lista de IDs de SiloModulo
			if (listSmocod != null && !listSmocod.isEmpty()) {
				predicates.add(root.get("smocod").in(listSmocod));
			}

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Adiciona predicados para os campos string
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("smodes")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("smonse")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("smosta")), likePattern));

				// Tenta converter o termo de busca para Long e Integer
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("smocod"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("smotse"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("smotke"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("smotme"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Long falhar
				}

				try {
					Integer searchTermInt = Integer.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("smogmt"), searchTermInt));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Integer falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
