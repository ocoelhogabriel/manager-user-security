package com.ocoelhogabriel.manager_user_security.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@Table(name = "usuario")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usucod")
	private Long usucod;

	@Column(name = "usunom", nullable = false)
	private String usunom;

	@Column(name = "usucpf", nullable = false)
	private Long usucpf;

	@Column(name = "usulog", nullable = false)
	private String usulog;

	@Column(name = "ususen", nullable = false)
	private String ususen;

	@Column(name = "usuema", nullable = false)
	private String usuema;

	@ManyToOne
	@JoinColumn(name = "empcod", nullable = false)
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "percod", nullable = false)
	private Perfil perfil;

	@ManyToOne
	@JoinColumn(name = "abrcod", nullable = false)
	private Abrangencia abrangencia;

	public Usuario(Usuario usario) {
		super();
		this.usucod = usario.getUsucod();
		this.usucpf = usario.getUsucpf();
		this.usunom = usario.getUsunom();
		this.usulog = usario.getUsulog();
		this.ususen = usario.getUsusen();
		this.usuema = usario.getUsuema();
		this.empresa = usario.getEmpresa();
		this.perfil = usario.getPerfil();
		this.abrangencia = usario.getAbrangencia();
	}

	public Usuario(Long usucod, Long usucpf, String usunom, String usulog, String ususen, String usuema, Empresa empresa, Perfil perfil, Abrangencia abrangencia) {
		super();
		this.usucod = usucod;
		this.usucpf = usucpf;
		this.usunom = usunom;
		this.usulog = usulog;
		this.ususen = ususen;
		this.usuema = usuema;
		this.empresa = empresa;
		this.perfil = perfil;
		this.abrangencia = abrangencia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Usuario [");
		if (usucod != null) {
			builder.append("usucod=").append(usucod).append(", ");
		}
		if (usucpf != null) {
			builder.append("usucpf=").append(usucpf).append(", ");
		}
		if (usunom != null) {
			builder.append("usunam=").append(usunom).append(", ");
		}
		if (usulog != null) {
			builder.append("usulog=").append(usulog).append(", ");
		}
		if (ususen != null) {
			builder.append("ususen=").append(ususen).append(", ");
		}
		if (usuema != null) {
			builder.append("usuema=").append(usuema).append(", ");
		}
		if (empresa != null) {
			builder.append("empcod=").append(empresa).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil).append(", ");
		}
		if (abrangencia != null) {
			builder.append("abrangencia=").append(abrangencia);
		}
		builder.append("]");
		return builder.toString();
	}

	public Long getUsucod() {
		return usucod;
	}

	public void setUsucod(Long usucod) {
		this.usucod = usucod;
	}

	public Long getUsucpf() {
		return usucpf;
	}

	public void setUsucpf(Long usucpf) {
		this.usucpf = usucpf;
	}

	public String getUsunom() {
		return usunom;
	}

	public void setUsunom(String usunom) {
		this.usunom = usunom;
	}

	public String getUsulog() {
		return usulog;
	}

	public void setUsulog(String usulog) {
		this.usulog = usulog;
	}

	public String getUsusen() {
		return ususen;
	}

	public void setUsusen(String ususen) {
		this.ususen = ususen;
	}

	public String getUsuema() {
		return usuema;
	}

	public void setUsuema(String usuema) {
		this.usuema = usuema;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Abrangencia getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(Abrangencia abrangencia) {
		this.abrangencia = abrangencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario() {
		super();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.perfil.getPernom()));
	}

	@Override
	public String getPassword() {
		return this.getUsusen();
	}

	@Override
	public String getUsername() {
		return this.getUsulog();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static Specification<Usuario> filterByFields(String searchTerm) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (searchTerm != null && !searchTerm.isEmpty()) {
				String likePattern = "%" + searchTerm.toLowerCase() + "%";

				List<Predicate> searchPredicates = new ArrayList<>();

				// Add predicates for string fields
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("usunom")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("usulog")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("ususen")), likePattern));
				searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("usuema")), likePattern));

				// Attempt to convert the search term to Long
				try {
					Long searchTermLong = Long.valueOf(searchTerm);
					searchPredicates.add(criteriaBuilder.equal(root.get("usucod"), searchTermLong));
					searchPredicates.add(criteriaBuilder.equal(root.get("usucpf"), searchTermLong));
				} catch (NumberFormatException e) {
					// Ignore if the conversion fails
				}

				predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
}