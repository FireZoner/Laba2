/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import model.*;
import builders.MissionBuilder;
import model.enums.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.*;

/**
 *
 * @author zubbo
 */
public class XmlParserStrategy implements ParserStrategy {
    
    private final XmlMapper xmlMapper;
    
    public XmlParserStrategy() {
        this.xmlMapper = new XmlMapper();
    }
    
    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".xml")) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            return firstLine != null && firstLine.contains("<mission");
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public Mission parse(File file, MissionBuilder builder) throws IOException {
        JsonNode root = xmlMapper.readTree(file);
        
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
        
        if (root.has("sorcerers")) {
            JsonNode sorcerersNode = root.get("sorcerers");
            if (sorcerersNode.has("sorcerer")) {
                JsonNode sorcererNode = sorcerersNode.get("sorcerer");
                if (sorcererNode.isArray()) {
                    for (JsonNode sNode : sorcererNode) {
                        addSorcererFromXml(sNode, builder);
                    }
                } else {
                    addSorcererFromXml(sorcererNode, builder);
                }
            }
        }
        
        if (root.has("techniques")) {
            JsonNode techniquesNode = root.get("techniques");
            if (techniquesNode.has("technique")) {
                JsonNode techniqueNode = techniquesNode.get("technique");
                if (techniqueNode.isArray()) {
                    for (JsonNode tNode : techniqueNode) {
                        addTechniqueFromXml(tNode, builder);
                    }
                } else {
                    addTechniqueFromXml(techniqueNode, builder);
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
            
            if (ea.has("attackPatterns")) {
                JsonNode patternsNode = ea.get("attackPatterns");
                if (patternsNode.has("pattern")) {
                    JsonNode patternNode = patternsNode.get("pattern");
                    if (patternNode.isArray()) {
                        for (JsonNode p : patternNode) {
                            String pattern = p.asText();
                            if (pattern != null && !pattern.isEmpty()) {
                                activity.addAttackPattern(pattern);
                            }
                        }
                    } else {
                        String pattern = patternNode.asText();
                        if (pattern != null && !pattern.isEmpty()) {
                            activity.addAttackPattern(pattern);
                        }
                    }
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
    
    private void addSorcererFromXml(JsonNode node, MissionBuilder builder) {
        String name = getString(node, "name");
        Rank rank = Rank.parse(getString(node, "rank"));
        if (name != null && !name.isEmpty()) {
            builder.addSorcerer(name, rank);
        }
    }
    
    private void addTechniqueFromXml(JsonNode node, MissionBuilder builder) {
        String name = getString(node, "name");
        TechniqueType type = TechniqueType.parse(getString(node, "type"));
        String owner = getString(node, "owner");
        long damage = getLong(node, "damage");
        
        if (name != null && !name.isEmpty()) {
            builder.addTechnique(name, type, owner, damage);
        }
    }
    
    private String getString(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asText() : null;
    }
    
    private long getLong(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value != null && !value.isNull() && value.isNumber()) {
            return value.asLong();
        }
        if (value != null && !value.isNull() && value.isTextual()) {
            try {
                return Long.parseLong(value.asText());
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }
    
    private int getInt(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value != null && !value.isNull() && value.isNumber()) {
            return value.asInt();
        }
        if (value != null && !value.isNull() && value.isTextual()) {
            try {
                return Integer.parseInt(value.asText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    private boolean getBoolean(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() && value.asBoolean();
    }
}