package com.example.backend.dto;

public class AgentSkillDto {
    private Integer skillId;
    private String skillName;
    private int level;
    private int experience;
    private int maxLevel;

    // Конструктор с параметрами
    public AgentSkillDto(Integer skillId, String skillName, int level, int experience, int maxLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.level = level;
        this.experience = experience;
        this.maxLevel = maxLevel;
    }

    // Геттеры (и сеттеры, если нужны)
    public Integer getSkillId() { return skillId; }
    public String getSkillName() { return skillName; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getMaxLevel() { return maxLevel; }
}