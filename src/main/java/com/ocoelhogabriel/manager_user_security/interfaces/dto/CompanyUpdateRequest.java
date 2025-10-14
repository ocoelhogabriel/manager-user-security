package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for company update requests.
 */
public class CompanyUpdateRequest {
    
    @NotNull(message = "ID is required")
    private Long id;
    
    @NotBlank(message = "CNPJ is required")
    @Pattern(regexp = "\\d{14}", message = "CNPJ must contain exactly 14 digits")
    private String cnpj;
    
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    
    @Size(max = 100, message = "Trading name must not exceed 100 characters")
    private String tradingName;
    
    @Pattern(regexp = "^$|\\d{10,14}", message = "Phone must be empty or contain 10-14 digits")
    private String phone;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTradingName() {
        return tradingName;
    }
    
    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
