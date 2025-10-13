package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
	List<Usuario> findByUsucodIn(Collection<Long> list);

	Page<Usuario> findByUsucodIn(Pageable pageable, Collection<Long> list);

	Page<Usuario> findByUsulogLike(String usulog, Pageable pageable);

	Page<Usuario> findByUsulogLikeAndUsucodIn(String usulog, Pageable pageable, Collection<Long> list);

	Optional<Usuario> findByUsulog(String usulog);
}
