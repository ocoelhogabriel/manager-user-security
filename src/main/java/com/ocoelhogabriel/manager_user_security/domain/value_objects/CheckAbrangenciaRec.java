package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import java.util.List;

import org.springframework.lang.NonNull;

public record CheckAbrangenciaRec(List<Long> listAbrangencia, @NonNull Integer isHier) {

}
