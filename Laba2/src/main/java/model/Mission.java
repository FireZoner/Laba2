/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.enums.*;
import java.util.*;

/**
 *
 * @author zubbo
 */
public class Mission {
    private String missionId;
    private String date;
    private String location;
    private Outcome outcome;
    private long damageCost;
    private String comment;
    
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;
    
    private EconomicAssessment economicAssessment;
    private EnemyActivity enemyActivity;
    private EnvironmentConditions environmentConditions;
    private CivilianImpact civilianImpact;
    
    private List<OperationTimeline> operationTimeline;
    private List<String> missionTags;
    private List<String> supportUnits;
    private List<String> recommendations;
    private List<String> artifactsRecovered;
    private List<String> evacuationZones;
    private List<String> statusEffects;
    
    public Mission() {
        this.sorcerers = new ArrayList<>();
        this.techniques = new ArrayList<>();
        this.operationTimeline = new ArrayList<>();
        this.missionTags = new ArrayList<>();
        this.supportUnits = new ArrayList<>();
        this.recommendations = new ArrayList<>();
        this.artifactsRecovered = new ArrayList<>();
        this.evacuationZones = new ArrayList<>();
        this.statusEffects = new ArrayList<>();
    }
    
    public String getMissionId() { 
        return missionId; 
    }
    
    public void setMissionId(String missionId) { 
        this.missionId = missionId; 
    }
    
    public String getDate() { 
        return date; 
    }
    
    public void setDate(String date) { 
        this.date = date; 
    }
    
    public String getLocation() { 
        return location; 
    }
    
    public void setLocation(String location) { 
        this.location = location; 
    }
    
    public Outcome getOutcome() { 
        return outcome; 
    }
    
    public void setOutcome(Outcome outcome) { 
        this.outcome = outcome; 
    }
    
    public long getDamageCost() { 
        return damageCost; 
    }
    
    public void setDamageCost(long damageCost) { 
        this.damageCost = damageCost; 
    }
    
    public String getComment() { 
        return comment; 
    }
    
    public void setComment(String comment) { 
        this.comment = comment; 
    }
    
    public Curse getCurse() { 
        return curse;
    }
    
    public void setCurse(Curse curse) { 
        this.curse = curse; 
    }
    
    public List<Sorcerer> getSorcerers() { 
        return sorcerers; 
    }
    
    public void addSorcerer(Sorcerer sorcerer) { 
        this.sorcerers.add(sorcerer); 
    }
    
    public List<Technique> getTechniques() { 
        return techniques; 
    }
    
    public void addTechnique(Technique technique) { 
        this.techniques.add(technique); 
    }
    
    public EconomicAssessment getEconomicAssessment() { 
        return economicAssessment; 
    }
    
    public void setEconomicAssessment(EconomicAssessment economicAssessment) { 
        this.economicAssessment = economicAssessment; 
    }
    
    public EnemyActivity getEnemyActivity() { 
        return enemyActivity; 
    }
    
    public void setEnemyActivity(EnemyActivity enemyActivity) { 
        this.enemyActivity = enemyActivity; 
    }
    
    public EnvironmentConditions getEnvironmentConditions() { 
        return environmentConditions; 
    }
    
    public void setEnvironmentConditions(EnvironmentConditions environmentConditions) { 
        this.environmentConditions = environmentConditions; 
    }
    
    public CivilianImpact getCivilianImpact() { 
        return civilianImpact; 
    }
    
    public void setCivilianImpact(CivilianImpact civilianImpact) { 
        this.civilianImpact = civilianImpact; 
    }
    
    public List<OperationTimeline> getOperationTimeline() { 
        return operationTimeline; 
    }
    
    public void setOperationTimeline(List<OperationTimeline> operationTimeline) {
        this.operationTimeline = operationTimeline;
    }
    public void addTimelineEvent(OperationTimeline event) {
        this.operationTimeline.add(event);
    }
    
    public List<String> getMissionTags() { 
        return missionTags; 
    }
    
    public void addMissionTag(String tag) { 
        this.missionTags.add(tag); 
    }
    
    public List<String> getSupportUnits() { 
        return supportUnits; 
    }
    
    public void addSupportUnit(String unit) { 
        this.supportUnits.add(unit); 
    }
    
    public List<String> getRecommendations() { 
        return recommendations; 
    }
    
    public void addRecommendation(String rec) { 
        this.recommendations.add(rec); 
    }
    
    public List<String> getArtifactsRecovered() { 
        return artifactsRecovered; 
    }
    
    public void addArtifact(String artifact) { 
        this.artifactsRecovered.add(artifact); 
    }
    
    public List<String> getEvacuationZones() { 
        return evacuationZones; 
    }
    
    public void addEvacuationZone(String zone) { 
        this.evacuationZones.add(zone); 
    }
    
    public List<String> getStatusEffects() { 
        return statusEffects; 
    }
    
    public void addStatusEffect(String effect) {
        this.statusEffects.add(effect); 
    }
}