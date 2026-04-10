/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import model.builder.MissionBuilder;
import model.enums.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 * @author zubbo
 */
public class PipeParserStrategy implements ParserStrategy {
    
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
                    case "MISSION_CREATED":
                        if (parts.length >= 5) {
                            builder.setMissionId(parts[1])
                                   .setDate(parts[2])
                                   .setLocation(parts[3]);
                            if (parts.length >= 5) {
                                builder.setOutcome(parseOutcome(parts[4]));
                            }
                        }
                        break;
                        
                    case "CURSE_DETECTED":
                        if (parts.length >= 3) {
                            builder.setCurse(parts[1], parseThreatLevel(parts[2]));
                        }
                        break;
                        
                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            builder.addSorcerer(parts[1], parseRank(parts[2]));
                        }
                        break;
                        
                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            builder.addTechnique(
                                parts[1],
                                parseTechniqueType(parts[2]),
                                parts[3],
                                parseLong(parts[4])
                            );
                        }
                        break;
                        
                    case "TIMELINE_EVENT":
                        if (parts.length >= 4) {
                            OperationTimeline event = new OperationTimeline();
                            event.setTimestamp(LocalDateTime.parse(parts[1]));
                            event.setType(parseTimelineEventType(parts[2]));
                            event.setDescription(parts[3]);
                            builder.addTimelineEvent(event);
                        }
                        break;
                        
                    case "ENEMY_ACTION":
                        if (parts.length >= 3) {
                            EnemyActivity activity = builder.build().getEnemyActivity();
                            if (activity == null) {
                                activity = new EnemyActivity();
                            }
                            activity.setBehaviorType(parseBehaviorType(parts[1]));
                            if (parts.length >= 3) {
                                activity.addAttackPattern(parts[2]);
                            }
                            builder.setEnemyActivity(activity);
                        }
                        break;
                        
                    case "CIVILIAN_IMPACT":
                        if (parts.length >= 2) {
                            CivilianImpact impact = new CivilianImpact();
                            for (int i = 1; i < parts.length; i++) {
                                String[] kv = parts[i].split("=");
                                if (kv.length == 2) {
                                    switch (kv[0]) {
                                        case "evacuated":
                                            impact.setEvacuated(parseInt(kv[1]));
                                            break;
                                        case "injured":
                                            impact.setInjured(parseInt(kv[1]));
                                            break;
                                        case "missing":
                                            impact.setMissing(parseInt(kv[1]));
                                            break;
                                    }
                                }
                            }
                            builder.setCivilianImpact(impact);
                        }
                        break;
                        
                    case "MISSION_RESULT":
                        if (parts.length >= 2) {
                            builder.setOutcome(parseOutcome(parts[1]));
                            if (parts.length >= 3 && parts[2].startsWith("damageCost=")) {
                                String damageStr = parts[2].substring(11);
                                builder.setDamageCost(parseLong(damageStr));
                            }
                        }
                        break;
                }
            }
        }
        
        return builder.build();
    }
    
    private Outcome parseOutcome(String value) {
        try {
            return Outcome.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private ThreatLevel parseThreatLevel(String value) {
        try {
            return ThreatLevel.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Rank parseRank(String value) {
        try {
            return Rank.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TechniqueType parseTechniqueType(String value) {
        try {
            return TechniqueType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TimelineEventType parseTimelineEventType(String value) {
        try {
            return TimelineEventType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private BehaviorType parseBehaviorType(String value) {
        try {
            return BehaviorType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
