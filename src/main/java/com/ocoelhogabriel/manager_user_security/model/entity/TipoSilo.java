package com.ocoelhogabriel.manager_user_security.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ocoelhogabriel.manager_user_security.utils.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "tipo_silo")
public class TipoSilo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long tsicod;
	@Column(nullable = false)
	private String tsinom;
	@Column(nullable = false)
	private String tsides;
	@Column(nullable = false)
	private String tsitip;
	@Column(nullable = false)
	private Double tsidse;
	@Column(nullable = false)
	private Double tsiach;

	private Double tsirai = (double) 0;
	private Double tsilar = (double) 0;
	private Double tsicom = (double) 0;

	public TipoSilo(Long tsicod, String tsinom, String tsides, String tsitip, Double tsidse, Double tsiach, Double tsirai, Double tsilar, Double tsicom) {
		super();
		this.tsicod = tsicod;
		this.tsinom = tsinom;
		this.tsides = tsides;
		this.tsitip = tsitip;
		this.tsidse = tsidse;
		this.tsiach = tsiach;
		this.tsirai = tsirai;
		this.tsilar = tsilar;
		this.tsicom = tsicom;
	}

	public void tipoSiloVertical(Double tsirai) {
		this.tsirai = Utils.converterMParaMm(tsirai);
	}

	public void tipoSiloHorizontal(Double tsilar, Double tsicom) {
		this.tsilar = Utils.converterMParaMm(tsilar);
		this.tsicom = Utils.converterMParaMm(tsicom);
	}

	public TipoSilo() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TipoSilo [");
		if (tsicod != null) {
			builder.append("tsicod=").append(tsicod).append(", ");
		}
		if (tsinom != null) {
			builder.append("tsinom=").append(tsinom).append(", ");
		}
		if (tsides != null) {
			builder.append("tsides=").append(tsides).append(", ");
		}
		if (tsitip != null) {
			builder.append("tsitip=").append(tsitip).append(", ");
		}
		if (tsidse != null) {
			builder.append("tsidse=").append(tsidse).append(", ");
		}
		if (tsiach != null) {
			builder.append("tsiach=").append(tsiach).append(", ");
		}
		if (tsirai != null) {
			builder.append("tsirai=").append(tsirai).append(", ");
		}
		if (tsilar != null) {
			builder.append("tsilar=").append(tsilar).append(", ");
		}
		if (tsicom != null) {
			builder.append("tsicom=").append(tsicom);
		}
		builder.append("]");
		return builder.toString();
	}

	public Long getTsicod() {
		return tsicod;
	}

	public void setTsicod(Long tsicod) {
		this.tsicod = tsicod;
	}

	public String getTsinom() {
		return tsinom;
	}

	public void setTsinom(String tsinom) {
		this.tsinom = tsinom;
	}

	public String getTsides() {
		return tsides;
	}

	public void setTsides(String tsides) {
		this.tsides = tsides;
	}

	public String getTsitip() {
		return tsitip;
	}

	public void setTsitip(String tsitip) {
		this.tsitip = tsitip;
	}

	public Double getTsidse() {
		return tsidse;
	}

	public void setTsidse(Double tsidse) {
		this.tsidse = tsidse;
	}

	public Double getTsiach() {
		return tsiach;
	}

	public void setTsiach(Double tsiach) {
		this.tsiach = tsiach;
	}

	public Double getTsirai() {
		return tsirai;
	}

	public void setTsirai(Double tsirai) {
		this.tsirai = tsirai;
	}

	public Double getTsilar() {
		return tsilar;
	}

	public void setTsilar(Double tsilar) {
		this.tsilar = tsilar;
	}

	public Double getTsicom() {
		return tsicom;
	}

	public void setTsicom(Double tsicom) {
		this.tsicom = tsicom;
	}

	public static Specification<TipoSilo> filterByFields(String searchTerm, List<Long> listTsicod) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtragem por lista de IDs
			if (listTsicod != null && !listTsicod.isEmpty()) {
				predicates.add(root.get("tsicod").in(listTsicod));
			}

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Adiciona predicados para os campos string
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tsinom")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tsides")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tsitip")), likePattern));

				// Tenta converter o termo de busca para Long e Double
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("tsicod"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Long falhar
				}

				try {
					Double searchTermDouble = Double.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("tsidse"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("tsiach"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("tsirai"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("tsilar"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("tsicom"), searchTermDouble));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Double falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
}
