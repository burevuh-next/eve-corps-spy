package com.example.backend.dto;

public class AgentInfoResponse {
    private Long id;
    private String name;
    private String corporationName;
    private String specialization;
    private int suspicionLevel;
    private int credits;
    private String bio;
    private int reputation;

    public AgentInfoResponse(Long id, String name, String corporationName, String specialization, int suspicionLevel, int credits, String bio, int reputation) {
        this.id = id;
        this.name = name;
        this.corporationName = corporationName;
        this.specialization = specialization;
        this.suspicionLevel = suspicionLevel;
        this.credits = credits;
        this.bio = bio;
        this.reputation = reputation;
    }

    // геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCorporationName() { return corporationName; }
    public String getSpecialization() { return specialization; }
    public int getSuspicionLevel() { return suspicionLevel; }
    public int getCredits() { return credits; }
    public String getBio() { return bio; }
    public int getReputation() { return reputation; }
}