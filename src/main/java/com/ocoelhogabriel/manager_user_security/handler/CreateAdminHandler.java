package com.ocoelhogabriel.manager_user_security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ocoelhogabriel.manager_user_security.model.EmpresaModel;
import com.ocoelhogabriel.manager_user_security.model.PermissaoModel;
import com.ocoelhogabriel.manager_user_security.model.RecursoModel;
import com.ocoelhogabriel.manager_user_security.model.UsuarioModel;
import com.ocoelhogabriel.manager_user_security.model.entity.Abrangencia;
import com.ocoelhogabriel.manager_user_security.model.entity.AbrangenciaDetalhes;
import com.ocoelhogabriel.manager_user_security.model.entity.Perfil;
import com.ocoelhogabriel.manager_user_security.model.entity.Recurso;
import com.ocoelhogabriel.manager_user_security.model.enums.RecursoMapEnum;
import com.ocoelhogabriel.manager_user_security.services.impl.AbrangenciaServiceImpl;
import com.ocoelhogabriel.manager_user_security.services.impl.EmpresaServiceImpl;
import com.ocoelhogabriel.manager_user_security.services.impl.PerfilPermissaoServiceImpl;
import com.ocoelhogabriel.manager_user_security.services.impl.RecursoServiceImpl;
import com.ocoelhogabriel.manager_user_security.services.impl.UsuarioServiceImpl;
import com.ocoelhogabriel.manager_user_security.utils.JsonNodeConverter;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

@Configuration
public class CreateAdminHandler {

	private static final Logger logs = LoggerFactory.getLogger(CreateAdminHandler.class);

	@Autowired
	private UsuarioServiceImpl usuarioService;
	@Autowired
	private PerfilPermissaoServiceImpl perfilPermissaoService;
	@Autowired
	private RecursoServiceImpl recursoService;
	@Autowired
	private AbrangenciaServiceImpl abrangenciaService;
	@Autowired
	private EmpresaServiceImpl empresaService;

	private static final Long CNPJTSI = Long.valueOf("44772937000150");

	private static final String[] listaRecursos = { "PENDENCIA", "FIRMWARE", "LOGGER", "USUARIO", "PERFIL", "RECURSO", "ABRANGENCIA", "EMPRESA", "PLANTA", "MEDICAO", "SILO", "TIPOSILO", "MODULO" };
	private static final String[] listaAbrangencia = { "EMPRESA", "PLANTA", "SILO", "TIPOSILO", "MODULO" };

	@PostConstruct
	public void createAdminHandler() {
		try {
			logs.info("CreateAdminHandler Start... ");
			var user = usuarioService.findLoginEntityNull("admin");
			createEmpresa();
			createRecurso();
			createPerfilPermissao();
			createPerfilPermissaoDevice();
			createAbrangencia();
			createAbrangenciaDevice();
			if (user == null) {
				logs.info("CreateAdminHandler Init Create... ");
				createUsuario();
				createUsuarioDevice();
			}
		} catch (Exception e) {
			logs.error("CreateAdminHandler: ", e);
		}
	}

	public void createEmpresa() {
		try {
			logs.info("createEmpresa Start... ");
			var empresa = empresaService.empresaFindByCnpjEntity(CNPJTSI);

			EmpresaModel empresaModel = new EmpresaModel(CNPJTSI, "Telemática - Sistemas Inteligentes", "TSI", "(99)99999-9999");
			if (empresa == null)
				empresaService.empresaSave(empresaModel);

		} catch (IOException e) {
			logs.error("Error createEmpresa: ", e);
		}
	}

	public void createPerfilPermissao() {
		try {
			logs.info("createPerfil Start... ");
			var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
			if (perfil == null)
				perfil = perfilPermissaoService.createUpdatePerfil(new Perfil(null, "ADMIN", "Perfil do Administrador."));
			else
				perfil = perfilPermissaoService.createUpdatePerfil(new Perfil(perfil.getPercod(), "ADMIN", "Perfil do Administrador."));
			int listItem = listaRecursos.length;
			for (int i = 0; i < listItem; i++) {
				RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecursos[i]);
				var valueRecurso = perfilPermissaoService.findByPerfilAndRecurso(perfil, recursoService.findByIdEntity(recursoEnum.getNome()));
				PermissaoModel permissao = new PermissaoModel(recursoEnum, 1, 1, 1, 1, 1);
				if (valueRecurso == null)
					perfilPermissaoService.saveEntityPermissao(perfil, permissao);

			}

		} catch (Exception e) {
			logs.error("createPerfil: ", e);
		}
	}

	public void createPerfilPermissaoDevice() {
		try {
			logs.info("createPerfilDevice Start... ");
			var perfil = perfilPermissaoService.findByIdPerfilEntity("DEVICE");
			if (perfil == null)
				perfil = perfilPermissaoService.createUpdatePerfil(new Perfil(null, "DEVICE", "Perfil do DEVICE."));
			else
				perfil = perfilPermissaoService.createUpdatePerfil(new Perfil(perfil.getPercod(), "DEVICE", "Perfil do DEVICE."));
			int listItem = listaRecursos.length;
			for (int i = 0; i < listItem; i++) {
				RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecursos[i]);
				var valueRecurso = perfilPermissaoService.findByPerfilAndRecurso(perfil, recursoService.findByIdEntity(recursoEnum.getNome()));
				PermissaoModel permissao = new PermissaoModel(RecursoMapEnum.valueOf(listaRecursos[i]), 1, 1, 1, 1, 1);
				if (valueRecurso == null)
					perfilPermissaoService.saveEntityPermissao(perfil, permissao);

			}

		} catch (Exception e) {
			logs.error("createPerfilDevice: ", e);
		}
	}

	public void createRecurso() {
		try {
			logs.info("createRecurso Start... ");
			int listItem = listaRecursos.length;
			for (int i = 0; i < listItem; i++) {
				RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecursos[i]);
				var valueRecurso = recursoService.findByIdEntity(recursoEnum.getNome());
				RecursoModel recurso = new RecursoModel(recursoEnum, "Descrição do Recurso " + listaRecursos[i]);
				if (valueRecurso == null)
					recursoService.saveEntity(recurso);
			}
		} catch (Exception e) {
			logs.error("createRecurso: ", e);
		}
	}

	public void createAbrangencia() {
		try {
			logs.info("createAbrangencia Start...");

			Abrangencia abrangencia = abrangenciaService.findByIdEntity("ADMIN");

			if (abrangencia == null) {
				abrangencia = new Abrangencia(null, "ADMIN", "Descrição Abrangencia ADMIN");
			} else {
				abrangencia = new Abrangencia(abrangencia.getAbrcod(), "ADMIN", "Descrição Abrangencia ADMIN");
			}
			abrangenciaService.createUpdateAbrangencia(abrangencia);

			int listItem = listaAbrangencia.length;
			for (int i = 0; i < listItem; i++) {
				RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaAbrangencia[i]);
				Recurso recurso = recursoService.findByIdEntity(recursoEnum.getNome());

				JsonNodeConverter jsonNode = new JsonNodeConverter();
				String data = jsonNode.convertToDatabaseColumn(new ObjectMapper().createArrayNode());

				AbrangenciaDetalhes abd = new AbrangenciaDetalhes(null, abrangencia, recurso, 0, data);

				if (abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(abrangencia, recurso) == null)
					abrangenciaService.saveOrUpdateAbrangenciaDetalhes(abrangencia, abd);
			}
		} catch (Exception e) {
			logs.error("createAbrangencia: ", e);
		}
	}

	public void createAbrangenciaDevice() {
		try {
			logs.info("createAbrangenciaDevice Start...");

			Abrangencia abrangencia = abrangenciaService.findByIdEntity("DEVICE");

			if (abrangencia == null) {
				abrangencia = new Abrangencia(null, "DEVICE", "Descrição Abrangencia DEVICE");
			} else {
				abrangencia = new Abrangencia(abrangencia.getAbrcod(), "DEVICE", "Descrição Abrangencia DEVICE");
			}

			abrangenciaService.createUpdateAbrangencia(abrangencia);
		} catch (Exception e) {
			logs.error("createAbrangenciaDevice: ", e);
		}
	}

	public void createUsuario() {
		try {
			logs.info("createUsuario Start... ");
			var empresa = empresaService.empresaFindByCnpjEntity(CNPJTSI);
			var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
			var abrangencia = abrangenciaService.findByIdEntity("ADMIN");
			UsuarioModel usuario = new UsuarioModel("ADMIN", Long.valueOf(0), "admin", "admin", "admin@admin.com", empresa.getEmpcod(), perfil.getPercod(), abrangencia.getAbrcod());
			var userCheck = usuarioService.findLoginEntityNull("admin");
			if (userCheck == null)
				usuarioService.saveUpdateEntity(usuario);
		} catch (EntityNotFoundException | IOException e) {
			logs.error("createUsuario: ", e);
		}
	}

	public void createUsuarioDevice() {
		try {
			logs.info("createUsuarioDevice Start... ");
			var empresa = empresaService.empresaFindByCnpjEntity(CNPJTSI);
			var perfil = perfilPermissaoService.findByIdPerfilEntity("DEVICE");
			var abrangencia = abrangenciaService.findByIdEntity("DEVICE");
			UsuarioModel usuario = new UsuarioModel("DEVICE", Long.valueOf(0), "device", "device", "DEVICE@DEVICE.com", empresa.getEmpcod(), perfil.getPercod(), abrangencia.getAbrcod());
			var userCheck = usuarioService.findLoginEntityNull("device");
			if (userCheck == null)
				usuarioService.saveUpdateEntity(usuario);
		} catch (EntityNotFoundException | IOException e) {
			logs.error("createUsuarioDevice: ", e);
		}
	}
}
