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
        
        // Дополнительная проверка: пытаемся прочитать корневой элемент
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
            try {
                builder.setOutcome(Outcome.valueOf(outcomeStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown outcome: " + outcomeStr);
            }
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
            ThreatLevel threatLevel = parseThreatLevel(getString(curseNode, "threatLevel"));
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
            activity.setBehaviorType(parseBehaviorType(getString(ea, "behaviorType")));
            activity.setTargetPriority(getString(ea, "targetPriority"));
            activity.setMobility(parseMobility(getString(ea, "mobility")));
            activity.setEscalationRisk(parseEscalationRisk(getString(ea, "escalationRisk")));
            
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
            conditions.setWeather(parseWeather(getString(ec, "weather")));
            conditions.setTimeOfDay(parseTimeOfDay(getString(ec, "timeOfDay")));
            conditions.setVisibility(parseVisibility(getString(ec, "visibility")));
            conditions.setCursedEnergyDensity(getInt(ec, "cursedEnergyDensity"));
            builder.setEnvironmentConditions(conditions);
        }
        
        if (root.has("civilianImpact") && root.get("civilianImpact") != null) {
            JsonNode ci = root.get("civilianImpact");
            CivilianImpact impact = new CivilianImpact();
            impact.setEvacuated(getInt(ci, "evacuated"));
            impact.setInjured(getInt(ci, "injured"));
            impact.setMissing(getInt(ci, "missing"));
            impact.setPublicExposureRisk(parsePublicExposureRisk(getString(ci, "publicExposureRisk")));
            builder.setCivilianImpact(impact);
        }
        
        if (root.has("operationTimeline") && root.get("operationTimeline") != null) {
            JsonNode timelineNode = root.get("operationTimeline");
            if (timelineNode.has("event")) {
                JsonNode eventNode = timelineNode.get("event");
                if (eventNode.isArray()) {
                    for (JsonNode e : eventNode) {
                        addTimelineEventFromXml(e, builder);
                    }
                } else {
                    addTimelineEventFromXml(eventNode, builder);
                }
            }
        }
        
        if (root.has("missionTags") && root.get("missionTags") != null) {
            JsonNode tagsNode = root.get("missionTags");
            if (tagsNode.has("tag")) {
                JsonNode tagNode = tagsNode.get("tag");
                if (tagNode.isArray()) {
                    for (JsonNode t : tagNode) {
                        String tag = t.asText();
                        if (tag != null && !tag.isEmpty()) {
                            builder.addMissionTag(tag);
                        }
                    }
                } else {
                    String tag = tagNode.asText();
                    if (tag != null && !tag.isEmpty()) {
                        builder.addMissionTag(tag);
                    }
                }
            }
        }
        
        if (root.has("supportUnits") && root.get("supportUnits") != null) {
            JsonNode unitsNode = root.get("supportUnits");
            if (unitsNode.has("unit")) {
                JsonNode unitNode = unitsNode.get("unit");
                if (unitNode.isArray()) {
                    for (JsonNode u : unitNode) {
                        String unit = u.asText();
                        if (unit != null && !unit.isEmpty()) {
                            builder.addSupportUnit(unit);
                        }
                    }
                } else {
                    String unit = unitNode.asText();
                    if (unit != null && !unit.isEmpty()) {
                        builder.addSupportUnit(unit);
                    }
                }
            }
        }
        
        if (root.has("artifactsRecovered") && root.get("artifactsRecovered") != null) {
            JsonNode artifactsNode = root.get("artifactsRecovered");
            if (artifactsNode.has("artifact")) {
                JsonNode artifactNode = artifactsNode.get("artifact");
                if (artifactNode.isArray()) {
                    for (JsonNode a : artifactNode) {
                        String artifact = a.asText();
                        if (artifact != null && !artifact.isEmpty()) {
                            builder.addArtifact(artifact);
                        }
                    }
                } else {
                    String artifact = artifactNode.asText();
                    if (artifact != null && !artifact.isEmpty()) {
                        builder.addArtifact(artifact);
                    }
                }
            }
        }
        
        return builder.build();
    }
    
    private void addSorcererFromXml(JsonNode node, MissionBuilder builder) {
        String name = getString(node, "name");
        Rank rank = parseRank(getString(node, "rank"));
        if (name != null && !name.isEmpty()) {
            builder.addSorcerer(name, rank);
        }
    }
    
    private void addTechniqueFromXml(JsonNode node, MissionBuilder builder) {
        String name = getString(node, "name");
        TechniqueType type = parseTechniqueType(getString(node, "type"));
        String owner = getString(node, "owner");
        long damage = getLong(node, "damage");
        
        if (name != null && !name.isEmpty()) {
            builder.addTechnique(name, type, owner, damage);
        }
    }
    
    private void addTimelineEventFromXml(JsonNode node, MissionBuilder builder) {
        String timestampStr = getString(node, "timestamp");
        String typeStr = getString(node, "type");
        String description = getString(node, "description");
        
        if (description != null && !description.isEmpty()) {
            OperationTimeline event = new OperationTimeline();
            
            if (timestampStr != null && !timestampStr.isEmpty()) {
                try {
                    event.setTimestamp(java.time.LocalDateTime.parse(timestampStr));
                } catch (Exception e) {
                }
            }
            
            event.setType(parseTimelineEventType(typeStr));
            event.setDescription(description);
            builder.addTimelineEvent(event);
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
    
    private ThreatLevel parseThreatLevel(String value) {
        if (value == null) return null;
        try {
            return ThreatLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Rank parseRank(String value) {
        if (value == null) return null;
        try {
            return Rank.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TechniqueType parseTechniqueType(String value) {
        if (value == null) return null;
        try {
            return TechniqueType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private BehaviorType parseBehaviorType(String value) {
        if (value == null) return null;
        try {
            return BehaviorType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Mobility parseMobility(String value) {
        if (value == null) return null;
        try {
            return Mobility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private EscalationRisk parseEscalationRisk(String value) {
        if (value == null) return null;
        try {
            return EscalationRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Weather parseWeather(String value) {
        if (value == null) return null;
        try {
            return Weather.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TimeOfDay parseTimeOfDay(String value) {
        if (value == null) return null;
        try {
            return TimeOfDay.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Visibility parseVisibility(String value) {
        if (value == null) return null;
        try {
            return Visibility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private PublicExposureRisk parsePublicExposureRisk(String value) {
        if (value == null) return null;
        try {
            return PublicExposureRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private TimelineEventType parseTimelineEventType(String value) {
        if (value == null) return null;
        try {
            return TimelineEventType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}