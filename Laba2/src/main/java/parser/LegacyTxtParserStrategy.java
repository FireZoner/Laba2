/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import builders.MissionBuilder;
import model.enums.*;
import java.io.*;
import java.util.*;

/**
 * @author zubbo
 */
public class LegacyTxtParserStrategy implements ParserStrategy {
    
    @Override
    public boolean supports(File file) {
        if (!file.getName().toLowerCase().endsWith(".txt")) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            if (firstLine == null) return false;
            firstLine = firstLine.trim();
            
            if (firstLine.startsWith("[") && firstLine.endsWith("]")) {
                return false;
            }
            
            if (firstLine.contains("|")) {
                return false;
            }
            
            return firstLine.contains(":");
            
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        Map<Integer, Sorcerer> sorcererMap = new HashMap<>();
        Map<Integer, Technique> techniqueMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(":", 2);
                if (parts.length < 2) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                processKey(key, value, builder, sorcererMap, techniqueMap);
            }
        }
        
        for (Sorcerer s : sorcererMap.values()) {
            if (s.getName() != null && !s.getName().isEmpty()) {
                builder.addSorcerer(s);
            }
        }
        
        for (Technique t : techniqueMap.values()) {
            if (t.getName() != null && !t.getName().isEmpty()) {
                builder.addTechnique(t);
            }
        }
        
        return builder.build();
    }
    
    private void processKey(String key, String value, MissionBuilder builder,
                           Map<Integer, Sorcerer> sorcererMap,
                           Map<Integer, Technique> techniqueMap) {
        
        switch (key) {
            case "missionId":
                builder.setMissionId(value);
                return;
            case "date":
                builder.setDate(value);
                return;
            case "location":
                builder.setLocation(value);
                return;
            case "outcome":
                try {
                    builder.setOutcome(Outcome.valueOf(value.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown outcome: " + value);
                }
                return;
            case "damageCost":
                try {
                    builder.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid damageCost: " + value);
                }
                return;
            case "note":
            case "comment":
                builder.setComment(value);
                return;
        }
        
        if (key.startsWith("curse.")) {
            processCurseKey(key.substring(6), value, builder);
            return;
        }
        
        if (key.startsWith("sorcerer[")) {
            processSorcererKey(key, value, sorcererMap);
            return;
        }
        
        if (key.startsWith("technique[")) {
            processTechniqueKey(key, value, techniqueMap);
        }
    }
    
    private void processCurseKey(String curseKey, String value, MissionBuilder builder) {
        String curseName = null;
        ThreatLevel threatLevel = null;
        
        Mission tempMission = builder.build();
        if (tempMission.getCurse() != null) {
            curseName = tempMission.getCurse().getName();
            threatLevel = tempMission.getCurse().getThreatLevel();
        }
        
        switch (curseKey) {
            case "name":
                curseName = value;
                break;
            case "threatLevel":
                try {
                    threatLevel = ThreatLevel.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown threatLevel: " + value);
                }
                break;
        }
        
        builder.setCurse(curseName, threatLevel);
    }
    
    private void processSorcererKey(String key, String value, Map<Integer, Sorcerer> sorcererMap) {
        int index = extractIndex(key);
        if (index == -1) return;
        
        Sorcerer sorcerer = sorcererMap.get(index);
        if (sorcerer == null) {
            sorcerer = new Sorcerer();
            sorcererMap.put(index, sorcerer);
        }
        
        if (key.endsWith(".name")) {
            sorcerer.setName(value);
        } else if (key.endsWith(".rank")) {
            try {
                sorcerer.setRank(Rank.valueOf(value.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown rank: " + value);
            }
        }
    }
    
    private void processTechniqueKey(String key, String value, Map<Integer, Technique> techniqueMap) {
        int index = extractIndex(key);
        if (index == -1) return;
        
        Technique technique = techniqueMap.get(index);
        if (technique == null) {
            technique = new Technique();
            techniqueMap.put(index, technique);
        }
        
        if (key.endsWith(".name")) {
            technique.setName(value);
        } else if (key.endsWith(".type")) {
            try {
                technique.setType(TechniqueType.valueOf(value.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown technique type: " + value);
            }
        } else if (key.endsWith(".owner")) {
            technique.setOwnerName(value);
        } else if (key.endsWith(".damage")) {
            try {
                technique.setDamage(Long.parseLong(value));
            } catch (NumberFormatException e) {
                System.out.println("Invalid damage: " + value);
            }
        }
    }
    
    private int extractIndex(String key) {
        int start = key.indexOf('[');
        int end = key.indexOf(']');
        if (start == -1 || end == -1 || start >= end) {
            return -1;
        }
        
        try {
            return Integer.parseInt(key.substring(start + 1, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}