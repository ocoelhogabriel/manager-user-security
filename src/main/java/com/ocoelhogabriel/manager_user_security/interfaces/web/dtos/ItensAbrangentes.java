package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import java.util.List;

/**
 * DTO que encapsula listas abrangentes de empresas e plantas.
 * Utilizado para transferir múltiplos itens relacionados em uma única resposta.
 * 
 * @param empresas Lista de empresas abrangentes.
 * @param planta   Lista de plantas abrangentes.
 */

public record ItensAbrangentes(List<EmpresaDTO> empresas, List<PlantaDTO> planta) {

}
