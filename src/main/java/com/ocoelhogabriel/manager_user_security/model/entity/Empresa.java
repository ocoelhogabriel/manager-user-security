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
@Table(name = "empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "empcod")
	private Long empcod;

	@Column(name = "empcnp", nullable = false)
	private Long empcnp;

	@Column(name = "empnom", nullable = false)
	private String empnom;

	@Column(name = "empfan")
	private String empfan;

	@Column(name = "emptel")
	private String emptel;

	public Long getEmpcod() {
		return empcod;
	}

	public void setEmpcod(Long empcod) {
		this.empcod = empcod;
	}

	public Long getEmpcnp() {
		return empcnp;
	}

	public void setEmpcnp(Long empcnp) {
		this.empcnp = empcnp;
	}

	public String getEmpnom() {
		return empnom;
	}

	public void setEmpnom(String empnom) {
		this.empnom = empnom;
	}

	public String getEmpfan() {
		return empfan;
	}

	public void setEmpfan(String empfan) {
		this.empfan = empfan;
	}

	public String getEmptel() {
		return emptel;
	}

	public void setEmptel(String emptel) {
		this.emptel = emptel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Empresa [empcod=");
		builder.append(empcod);
		builder.append(", empcnp=");
		builder.append(empcnp);
		builder.append(", empnom=");
		builder.append(empnom);
		builder.append(", empfan=");
		builder.append(empfan);
		builder.append(", emptel=");
		builder.append(emptel);

		builder.append("]");
		return builder.toString();
	}

	public Empresa(Long empcod, Long empcnp, String empnom, String empfan, String emptel) {
		super();
		this.empcod = empcod;
		this.empcnp = empcnp;
		this.empnom = empnom;
		this.empfan = empfan;
		this.emptel = emptel;

	}

	public Empresa() {
		super();

	}

	public static Specification<Empresa> filterByFields(String searchTerm, List<Long> listAbrangencia) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (listAbrangencia != null && !listAbrangencia.isEmpty()) {
				predicates.add(root.get("empcod").in(listAbrangencia));
			}

			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Add predicates for string fields
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("empnom")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("empfan")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("emptel")), likePattern));

				// Attempt to convert the search term to Long and Integer
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("empcod"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("empcnp"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
