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
        
        String outcomeStr = getString(root, "outcome");
        if (outcomeStr != null) {
            builder.setOutcome(Outcome.parse(outcomeStr));
        }
        
        if (root.has("curse") && root.get("curse") != null) {
            JsonNode curseNode = root.get("curse");
            String curseName = getString(curseNode, "name");
            ThreatLevel threatLevel = ThreatLevel.parse(getString(curseNode, "threatLevel"));
            builder.setCurse(curseName, threatLevel);
        }
        
        if (root.has("sorcerers") && root.get("sorcerers").isArray()) {
            for (JsonNode sNode : root.get("sorcerers")) {
                String name = getString(sNode, "name");
                Rank rank = Rank.parse(getString(sNode, "rank"));
                if (name != null && !name.isEmpty()) {
                    builder.addSorcerer(name, rank);
                }
            }
        }
        
        if (root.has("techniques") && root.get("techniques").isArray()) {
            for (JsonNode tNode : root.get("techniques")) {
                String techName = getString(tNode, "name");
                TechniqueType techType = TechniqueType.parse(getString(tNode, "type"));
                String ownerName = getString(tNode, "owner");
                long damage = getLong(tNode, "damage");
                
                if (techName != null && !techName.isEmpty()) {
                    builder.addTechnique(techName, techType, ownerName, damage);
                }
            }
        }
        
        if (root.has("economicAssessment") && root.get("economicAssessment") != null) {
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
}