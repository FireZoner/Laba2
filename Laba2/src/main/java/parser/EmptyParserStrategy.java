/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import builders.MissionBuilder;
import model.enums.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 * @author zubbo
 */
public class EmptyParserStrategy implements ParserStrategy {
    
    @Override
    public boolean supports(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            if (firstLine == null) return false;
            
            firstLine = firstLine.trim();
            if (!firstLine.contains("|")) return false;
            
            String[] parts = firstLine.split("\\|");
            if (parts.length < 2) return false;
            
            String type = parts[0];
            
            return type.equals("MISSION_CREATED") || 
                   type.equals("CURSE_DETECTED") ||
                   type.equals("SORCERER_ASSIGNED") ||
                   type.equals("TECHNIQUE_USED") ||
                   type.equals("TIMELINE_EVENT") ||
                   type.equals("ENEMY_ACTION") ||
                   type.equals("CIVILIAN_IMPACT") ||
                   type.equals("MISSION_RESULT");
                   
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                
                String type = parts[0];
                
                switch (type) {
                    case "MISSION_CREATED" -> {
                        if (parts.length >= 4) {
                            builder.setMissionId(parts[1])
                                   .setDate(parts[2])
                                   .setLocation(parts[3]);
                            if (parts.length >= 5 && parts[4] != null && !parts[4].isEmpty()) {
                                builder.setOutcome(Outcome.parse(parts[4]));
                            }
                        }
                    }
                    
                    case "CURSE_DETECTED" -> {
                        if (parts.length >= 3) {
                            builder.setCurse(parts[1], ThreatLevel.parse(parts[2]));
                        }
                    }
                    
                    case "SORCERER_ASSIGNED" -> {
                        if (parts.length >= 3) {
                            builder.addSorcerer(parts[1], Rank.parse(parts[2]));
                        }
                    }
                    
                    case "TECHNIQUE_USED" -> {
                        if (parts.length >= 5) {
                            builder.addTechnique(
                                parts[1],
                                TechniqueType.parse(parts[2]),
                                parts[3],
                                parseLong(parts[4])
                            );
                        }
                    }
                    
                    case "TIMELINE_EVENT" -> {
                        if (parts.length >= 4) {
                            OperationTimeline event = new OperationTimeline();
                            try {
                                event.setTimestamp(LocalDateTime.parse(parts[1]));
                            } catch (Exception e) {
                                System.out.println("Failed to parse timestamp: " + parts[1]);
                            }
                            event.setType(TimelineEventType.parse(parts[2]));
                            event.setDescription(parts[3]);
                            builder.addTimelineEvent(event);
                        }
                    }
                    
                    case "ENEMY_ACTION" -> {
                        if (parts.length >= 3) {
                            EnemyActivity activity = builder.build().getEnemyActivity();
                            if (activity == null) {
                                activity = new EnemyActivity();
                            }
                            activity.setBehaviorType(BehaviorType.parse(parts[1]));
                            if (parts.length >= 3 && parts[2] != null && !parts[2].isEmpty()) {
                                activity.addAttackPattern(parts[2]);
                            }
                            builder.setEnemyActivity(activity);
                        }
                    }
                    
                    case "CIVILIAN_IMPACT" -> {
                        if (parts.length >= 2) {
                            CivilianImpact impact = new CivilianImpact();
                            for (int i = 1; i < parts.length; i++) {
                                String[] kv = parts[i].split("=");
                                if (kv.length == 2) {
                                    switch (kv[0]) {
                                        case "evacuated" -> impact.setEvacuated(parseInt(kv[1]));
                                        case "injured" -> impact.setInjured(parseInt(kv[1]));
                                        case "missing" -> impact.setMissing(parseInt(kv[1]));
                                    }
                                }
                            }
                            builder.setCivilianImpact(impact);
                        }
                    }
                    
                    case "MISSION_RESULT" -> {
                        if (parts.length >= 2) {
                            builder.setOutcome(Outcome.parse(parts[1]));
                            if (parts.length >= 3 && parts[2] != null && parts[2].startsWith("damageCost=")) {
                                String damageStr = parts[2].substring(11);
                                builder.setDamageCost(parseLong(damageStr));
                            }
                        }
                    }
                }
            }
        }
        
        return builder.build();
    }
    
    private long parseLong(String value) {
        if (value == null || value.isEmpty()) return 0;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + value);
            return 0;
        }
    }
    
    private int parseInt(String value) {
        if (value == null || value.isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer: " + value);
            return 0;
        }
    }
}