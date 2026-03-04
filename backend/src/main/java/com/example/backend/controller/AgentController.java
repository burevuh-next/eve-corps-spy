package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.AgentInfoResponse;
import com.example.backend.dto.AgentResponse;
import com.example.backend.dto.AgentSkillDto;
import com.example.backend.dto.CreateAgentRequest;
import com.example.backend.entity.Agent;
import com.example.backend.entity.Corporation;
import com.example.backend.entity.User;
import com.example.backend.service.AgentService;
import com.example.backend.service.EveCorporationService;
import com.example.backend.service.UserService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(email);
    }
    
    @Autowired
    private EveCorporationService eveCorporationService;

    @GetMapping("/corporations")
    public ResponseEntity<?> getCorporations() {
        List<Corporation> corps = eveCorporationService.getNpcCorporations();
        
        // Преобразуем в DTO
        List<Map<String, Object>> result = corps.stream()
            .map(c -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", c.getEveCorporationId());
                map.put("name", c.getName());
                map.put("ticker", c.getTicker());
                map.put("memberCount", c.getMemberCount());
                return map;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAgent(@RequestBody CreateAgentRequest request,
                                        @AuthenticationPrincipal String userId) {
        System.out.println("createAgent called with userId string: " + userId);
        try {
            Long uid = Long.parseLong(userId);
            System.out.println("Parsed userId: " + uid);
            
            // Проверка существования пользователя через userService
            User user = userService.getUserById(uid); // добавить метод в UserService
            System.out.println("User found: " + (user != null));
            
            AgentResponse agentResponse = agentService.createAgent(uid, request);
            return ResponseEntity.ok(agentResponse);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAgent(@AuthenticationPrincipal String userId) {
        try {
            Long uid = Long.parseLong(userId);
            Agent agent = agentService.getActiveAgentByUserId(uid);
            return ResponseEntity.ok(agent);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	@GetMapping
	public ResponseEntity<?> getAgentInfo(@AuthenticationPrincipal String userId) {
		try {
			Long uid = Long.parseLong(userId);
			AgentInfoResponse agent = agentService.getAgentInfoByUserId(uid);
			return ResponseEntity.ok(agent);
		} catch (RuntimeException e) {
			Map<String, String> error = new HashMap<>();
			error.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		}
	}

    @GetMapping("/skills")
    public ResponseEntity<?> getSkills(@AuthenticationPrincipal String userId) {
        try {
            Long uid = Long.parseLong(userId);
            Agent agent = agentService.getActiveAgentByUserId(uid);
            List<AgentSkillDto> skills = agentService.getAgentSkills(agent.getId());
            return ResponseEntity.ok(skills);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}