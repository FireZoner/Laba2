/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import model.enums.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author zubbo
 */
public class TextParser implements MissionParser {
    
    @Override
    public Mission parse(File file) throws IOException {
        Mission mission = new Mission();
        Map<Integer, Sorcerer> sorcererMap = new HashMap<>();
        Map<Integer, Technique> techniqueMap = new HashMap<>();
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while((line = reader.readLine()) != null) {             
                if(line.trim().isEmpty()) continue;
                
                String[] parts = line.split(":", 2);
                if (parts.length < 2) {
                    throw new IOException("Некорректная строка в файле: " + line);
                }

                String key = parts[0].trim();
                String value = parts[1].trim();
                
                keyProcessing(key, value, mission, sorcererMap, techniqueMap);
            }
        }
        for (Sorcerer s : sorcererMap.values()) {
            mission.addSorcerer(s);
        }
        
        for (Technique t : techniqueMap.values()) {
            mission.addTechnique(t);
        }
        
        return mission;
    }
    
    private void keyProcessing(String key, String value, Mission mission, Map<Integer, Sorcerer> sorcererMap,
                            Map<Integer, Technique> techniqueMap) {
        switch (key) {
            case "missionId":
                mission.setMissionId(value);
                break;
            case "date":
                mission.setDate(value);
                break;
            case "location":
                mission.setLocation(value);
                break;
            case "outcome":
                try {
                    mission.setOutcome(Outcome.valueOf(value));
                } catch (IllegalArgumentException e) {
                    System.out.println("Неизвестный outcome: " + value);
                }
                break;
            case "damageCost":
                try {
                    mission.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка парсинга damageCost: " + value);
                }
                break;
            case "note":
                mission.setComment(value);
            case "comment":
                mission.setComment(value);
            default:
                complexKeyProcessing(key, value, mission, sorcererMap, techniqueMap);
        }
    }
    
    private void complexKeyProcessing(String key, String value, Mission mission, Map<Integer, Sorcerer> sorcererMap,
                                   Map<Integer, Technique> techniqueMap) {
        if(key.startsWith("curse")) curseKeyProcessing(key.substring(6), value, mission);
        if(key.startsWith("sorcerer[")) sorcererKeyProcessing(key, value, sorcererMap);
        if(key.startsWith("technique[")) techniqueKeyProcessing(key, value, techniqueMap);
    }
    
    private void curseKeyProcessing(String curseKey, String curseValue, Mission mission) {
        Curse curse = mission.getCurse();
        if (curse == null) {
            curse = new Curse();
            mission.setCurse(curse);
        }
        
        switch (curseKey) {
            case "name":
                curse.setName(curseValue);
                break;
            case "threatLevel":
                try {
                    curse.setThreatLevel(ThreatLevel.valueOf(curseValue));
                } catch (IllegalArgumentException e) {
                    System.out.println("Неизвестный threatLevel: " + curseValue);
                }
                break;
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
    
    private void sorcererKeyProcessing(String sorcererKey, String sorcererValue, Map<Integer, Sorcerer> sorcererMap) {
        int index = extractIndex(sorcererKey);
        if (index == -1) return;
        
        Sorcerer sorcerer = sorcererMap.get(index);
        if (sorcerer == null) {
            sorcerer = new Sorcerer();
            sorcererMap.put(index, sorcerer);
        }
        
        if (sorcererKey.endsWith(".name")) {
            sorcerer.setName(sorcererValue);
        } else if (sorcererKey.endsWith(".rank")) {
            try {
                sorcerer.setRank(Rank.valueOf(sorcererValue));
            } catch (IllegalArgumentException e) {
                System.out.println("Неизвестный rank: " + sorcererValue);
            }
        }
    }
    
    private void techniqueKeyProcessing(String techniqueKey, String techniqueValue, Map<Integer, Technique> techniqueMap) {
        int index = extractIndex(techniqueKey);
        if (index == -1) return;
        
        Technique technique = techniqueMap.get(index);
        if (technique == null) {
            technique = new Technique();
            techniqueMap.put(index, technique);
        }
        
        if (techniqueKey.endsWith(".name")) {
            technique.setName(techniqueValue);
        } else if (techniqueKey.endsWith(".type")) {
            try {
                technique.setType(TechniqueType.valueOf(techniqueValue));
            } catch (IllegalArgumentException e) {
                System.out.println("Неизвестный technique type: " + techniqueValue);
            }
        } else if (techniqueKey.endsWith(".owner")) {
            technique.setOwner(new Sorcerer(techniqueValue, null));
        } else if (techniqueKey.endsWith(".damage")) {
            try {
                technique.setDamage(Long.parseLong(techniqueValue));
            } catch (NumberFormatException e) {
                System.out.println("Ошибка парсинга damage: " + techniqueValue);
            }
        }
    }
}
