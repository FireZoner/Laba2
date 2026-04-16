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
        
        String outcomeStr = getString(root, "outcome");
        if (outcomeStr != null) {
            builder.setOutcome(Outcome.parse(outcomeStr));
        }
        
        String comment = getString(root, "comment");
        if (comment == null) {
            comment = getString(root, "note");
        }
        if (comment != null) {
            builder.setComment(comment);
        }
        
        if (root.has("curse") && root.get("curse") != null && !root.get("curse").isNull()) {
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

        if (root.has("enemyActivity") && root.get("enemyActivity") != null) {
            JsonNode ea = root.get("enemyActivity");
            EnemyActivity activity = new EnemyActivity();
            activity.setBehaviorType(BehaviorType.parse(getString(ea, "behaviorType")));
            activity.setTargetPriority(getString(ea, "targetPriority"));
            activity.setMobility(Mobility.parse(getString(ea, "mobility")));
            activity.setEscalationRisk(EscalationRisk.parse(getString(ea, "escalationRisk")));
            
            if (ea.has("attackPatterns") && ea.get("attackPatterns").isArray()) {
                for (JsonNode pattern : ea.get("attackPatterns")) {
                    activity.addAttackPattern(pattern.asText());
                }
            }
            builder.setEnemyActivity(activity);
        }
        
        if (root.has("environmentConditions") && root.get("environmentConditions") != null) {
            JsonNode ec = root.get("environmentConditions");
            EnvironmentConditions conditions = new EnvironmentConditions();
            conditions.setWeather(Weather.parse(getString(ec, "weather")));
            conditions.setTimeOfDay(TimeOfDay.parse(getString(ec, "timeOfDay")));
            conditions.setVisibility(Visibility.parse(getString(ec, "visibility")));
            conditions.setCursedEnergyDensity(getInt(ec, "cursedEnergyDensity"));
            builder.setEnvironmentConditions(conditions);
        }
        
        if (root.has("civilianImpact") && root.get("civilianImpact") != null) {
            JsonNode ci = root.get("civilianImpact");
            CivilianImpact impact = new CivilianImpact();
            impact.setEvacuated(getInt(ci, "evacuated"));
            impact.setInjured(getInt(ci, "injured"));
            impact.setMissing(getInt(ci, "missing"));
            impact.setPublicExposureRisk(PublicExposureRisk.parse(getString(ci, "publicExposureRisk")));
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
}