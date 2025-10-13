package com.ocoelhogabriel.manager_user_security.model.entity;

import java.util.ArrayList;
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
@Table(name = "planta")
public class Planta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long placod;
	@Column(length = 100, nullable = false)
	private String planom;
	@ManyToOne
	@JoinColumn(name = "empcod", nullable = false)
	private Empresa empresa;

	public Planta() {
	}

	public Planta(Long placod, String planom, Empresa empresa) {
		this.placod = placod;
		this.planom = planom;
		this.empresa = empresa;
	}

	public Planta plantaUpdateOrSave(String planom, Empresa empresa) {
		this.planom = planom;
		this.empresa = empresa;
		return this;
	}

	public Long getPlacod() {
		return placod;
	}

	public void setPlacod(Long placod) {
		this.placod = placod;
	}

	public String getPlanom() {
		return planom;
	}

	public void setPlanom(String planom) {
		this.planom = planom;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Planta [");
		if (placod != null) {
			builder.append("placod=").append(placod).append(", ");
		}
		if (planom != null) {
			builder.append("planom=").append(planom).append(", ");
		}
		if (empresa != null) {
			builder.append("empresa=").append(empresa);
		}
		builder.append("]");
		return builder.toString();
	}

	public static Specification<Planta> filterByFields(String searchTerm, List<Long> listPlacod) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtragem por lista de IDs da empresa
			if (listPlacod != null && !listPlacod.isEmpty()) {
				predicates.add(root.get("placod").in(listPlacod));
			}

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Adiciona predicado para o campo `planom`
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("planom")), likePattern));

				// Tenta converter o termo de busca para Long
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("placod"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignora se a conversão falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
