/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import model.*;
import model.builder.MissionBuilder;
import model.enums.*;
import java.io.*;

/**
 * Стратегия для секционного TXT формата
 * Формат: [SECTION] key=value
 * @author zubbo
 */
public class TxtParserStrategy implements ParserStrategy {
    
    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".txt")) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    return line.startsWith("[") && line.endsWith("]");
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        String currentSection = null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }
                
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;
                
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                processKeyValue(currentSection, key, value, builder);
            }
        }
        
        return builder.build();
    }
    
    private void processKeyValue(String section, String key, String value, MissionBuilder builder) {
        if (section == null) return;
        
        switch (section.toUpperCase()) {
            case "MISSION":
                processMissionField(key, value, builder);
                break;
            case "CURSE":
                processCurseField(key, value, builder);
                break;
            case "SORCERER":
                processSorcererField(key, value, builder);
                break;
            case "TECHNIQUE":
                processTechniqueField(key, value, builder);
                break;
            case "ENVIRONMENT":
                processEnvironmentField(key, value, builder);
                break;
        }
    }
    
    private void processMissionField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "missionId":
                builder.setMissionId(value);
                break;
            case "date":
                builder.setDate(value);
                break;
            case "location":
                builder.setLocation(value);
                break;
            case "outcome":
                try {
                    builder.setOutcome(Outcome.valueOf(value));
                } catch (IllegalArgumentException e) {}
                break;
            case "damageCost":
                try {
                    builder.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {}
                break;
        }
    }
    
    private void processCurseField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "name":
                if (builder.build().getCurse() == null) {
                    builder.setCurse(value, null);
                } else {
                    builder.setCurse(value, builder.build().getCurse().getThreatLevel());
                }
                break;
            case "threatLevel":
                try {
                    ThreatLevel level = ThreatLevel.valueOf(value);
                    if (builder.build().getCurse() == null) {
                        builder.setCurse(null, level);
                    } else {
                        builder.setCurse(builder.build().getCurse().getName(), level);
                    }
                } catch (IllegalArgumentException e) {}
                break;
        }
    }
    
    private Sorcerer currentSorcerer = null;
    
    private void processSorcererField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "name":
                currentSorcerer = new Sorcerer();
                currentSorcerer.setName(value);
                break;
            case "rank":
                if (currentSorcerer != null) {
                    try {
                        currentSorcerer.setRank(Rank.valueOf(value));
                        builder.addSorcerer(currentSorcerer);
                        currentSorcerer = null;
                    } catch (IllegalArgumentException e) {}
                }
                break;
        }
    }
    
    private Technique currentTechnique = null;
    
    private void processTechniqueField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "name":
                currentTechnique = new Technique();
                currentTechnique.setName(value);
                break;
            case "type":
                if (currentTechnique != null) {
                    try {
                        currentTechnique.setType(TechniqueType.valueOf(value));
                    } catch (IllegalArgumentException e) {}
                }
                break;
            case "owner":
                if (currentTechnique != null) {
                    currentTechnique.setOwnerName(value);
                }
                break;
            case "damage":
                if (currentTechnique != null) {
                    try {
                        currentTechnique.setDamage(Long.parseLong(value));
                        builder.addTechnique(currentTechnique);
                        currentTechnique = null;
                    } catch (NumberFormatException e) {}
                }
                break;
        }
    }
    
    private void processEnvironmentField(String key, String value, MissionBuilder builder) {
        EnvironmentConditions conditions = builder.build().getEnvironmentConditions();
        if (conditions == null) {
            conditions = new EnvironmentConditions();
        }
        
        switch (key) {
            case "weather":
                try {
                    conditions.setWeather(Weather.valueOf(value));
                } catch (IllegalArgumentException e) {}
                break;
            case "timeOfDay":
                try {
                    conditions.setTimeOfDay(TimeOfDay.valueOf(value));
                } catch (IllegalArgumentException e) {}
                break;
            case "visibility":
                try {
                    conditions.setVisibility(Visibility.valueOf(value));
                } catch (IllegalArgumentException e) {}
                break;
            case "cursedEnergyDensity":
                try {
                    conditions.setCursedEnergyDensity(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
                break;
        }
        
        builder.setEnvironmentConditions(conditions);
    }
}
