package com.ocoelhogabriel.manager_user_security.model.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "logger")
public class LoggerEntity {

	@Id
	@Column(nullable = false)
	private Date logdat;
	@Column(nullable = false)
	private Long smocod;
	@Column(nullable = false)
	private String logtip;
	@Column(nullable = false)
	private String logmsg;

	public Date getLogdat() {
		return logdat;
	}

	public void setLogdat(Date logdat) {
		this.logdat = logdat;
	}

	public Long getSmocod() {
		return smocod;
	}

	public void setSmocod(Long smocod) {
		this.smocod = smocod;
	}

	public String getLogtip() {
		return logtip;
	}

	public void setLogtip(String logtip) {
		this.logtip = logtip;
	}

	public String getLogmsg() {
		return logmsg;
	}

	public void setLogmsg(String logmsg) {
		this.logmsg = logmsg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoggerEntity [");
		if (logdat != null) {
			builder.append("logdat=").append(logdat).append(", ");
		}
		if (smocod != null) {
			builder.append("smocod=").append(smocod).append(", ");
		}
		if (logtip != null) {
			builder.append("logtip=").append(logtip).append(", ");
		}
		if (logmsg != null) {
			builder.append("logmsg=").append(logmsg);
		}
		builder.append("]");
		return builder.toString();
	}

	public LoggerEntity(Date logdat, Long smocod, String logtip, String logmsg) {
		super();
		this.logdat = logdat;
		this.smocod = smocod;
		this.logtip = logtip;
		this.logmsg = logmsg;
	}

	public LoggerEntity() {
		super();

	}

	public static Specification<LoggerEntity> filterByFields(String filtro, String startDateStr, String endDateStr) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			Date endDate = null;

			try {
				if (startDateStr != null && !startDateStr.isEmpty()) {
					startDate = formatter.parse(startDateStr);
				}
				if (endDateStr != null && !endDateStr.isEmpty()) {
					endDate = formatter.parse(endDateStr);
				}
			} catch (ParseException e) {
				// Handle parsing exception if necessary
			}
			try {
				Long searchTermLong = Long.valueOf(filtro);
				predicates.add(criteriaBuilder.equal(root.get("logcod"), searchTermLong));
			} catch (NumberFormatException e) {
				//
			}
			// if (smocod != null) {
			// predicates.add(criteriaBuilder.equal(root.get("smocod"), smocod));
			// }

			if (filtro != null && !filtro.isEmpty()) {
				String likePattern = "%" + filtro.toLowerCase() + "%";
				List<Predicate> filtroPredicates = new ArrayList<>();
				filtroPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("logtip")), likePattern));
				filtroPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("logmsg")), likePattern));
				predicates.add(criteriaBuilder.or(filtroPredicates.toArray(Predicate[]::new)));
			}

			if (startDate != null && endDate != null) {
				predicates.add(criteriaBuilder.between(root.get("logdat"), startDate, endDate));
			} else if (startDate != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("logdat"), startDate));
			} else if (endDate != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("logdat"), endDate));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
