package com.example.backend.dto;

public class MissionTemplateDto {
    private Long id;
    private String type;
    private String name;
    private String description;
    private int baseReward;
    private int baseRisk;
    private int minHackingLevel;
    private int minSocialLevel;
    private int minStealthLevel;
    private int minAnalysisLevel;
    private int stepCount;
    private int minReputation;

    // конструктор
    public MissionTemplateDto(Long id, String type, String name, String description, int baseReward,
                              int baseRisk, int minHackingLevel, int minSocialLevel, int minStealthLevel,
                              int minAnalysisLevel, int stepCount, int minReputation) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.baseReward = baseReward;
        this.baseRisk = baseRisk;
        this.minHackingLevel = minHackingLevel;
        this.minSocialLevel = minSocialLevel;
        this.minStealthLevel = minStealthLevel;
        this.minAnalysisLevel = minAnalysisLevel;
        this.stepCount = stepCount;
        this.minReputation = minReputation;
    }

    // геттеры
    public Long getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getBaseReward() { return baseReward; }
    public int getBaseRisk() { return baseRisk; }
    public int getMinHackingLevel() { return minHackingLevel; }
    public int getMinSocialLevel() { return minSocialLevel; }
    public int getMinStealthLevel() { return minStealthLevel; }
    public int getMinAnalysisLevel() { return minAnalysisLevel; }
    public int getStepCount() { return stepCount; }
    public int getMinReputation() { return minReputation; }
}