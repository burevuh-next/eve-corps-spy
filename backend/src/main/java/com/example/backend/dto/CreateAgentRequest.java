package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAgentRequest {

    @NotBlank(message = "Назовитесь")
    private String name;

    @NotBlank(message = "Из какой вы корпорации")
    private String corporation;

    @NotBlank(message = "Ваша специализация")
    private String specialization;

    @Size(max = 500, message = "Ваша биография")
    private String bio;

    // геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCorporation() { return corporation; }
    public void setCorporation(String corporation) { this.corporation = corporation; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}