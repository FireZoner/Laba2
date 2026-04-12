/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import builders.MissionBuilder;
import model.enums.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.*;

/**
 * Стратегия для YAML файлов
 * @author zubbo
 */
public class YamlParserStrategy implements ParserStrategy {
    private final ObjectMapper mapper;
    
    public YamlParserStrategy() {
        this.mapper = new ObjectMapper(new YAMLFactory());
    }
    
    @Override
    public boolean supports(File file) {
        return file.getName().toLowerCase().endsWith(".yaml") ||
               file.getName().toLowerCase().endsWith(".yml");
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        JsonNode root = mapper.readTree(file);
        
        builder.setMissionId(getString(root, "missionId"))
               .setDate(getString(root, "date"))
               .setLocation(getString(root, "location"))
               .setDamageCost(getLong(root, "damageCost"));
        
        setOutcome(root, builder);
        
        if (root.has("curse")) {
            JsonNode curseNode = root.get("curse");
            builder.setCurse(
                getString(curseNode, "name"),
                getThreatLevel(curseNode, "threatLevel")
            );
        }
        
        if (root.has("sorcerers") && root.get("sorcerers").isArray()) {
            for (JsonNode sNode : root.get("sorcerers")) {
                builder.addSorcerer(
                    getString(sNode, "name"),
                    getRank(sNode, "rank")
                );
            }
        }
        
        if (root.has("techniques") && root.get("techniques").isArray()) {
            for (JsonNode tNode : root.get("techniques")) {
                builder.addTechnique(
                    getString(tNode, "name"),
                    getTechniqueType(tNode, "type"),
                    getString(tNode, "owner"),
                    getLong(tNode, "damage")
                );
            }
        }
        
        if (root.has("economicAssessment")) {
            JsonNode ea = root.get("economicAssessment");
            EconomicAssessment assessment = new EconomicAssessment();
            assessment.setTotalDamageCost(getLong(ea, "totalDamageCost"));
            assessment.setInfrastructureDamage(getLong(ea, "infrastructureDamage"));
            assessment.setCommercialDamage(getLong(ea, "commercialDamage"));
            assessment.setTransportDamage(getLong(ea, "transportDamage"));
            assessment.setRecoveryEstimateDays(getInt(ea, "recoveryEstimateDays"));
            assessment.setInsuranceCovered(getBoolean(ea, "insuranceCovered"));
            builder.setEconomicAssessment(assessment);
        }
        
        return builder.build();
    }
    
    private String getString(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : null;
    }
    
    private long getLong(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asLong() : 0;
    }
    
    private int getInt(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asInt() : 0;
    }
    
    private boolean getBoolean(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() && value.asBoolean();
    }
    
    private void setOutcome(JsonNode node, MissionBuilder builder) {
        String value = getString(node, "outcome");
        if (value != null) {
            try {
                builder.setOutcome(Outcome.valueOf(value));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown outcome: " + value);
            }
        }
    }
    
    private ThreatLevel getThreatLevel(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return ThreatLevel.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Rank getRank(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return Rank.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TechniqueType getTechniqueType(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return TechniqueType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}