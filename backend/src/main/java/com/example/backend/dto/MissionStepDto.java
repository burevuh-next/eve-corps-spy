package com.example.backend.dto;

public class MissionStepDto {
    private int stepNumber;
    private String description;
    private boolean completed;

    public MissionStepDto(int stepNumber, String description, boolean completed) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.completed = completed;
    }

    public int getStepNumber() { return stepNumber; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
}