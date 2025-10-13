package com.ocoelhogabriel.manager_user_security.records;

import java.util.List;

import com.ocoelhogabriel.manager_user_security.model.dto.EmpresaDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.PlantaDTO;

public record ItensAbrangentes(List<EmpresaDTO> empresas, List<PlantaDTO> planta) {

}
