package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mission_templates")
public class MissionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String name;

    @Column(length = 1000)
    private String description;

    private int baseReward;
    private int baseRisk;
    private int minHackingLevel;
    private int minSocialLevel;
    private int minStealthLevel;
    private int minAnalysisLevel;
    private int stepCount;
    private boolean active = true;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getBaseReward() { return baseReward; }
    public void setBaseReward(int baseReward) { this.baseReward = baseReward; }

    public int getBaseRisk() { return baseRisk; }
    public void setBaseRisk(int baseRisk) { this.baseRisk = baseRisk; }

    public int getMinHackingLevel() { return minHackingLevel; }
    public void setMinHackingLevel(int minHackingLevel) { this.minHackingLevel = minHackingLevel; }

    public int getMinSocialLevel() { return minSocialLevel; }
    public void setMinSocialLevel(int minSocialLevel) { this.minSocialLevel = minSocialLevel; }

    public int getMinStealthLevel() { return minStealthLevel; }
    public void setMinStealthLevel(int minStealthLevel) { this.minStealthLevel = minStealthLevel; }

    public int getMinAnalysisLevel() { return minAnalysisLevel; }
    public void setMinAnalysisLevel(int minAnalysisLevel) { this.minAnalysisLevel = minAnalysisLevel; }

    public int getStepCount() { return stepCount; }
    public void setStepCount(int stepCount) { this.stepCount = stepCount; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}