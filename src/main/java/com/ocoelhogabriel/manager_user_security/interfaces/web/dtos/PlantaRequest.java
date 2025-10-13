package com.ocoelhogabriel.manager_user_security.interfaces.web.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisições de criação ou atualização de plantas.
 */
public class PlantaRequest {
    
    @NotBlank(message = "O nome da planta é obrigatório")
    @Size(min = 3, max = 100, message = "O nome da planta deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "O código da planta é obrigatório")
    @Size(min = 2, max = 20, message = "O código da planta deve ter entre 2 e 20 caracteres")
    private String codigo;
    
    @NotNull(message = "O ID da empresa é obrigatório")
    private UUID empresaId;
    
    @Size(max = 255, message = "A descrição da planta deve ter no máximo 255 caracteres")
    private String descricao;
    
    @Size(max = 100, message = "O endereço da planta deve ter no máximo 100 caracteres")
    private String endereco;
    
    @Size(max = 20, message = "O telefone da planta deve ter no máximo 20 caracteres")
    private String telefone;
    
    // Getters e Setters
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public UUID getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(UUID empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}