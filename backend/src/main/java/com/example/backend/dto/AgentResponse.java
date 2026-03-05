package com.example.backend.dto;

public class AgentResponse {
    private Long id;
    private String name;
    private String corporation;
    private String specialization;
    private String bio;
    private int suspicionLevel;
    private int credits;
    private String createdAt;
    private int reputation;

    public AgentResponse(Long id, String name, String corporation, String specialization, String bio,
                         int suspicionLevel, int credits, String createdAt, int reputation) {
        this.id = id;
        this.name = name;
        this.corporation = corporation;
        this.specialization = specialization;
        this.bio = bio;
        this.suspicionLevel = suspicionLevel;
        this.credits = credits;
        this.createdAt = createdAt;
        this.reputation = reputation;
    }

    // геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCorporation() { return corporation; }
    public String getSpecialization() { return specialization; }
    public String getBio() { return bio; }
    public int getSuspicionLevel() { return suspicionLevel; }
    public int getCredits() { return credits; }
    public String getCreatedAt() { return createdAt; }
    public int getReputation() { return reputation; }
}