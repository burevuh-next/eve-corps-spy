package com.example.backend.dto;

import java.util.List;

public class CurrentMissionDto {
    private Long id;
    private String name;
    private String description;
    private int currentStep;
    private int totalSteps;
    private int riskAccumulated;
    private int reward;
    private List<MissionStepDto> steps;

    public CurrentMissionDto(Long id, String name, String description, int currentStep, int totalSteps,
                             int riskAccumulated, int reward, List<MissionStepDto> steps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currentStep = currentStep;
        this.totalSteps = totalSteps;
        this.riskAccumulated = riskAccumulated;
        this.reward = reward;
        this.steps = steps;
    }

    // геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCurrentStep() { return currentStep; }
    public int getTotalSteps() { return totalSteps; }
    public int getRiskAccumulated() { return riskAccumulated; }
    public int getReward() { return reward; }
    public List<MissionStepDto> getSteps() { return steps; }
}