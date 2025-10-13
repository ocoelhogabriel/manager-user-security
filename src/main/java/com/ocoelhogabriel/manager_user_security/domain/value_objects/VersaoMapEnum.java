package com.ocoelhogabriel.manager_user_security.domain.value_objects;

/**
 * Enum para mapeamento de versões
 * Aplica Object Calisthenics - Regra 8: No classes with more than two instance variables
 */
public enum VersaoMapEnum {
    V1("v1", "Version 1.0"),
    V2("v2", "Version 2.0"),
    V3("v3", "Version 3.0");
    
    private final String codigo;
    private final String descricao;
    
    VersaoMapEnum(final String codigo, final String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return this.codigo;
    }
    
    public String getDescricao() {
        return this.descricao;
    }
    
    public static VersaoMapEnum fromCodigo(final String codigo) {
        for (final VersaoMapEnum versao : values()) {
            if (versao.codigo.equals(codigo)) {
                return versao;
            }
        }
        throw new IllegalArgumentException("Unknown version code: " + codigo);
    }
    
    public static String mapDescricaoToVersao(final String descricao) {
        for (final VersaoMapEnum versao : values()) {
            if (versao.descricao.equals(descricao)) {
                return versao.codigo;
            }
        }
        throw new IllegalArgumentException("Unknown version description: " + descricao);
    }
}
