/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import model.builder.MissionBuilder;
import model.enums.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;

/**
 * Стратегия для JSON файлов (поддерживает новые блоки)
 * @author zubbo
 */
public class JsonParserStrategy implements ParserStrategy {
    private final ObjectMapper mapper;
    
    public JsonParserStrategy() {
        this.mapper = new ObjectMapper();
    }
    
    @Override
    public boolean supports(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        JsonNode root = mapper.readTree(file);
        
        builder.setMissionId(getString(root, "missionId"))
               .setDate(getString(root, "date"))
               .setLocation(getString(root, "location"))
               .setDamageCost(getLong(root, "damageCost"));
        
        setOutcome(root, builder);
        setComment(root, builder);
        
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
        
        if (root.has("enemyActivity")) {
            JsonNode ea = root.get("enemyActivity");
            EnemyActivity activity = new EnemyActivity();
            activity.setBehaviorType(getBehaviorType(ea, "behaviorType"));
            activity.setTargetPriority(getString(ea, "targetPriority"));
            activity.setMobility(getMobility(ea, "mobility"));
            activity.setEscalationRisk(getEscalationRisk(ea, "escalationRisk"));
            
            if (ea.has("attackPatterns") && ea.get("attackPatterns").isArray()) {
                for (JsonNode pattern : ea.get("attackPatterns")) {
                    activity.addAttackPattern(pattern.asText());
                }
            }
            builder.setEnemyActivity(activity);
        }
        
        if (root.has("environmentConditions")) {
            JsonNode ec = root.get("environmentConditions");
            EnvironmentConditions conditions = new EnvironmentConditions();
            conditions.setWeather(getWeather(ec, "weather"));
            conditions.setTimeOfDay(getTimeOfDay(ec, "timeOfDay"));
            conditions.setVisibility(getVisibility(ec, "visibility"));
            conditions.setCursedEnergyDensity(getInt(ec, "cursedEnergyDensity"));
            builder.setEnvironmentConditions(conditions);
        }
        
        if (root.has("civilianImpact")) {
            JsonNode ci = root.get("civilianImpact");
            CivilianImpact impact = new CivilianImpact();
            impact.setEvacuated(getInt(ci, "evacuated"));
            impact.setInjured(getInt(ci, "injured"));
            impact.setMissing(getInt(ci, "missing"));
            impact.setPublicExposureRisk(getPublicExposureRisk(ci, "publicExposureRisk"));
            builder.setCivilianImpact(impact);
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
    
    private void setComment(JsonNode node, MissionBuilder builder) {
        String comment = getString(node, "comment");
        if (comment == null) {
            comment = getString(node, "note");
        }
        if (comment != null) {
            builder.setComment(comment);
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
    
    private BehaviorType getBehaviorType(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return BehaviorType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Mobility getMobility(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return Mobility.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private EscalationRisk getEscalationRisk(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return EscalationRisk.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Weather getWeather(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return Weather.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TimeOfDay getTimeOfDay(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return TimeOfDay.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Visibility getVisibility(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return Visibility.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private PublicExposureRisk getPublicExposureRisk(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        try {
            return PublicExposureRisk.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
