/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import com.fasterxml.jackson.databind.JsonNode;
import model.*;
import model.enums.*;

/**
 *
 * @author zubbo
 */
public abstract class BaseParser implements MissionParser {
    
    protected String getString(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : null;
    }

    private long getLong(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asLong() : 0;
    }

    private Outcome getOutcome(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return Outcome.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный Outcome: " + value);
            return null;
        }
    }
    
    protected ThreatLevel getThreatLevel(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return ThreatLevel.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный ThreatLevel: " + value);
            return null;
        }
    }
    
    private Rank getRank(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return Rank.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный Rank: " + value);
            return null;
        }
    }
    
    private TechniqueType getTechniqueType(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return TechniqueType.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный TechniqueType: " + value);
            return null;
        }
    }
    
    private Sorcerer findSorcererByName(Mission mission, String name) {
        if (name == null) return null;
        
        for (Sorcerer s : mission.getSorcerers()) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }
    
    protected void parseSorcerer(JsonNode node, Mission mission) {
        Sorcerer sorcerer = new Sorcerer();
        sorcerer.setName(getString(node, "name"));
        sorcerer.setRank(getRank(node, "rank"));
        mission.addSorcerer(sorcerer);
    }
    
    protected void parseTechnique(JsonNode node, Mission mission) {
        Technique technique = new Technique();
        technique.setName(getString(node, "name"));
        technique.setType(getTechniqueType(node, "type"));
        technique.setDamage(getLong(node, "damage"));
        
        String ownerName = getString(node, "owner");
        if (ownerName != null) {
            Sorcerer owner = findSorcererByName(mission, ownerName);
            if (owner != null) {
                technique.setOwner(owner);
            } else {
                technique.setOwner(new Sorcerer(ownerName, null));
            }
        }
        
        mission.addTechnique(technique);
    }
    
    protected Mission parseMission(JsonNode root) {
        Mission mission = new Mission();
        
        mission.setMissionId(getString(root, "missionId"));
        mission.setDate(getString(root, "date"));
        mission.setLocation(getString(root, "location"));
        mission.setOutcome(getOutcome(root, "outcome"));
        mission.setDamageCost(getLong(root, "damageCost"));
        
        String comment = getString(root, "comment");
        if (comment == null) {
            comment = getString(root, "note");
        }
        mission.setComment(comment);
        
        if (root.has("curse")) {
            JsonNode curseNode = root.get("curse");
            Curse curse = new Curse();
            curse.setName(getString(curseNode, "name"));
            curse.setThreatLevel(getThreatLevel(curseNode, "threatLevel"));
            mission.setCurse(curse);
        }
        
        return mission;
    }
}
