package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities;

import java.io.Serial;
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

	@Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "descricao")
	private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Abrangencia [");
		if (id != null) {
			builder.append("id=").append(id).append(", ");
		}
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public Abrangencia(Long id, String nome, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
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
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), likePattern));

				// Attempt to convert the search term to Long
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
