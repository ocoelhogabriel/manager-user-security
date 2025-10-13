package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

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
	@Column(name = "empcod") // Mantém nome DB para compatibilidade
	private Long id;

	@Column(name = "empcnp", nullable = false)
	private Long cnpj;

	@Column(name = "empnom", nullable = false)
	private String name;

	@Column(name = "empfan")
	private String fantasyName;

	@Column(name = "emptel")
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFantasyName() {
		return fantasyName;
	}

	public void setFantasyName(String fantasyName) {
		this.fantasyName = fantasyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Empresa [id=");
		builder.append(id);
		builder.append(", cnpj=");
		builder.append(cnpj);
		builder.append(", name=");
		builder.append(name);
		builder.append(", fantasyName=");
		builder.append(fantasyName);
		builder.append(", phone=");
		builder.append(phone);
		builder.append("]");
		return builder.toString();
	}

	public Empresa(Long id, Long cnpj, String name, String fantasyName, String phone) {
		super();
		this.id = id;
		this.cnpj = cnpj;
		this.name = name;
		this.fantasyName = fantasyName;
		this.phone = phone;
	}

	public Empresa() {
		super();

	}

	public static Specification<Empresa> filterByFields(String searchTerm, List<Long> listAbrangencia) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

		if (listAbrangencia != null && !listAbrangencia.isEmpty()) {
			predicates.add(root.get("id").in(listAbrangencia));
		}

		if (searchTerm != null && !searchTerm.isEmpty()) {
			String likePattern = "%" + searchTerm.toLowerCase() + "%";

			List<Predicate> searchPredicates = new ArrayList<>();

			// Add predicates for string fields
			searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
			searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fantasyName")), likePattern));
			searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), likePattern));

			// Attempt to convert the search term to Long and Integer
			try {
				Long searchTermLong = Long.valueOf(searchTerm);
				searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
				searchPredicates.add(criteriaBuilder.equal(root.get("cnpj"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
