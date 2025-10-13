package com.ocoelhogabriel.manager_user_security.model.entity;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ocoelhogabriel.manager_user_security.utils.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "medicao")
public class Medicao {

	@Id
	@Column(nullable = false)
	private Date msidth;
	@ManyToOne
	@JoinColumn(name = "smocod", nullable = false)
	private SiloModulo modulo;
	private Double msiumi;
	private Double msiana;
	private Double msibar;
	private Double msitem;
	private Double msidis;

	public Medicao() {
	}

	public Medicao(Date msidth, SiloModulo modulo, Double msiumi, Double msiana, Double msibar, Double msitem,
			Double msidis) {
		this.msidth = msidth;
		// this.silo = silo;
		this.modulo = modulo;
		this.msiumi = msiumi;
		this.msiana = msiana;
		this.msibar = msibar;
		this.msitem = msitem;
		this.msidis = msidis;
	}

	public Medicao updateMedicao(SiloModulo modulo, Double msiumi, Double msiana, Double msibar, Double msitem,
			Double msidis) {
		// this.silo = silo;
		this.modulo = modulo;
		this.msiumi = msiumi;
		this.msiana = msiana;
		this.msibar = msibar;
		this.msitem = msitem;
		this.msidis = msidis;
		return this;
	}

	public Date getMsidth() {
		return msidth;
	}

	public void setMsidth(Date msidth) {
		this.msidth = msidth;
	}

	public SiloModulo getModulo() {
		return modulo;
	}

	public void setModulo(SiloModulo silomodulo) {
		this.modulo = silomodulo;
	}

	public Double getMsiumi() {
		return msiumi;
	}

	public void setMsiumi(Double msiumi) {
		this.msiumi = msiumi;
	}

	public Double getMsiana() {
		return msiana;
	}

	public void setMsiana(Double msiana) {
		this.msiana = msiana;
	}

	public Double getMsibar() {
		return msibar;
	}

	public void setMsibar(Double msibar) {
		this.msibar = msibar;
	}

	public Double getMsitem() {
		return msitem;
	}

	public void setMsitem(Double msitem) {
		this.msitem = msitem;
	}

	public Double getMsidis() {
		return msidis;
	}

	public void setMsidis(Double msidis) {
		this.msidis = msidis;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Medicao [");
		if (msidth != null) {
			builder.append("msidth=").append(msidth).append(", ");
		}
		if (modulo != null) {
			builder.append("modulo=").append(modulo).append(", ");
		}
		if (msiumi != null) {
			builder.append("msiumi=").append(msiumi).append(", ");
		}
		if (msiana != null) {
			builder.append("msiana=").append(msiana).append(", ");
		}
		if (msibar != null) {
			builder.append("msibar=").append(msibar).append(", ");
		}
		if (msitem != null) {
			builder.append("msitem=").append(msitem).append(", ");
		}
		if (msidis != null) {
			builder.append("msidis=").append(msidis);
		}
		builder.append("]");
		return builder.toString();
	}

	public static Specification<Medicao> filterByFields(String searchTerm, Long smocod, String dataInicio,
			String dataFim, String sortDirection) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtragem pelo IDs de SiloModulo
			if (smocod != null) {
				predicates.add(criteriaBuilder.equal(root.get("modulo").get("smocod"), smocod));
			}

			// Filtragem por intervalo de datas
			if (dataInicio != null && !dataInicio.isEmpty()) {
				try {
					LocalDate startDate = Utils.convertStringToDateForDateTimeFormatter(dataInicio);
					if (startDate != null) {
						if (dataFim == null || dataFim.isEmpty()) {
							// Caso apenas dataInicio esteja presente
							predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("msidth").as(LocalDate.class), startDate));
						} else {
							// Caso dataInicio e dataFim estejam presentes
							LocalDate endDate = Utils.convertStringToDateForDateTimeFormatter(dataFim);
							if (endDate != null) {
								predicates.add(criteriaBuilder.between(root.get("msidth").as(LocalDate.class), startDate, endDate));
							}
						}
					}
				} catch (DateTimeParseException e) {
					// Registro do erro ou alguma ação apropriada
				}
			} else if (dataFim != null && !dataFim.isEmpty()) {
				try {
					LocalDate endDate = Utils.convertStringToDateForDateTimeFormatter(dataFim);
					if (endDate != null) {
						// Caso apenas dataFim esteja presente
						predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("msidth").as(LocalDate.class), endDate));
					}
				} catch (DateTimeParseException e) {
					// Registro do erro ou alguma ação apropriada
				}
			}

			// Filtragem por termo de busca
			if (searchTerm != null && !searchTerm.isEmpty()) {
				List<Predicate> searchPredicates = new ArrayList<>();

				// Tenta converter o termo de busca para Double
				try {
					Double searchTermDouble = Double.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("msiumi"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("msiana"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("msibar"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("msitem"), searchTermDouble));
					searchPredicates.add(criteriaBuilder.equal(root.get("msidis"), searchTermDouble));
				} catch (NumberFormatException e) {
					// Ignora se a conversão para Double falhar
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			// Adiciona a ordenação
			if (sortDirection != null && !sortDirection.isEmpty()) {
				if ("DESC".equalsIgnoreCase(sortDirection)) {
					query.orderBy(criteriaBuilder.desc(root.get("msidth")));
				} else {
					query.orderBy(criteriaBuilder.asc(root.get("msidth")));
				}
			}
			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

		};
	}

}
