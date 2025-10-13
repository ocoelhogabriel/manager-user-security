package com.ocoelhogabriel.manager_user_security.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "abrangencia")
public class Abrangencia implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "abrcod")
	private Long abrcod;

	@Column(name = "abrnom", nullable = false)
	private String abrnom;

	@Column(name = "abrdes")
	private String abrdes;

	public Long getAbrcod() {
		return abrcod;
	}

	public void setAbrcod(Long abrcod) {
		this.abrcod = abrcod;
	}

	public String getAbrnom() {
		return abrnom;
	}

	public void setAbrnom(String abrnom) {
		this.abrnom = abrnom;
	}

	public String getAbrdes() {
		return abrdes;
	}

	public void setAbrdes(String abrdes) {
		this.abrdes = abrdes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Abrangencia [");
		if (abrcod != null) {
			builder.append("abrcod=").append(abrcod).append(", ");
		}
		if (abrnom != null) {
			builder.append("abrnom=").append(abrnom).append(", ");
		}
		if (abrdes != null) {
			builder.append("abrdes=").append(abrdes);
		}
		builder.append("]");
		return builder.toString();
	}

	public Abrangencia(Long abrcod, String abrnom, String abrdes) {
		super();
		this.abrcod = abrcod;
		this.abrnom = abrnom;
		this.abrdes = abrdes;
	}

	public Abrangencia() {
		super();

	}

	public static Specification<Abrangencia> filterByFields(String searchTerm) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Add predicates for string fields
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("abrnom")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("abrdes")), likePattern));

				// Attempt to convert the search term to Long
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("abrcod"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
