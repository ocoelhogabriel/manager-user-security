package com.ocoelhogabriel.manager_user_security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User Model")
public class UserModel {

	@NotBlank(message = "The 'name' field is mandatory and cannot be blank.")
	@Schema(name = "name", description = "User's name registration.", example = "Administrator", format = "String")
	private String name;

	@NotBlank(message = "The 'cpf' field is mandatory and cannot be blank. Enter numbers only.")
	@Schema(name = "cpf", description = "User's CPF registration. (Numbers only)", example = "12332123212", format = "Long")
	private Long cpf;

	@NotBlank(message = "The 'username' field is mandatory and cannot be blank.")
	@Schema(name = "username", description = "User's login registration.", example = "admin", format = "String")
	private String username;

	@NotBlank(message = "The 'password' field is mandatory and cannot be blank.")
	@Schema(name = "password", description = "User's password registration.", example = "admin", format = "String")
	private String password;

	@NotBlank(message = "The 'email' field is mandatory and cannot be blank.")
	@Schema(name = "email", description = "User's email registration.", example = "admin@admin.com", format = "String")
	private String email;

	@NotBlank(message = "The 'company' field is mandatory and cannot be blank.")
	@Schema(name = "company", description = "User's company code.", example = "1", format = "Long")
	private Long company;

	@NotBlank(message = "The 'profile' field is mandatory and cannot be blank.")
	@Schema(name = "profile", description = "User's profile code.", example = "1", format = "Long")
	private Long profile;

	@NotBlank(message = "The 'coverage' field is mandatory and cannot be blank.")
	@Schema(name = "coverage", description = "User's coverage code.", example = "1", format = "Long")
	private Long coverage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCompany() {
		return company;
	}

	public void setCompany(Long company) {
		this.company = company;
	}

	public Long getProfile() {
		return profile;
	}

	public void setProfile(Long profile) {
		this.profile = profile;
	}

	public Long getCoverage() {
		return coverage;
	}

	public void setCoverage(Long coverage) {
		this.coverage = coverage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [");
		if (name != null) {
			builder.append("name=").append(name).append(", ");
		}
		if (cpf != null) {
			builder.append("cpf=").append(cpf).append(", ");
		}
		if (username != null) {
			builder.append("username=").append(username).append(", ");
		}
		if (password != null) {
			builder.append("password=").append(password).append(", ");
		}
		if (email != null) {
			builder.append("email=").append(email).append(", ");
		}
		if (company != null) {
			builder.append("company=").append(company).append(", ");
		}
		if (profile != null) {
			builder.append("profile=").append(profile).append(", ");
		}
		if (coverage != null) {
			builder.append("coverage=").append(coverage);
		}
		builder.append("]");
		return builder.toString();
	}

	public UserModel(String name, Long cpf, String username, String password, String email, Long company, Long profile, Long coverage) {
		super();
		this.name = name;
		this.cpf = cpf;
		this.username = username;
		this.password = password;
		this.email = email;
		this.company = company;
		this.profile = profile;
		this.coverage = coverage;
	}

	public UserModel() {
		super();

	}

}