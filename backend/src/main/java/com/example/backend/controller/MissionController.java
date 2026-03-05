package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CurrentMissionDto;
import com.example.backend.dto.MissionTemplateDto;
import com.example.backend.entity.Agent;
import com.example.backend.service.AgentService;
import com.example.backend.service.MissionService;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private AgentService agentService;

    // Получить список доступных миссий
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableMissions(@AuthenticationPrincipal String userId) {
        try {
            Long uid = Long.parseLong(userId);
            // Получаем агента по userId
            Agent agent = agentService.getActiveAgentByUserId(uid);
            List<MissionTemplateDto> templates = missionService.getAvailableTemplatesForAgent(agent.getId());
            return ResponseEntity.ok(templates);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
        } catch (RuntimeException e) {
            // Если агент не найден, возвращаем пустой список или ошибку
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // Принять миссию по шаблону
    @PostMapping("/accept/{templateId}")
    public ResponseEntity<?> acceptMission(@AuthenticationPrincipal String userId,
                                            @PathVariable Long templateId) {
        try {
            Long uid = Long.parseLong(userId);
            Long agentId = agentService.getAgentByUserId(uid).getId(); // предполагаем, что у пользователя есть агент
            CurrentMissionDto mission = missionService.acceptMission(agentId, templateId);
            return ResponseEntity.ok(mission);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Получить текущую активную миссию
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentMission(@AuthenticationPrincipal String userId) {
        try {
            Long uid = Long.parseLong(userId);
            Long agentId = agentService.getAgentByUserId(uid).getId();
            CurrentMissionDto mission = missionService.getCurrentMission(agentId);
            return ResponseEntity.ok(mission);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Выполнить следующий шаг миссии
    @PostMapping("/step")
    public ResponseEntity<?> executeStep(@AuthenticationPrincipal String userId,
                                          @RequestParam Long missionId) {
        try {
            Long uid = Long.parseLong(userId);
            Long agentId = agentService.getAgentByUserId(uid).getId();
            CurrentMissionDto mission = missionService.executeStep(missionId, agentId);
            return ResponseEntity.ok(mission);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}