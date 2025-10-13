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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "perfil")
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "percod", nullable = false)
	private Long percod;

	@NotNull
	@Column(name = "pernom", nullable = false)
	private String pernom;

	@Column(name = "perdes")
	private String perdes;

	public Long getPercod() {
		return percod;
	}

	public void setPercod(Long percod) {
		this.percod = percod;
	}

	public String getPernom() {
		return pernom;
	}

	public void setPernom(String pernom) {
		this.pernom = pernom;
	}

	public String getPerdes() {
		return perdes;
	}

	public void setPerdes(String perdes) {
		this.perdes = perdes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Perfil [");
		if (percod != null) {
			builder.append("percod=").append(percod).append(", ");
		}
		if (pernom != null) {
			builder.append("pernom=").append(pernom).append(", ");
		}
		if (perdes != null) {
			builder.append("perdes=").append(perdes);
		}
		builder.append("]");
		return builder.toString();
	}

	public Perfil(Long percod, String pernom, String perdes) {
		super();
		this.percod = percod;
		this.pernom = pernom;
		this.perdes = perdes;
	}

	public Perfil() {
		super();

	}

	public static Specification<Perfil> filterByFields(String searchTerm) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Add predicates for string fields
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("pernom")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("perdes")), likePattern));

				// Attempt to convert the search term to Long
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("percod"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
}
