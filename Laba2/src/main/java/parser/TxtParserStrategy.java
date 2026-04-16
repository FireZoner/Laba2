/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import model.*;
import builders.MissionBuilder;
import model.enums.*;
import java.io.*;

/**
 * Стратегия для секционного TXT формата
 * Формат: [SECTION] key=value
 * @author zubbo
 */
public class TxtParserStrategy implements ParserStrategy {
    
    private Sorcerer currentSorcerer = null;
    private Technique currentTechnique = null;
    
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
        currentSorcerer = null;
        currentTechnique = null;
        
        String currentSection = null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("[") && line.endsWith("]")) {
                    finalizeCurrentSection(builder);
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }
                
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;
                
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                processKeyValue(currentSection, key, value, builder);
            }
            
            finalizeCurrentSection(builder);
        }
        
        return builder.build();
    }
    
    private void finalizeCurrentSection(MissionBuilder builder) {
        if (currentSorcerer != null) {
            if (currentSorcerer.getName() != null && !currentSorcerer.getName().isEmpty()) {
                builder.addSorcerer(currentSorcerer);
            }
            currentSorcerer = null;
        }
        
        if (currentTechnique != null) {
            if (currentTechnique.getName() != null && !currentTechnique.getName().isEmpty()) {
                builder.addTechnique(currentTechnique);
            }
            currentTechnique = null;
        }
    }
    
    private void processKeyValue(String section, String key, String value, MissionBuilder builder) {
        if (section == null) return;
        
        switch (section.toUpperCase()) {
            case "MISSION" -> processMissionField(key, value, builder);
            case "CURSE" -> processCurseField(key, value, builder);
            case "SORCERER" -> processSorcererField(key, value, builder);
            case "TECHNIQUE" -> processTechniqueField(key, value, builder);
            case "ENVIRONMENT" -> processEnvironmentField(key, value, builder);
        }
    }
    
    private void processMissionField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "missionId" -> builder.setMissionId(value);
            case "date" -> builder.setDate(value);
            case "location" -> builder.setLocation(value);
            case "outcome" -> builder.setOutcome(Outcome.parse(value));
            case "damageCost" -> {
                try {
                    builder.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {}
            }
        }
    }
    
    private void processCurseField(String key, String value, MissionBuilder builder) {
        switch (key) {
            case "name" -> {
                if (builder.build().getCurse() == null) {
                    builder.setCurse(value, null);
                } else {
                    builder.setCurse(value, builder.build().getCurse().getThreatLevel());
                }
            }
            case "threatLevel" -> {
                ThreatLevel level = ThreatLevel.parse(value);
                if (builder.build().getCurse() == null) {
                    builder.setCurse(null, level);
                } else {
                    builder.setCurse(builder.build().getCurse().getName(), level);
                }
            }
        }
    }
    
    private void processSorcererField(String key, String value, MissionBuilder builder) {
        if (currentSorcerer == null) {
            currentSorcerer = new Sorcerer();
        }
        
        switch (key) {
            case "name" -> currentSorcerer.setName(value);
            case "rank" -> {
                currentSorcerer.setRank(Rank.parse(value));
                if (currentSorcerer.getName() != null) {
                    builder.addSorcerer(currentSorcerer);
                    currentSorcerer = null;
                }
            }
        }
    }
    
    private void processTechniqueField(String key, String value, MissionBuilder builder) {
        if (currentTechnique == null) {
            currentTechnique = new Technique();
        }
        
        switch (key) {
            case "name" -> currentTechnique.setName(value);
            case "type" -> currentTechnique.setType(TechniqueType.parse(value));
            case "owner" -> currentTechnique.setOwnerName(value);
            case "damage" -> {
                try {
                    currentTechnique.setDamage(Long.parseLong(value));
                    if (currentTechnique.getName() != null) {
                        builder.addTechnique(currentTechnique);
                        currentTechnique = null;
                    }
                } catch (NumberFormatException e) {}
            }
        }
    }
    
    private void processEnvironmentField(String key, String value, MissionBuilder builder) {
        EnvironmentConditions conditions = builder.build().getEnvironmentConditions();
        if (conditions == null) {
            conditions = new EnvironmentConditions();
        }
        
        switch (key) {
            case "weather" -> conditions.setWeather(Weather.parse(value));
            case "timeOfDay" -> conditions.setTimeOfDay(TimeOfDay.parse(value));
            case "visibility" -> conditions.setVisibility(Visibility.parse(value));
            case "cursedEnergyDensity" -> {
                try {
                    conditions.setCursedEnergyDensity(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
                break;
            }
        }
        
        builder.setEnvironmentConditions(conditions);
    }
}
