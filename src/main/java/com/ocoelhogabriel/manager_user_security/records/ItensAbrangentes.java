package com.ocoelhogabriel.manager_user_security.records;

import java.util.List;

import com.ocoelhogabriel.manager_user_security.model.dto.EmpresaDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.PlantaDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloModuloDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.TipoSiloDTO;

public record ItensAbrangentes(List<EmpresaDTO> empresas, List<PlantaDTO> planta, List<TipoSiloDTO> tipoSilo, List<SiloDTO> silo, List<SiloModuloDTO> modulo) {

}
