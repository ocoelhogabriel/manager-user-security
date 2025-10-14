package com.ocoelhogabriel.manager_user_security.interfaces.dto;

/**
 * DTO for company responses.
 */
public class CompanyResponse {
    
    private Long id;
    private String cnpj;
    private String name;
    private String tradingName;
    private String phone;
    
    // Constructors
    public CompanyResponse() {
    }
    
    public CompanyResponse(Long id, String cnpj, String name, String tradingName, String phone) {
        this.id = id;
        this.cnpj = cnpj;
        this.name = name;
        this.tradingName = tradingName;
        this.phone = phone;
    }
    
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
