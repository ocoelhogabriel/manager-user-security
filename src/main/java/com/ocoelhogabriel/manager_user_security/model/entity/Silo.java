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
@Table(name = "silo")
public class Silo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long silcod;
	@ManyToOne
	@JoinColumn(name = "tsicod", nullable = false)
	private TipoSilo tipoSilo;
	@Column(length = 100, nullable = false)
	private String silnom;
	@ManyToOne
	@JoinColumn(name = "placod", nullable = false)
	private Planta planta;
	@Column(nullable = false)
	private Double sillat;
	@Column(nullable = false)
	private Double sillon;

	public Silo() {
	}

	public Silo(Long silcod, TipoSilo tipoSilo, String silnom, Planta planta, Double sillat, Double sillon) {
		this.silcod = silcod;
		this.tipoSilo = tipoSilo;
		this.silnom = silnom;
		this.planta = planta;
		this.sillat = sillat;
		this.sillon = sillon;
	}

	public Long getSilcod() {
		return silcod;
	}

	public void setSilcod(Long silcod) {
		this.silcod = silcod;
	}

	public TipoSilo getTipoSilo() {
		return tipoSilo;
	}

	public void setTipoSilo(TipoSilo tipoSilo) {
		this.tipoSilo = tipoSilo;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public String getSilnom() {
		return silnom;
	}

	public void setSilnom(String silnom) {
		this.silnom = silnom;
	}

	public Double getSillat() {
		return sillat;
	}

	public void setSillat(Double sillat) {
		this.sillat = sillat;
	}

	public Double getSillon() {
		return sillon;
	}

	public void setSillon(Double sillon) {
		this.sillon = sillon;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Silo [");
		if (silcod != null)
			builder.append("silcod=").append(silcod).append(", ");
		if (tipoSilo != null)
			builder.append("tipoSilo=").append(tipoSilo).append(", ");
		if (silnom != null)
			builder.append("silnom=").append(silnom).append(", ");
		if (planta != null)
			builder.append("planta=").append(planta).append(", ");
		if (sillat != null)
			builder.append("sillat=").append(sillat).append(", ");
		if (sillon != null)
			builder.append("sillon=").append(sillon);
		builder.append("]");
		return builder.toString();
	}

	public static Specification<Silo> filterByFields(String searchTerm, List<Long> listSilcod) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtragem por lista de IDs de Silo
			if (listSilcod != null && !listSilcod.isEmpty()) {
				predicates.add(root.get("silcod").in(listSilcod));
			}

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Adiciona predicado para o campo `silnom`
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("silnom")), likePattern));

				// Tenta converter o termo de busca para Long e Double
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("silcod"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("tipoSilo").get("tsicod"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("planta").get("placod"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Long falhar
				}

				try {
					Double searchTermDouble = Double.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("sillat"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("sillon"), searchTermDouble));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Double falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
