package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company.Builder()
                .withCnpj("12345678000199")
                .withName("Test Company")
                .build();
    }

    @Test
    @DisplayName("Should register company successfully when CNPJ does not exist")
    void registerCompany_shouldSaveCompany_whenDataIsValid() {
        // Arrange
        when(companyRepository.existsByCnpj(company.getCnpj())).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Act
        Company savedCompany = companyService.registerCompany(company);

        // Assert
        assertNotNull(savedCompany);
        assertEquals(company.getCnpj(), savedCompany.getCnpj());
        verify(companyRepository).existsByCnpj(company.getCnpj());
        verify(companyRepository).save(company);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when CNPJ already exists")
    void registerCompany_shouldThrowException_whenCnpjAlreadyExists() {
        // Arrange
        when(companyRepository.existsByCnpj(company.getCnpj())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            companyService.registerCompany(company);
        });

        assertEquals("Company with CNPJ " + company.getCnpj() + " already exists", exception.getMessage());
        verify(companyRepository).existsByCnpj(company.getCnpj());
        verify(companyRepository, never()).save(any(Company.class));
    }
}
