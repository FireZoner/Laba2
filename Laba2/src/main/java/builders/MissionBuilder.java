/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package builders;

import model.*;
import model.enums.*;

/**
 * Builder для создания Mission (Builder Pattern)
 * @author zubbo
 */
public class MissionBuilder {
    private final Mission mission;
    
    public MissionBuilder() {
        this.mission = new Mission();
    }
    
    public MissionBuilder setMissionId(String id) {
        mission.setMissionId(id);
        return this;
    }
    
    public MissionBuilder setDate(String date) {
        mission.setDate(date);
        return this;
    }
    
    public MissionBuilder setLocation(String location) {
        mission.setLocation(location);
        return this;
    }
    
    public MissionBuilder setOutcome(Outcome outcome) {
        mission.setOutcome(outcome);
        return this;
    }
    
    public MissionBuilder setDamageCost(long cost) {
        mission.setDamageCost(cost);
        return this;
    }
    
    public MissionBuilder setComment(String comment) {
        mission.setComment(comment);
        return this;
    }
    
    public MissionBuilder setCurse(Curse curse) {
        mission.setCurse(curse);
        return this;
    }
    
    public MissionBuilder setCurse(String name, ThreatLevel level) {
        mission.setCurse(new Curse(name, level));
        return this;
    }
    
    // Sorcerers
    public MissionBuilder addSorcerer(Sorcerer sorcerer) {
        mission.addSorcerer(sorcerer);
        return this;
    }
    
    public MissionBuilder addSorcerer(String name, Rank rank) {
        mission.addSorcerer(new Sorcerer(name, rank));
        return this;
    }
    
    public MissionBuilder addTechnique(Technique technique) {
        mission.addTechnique(technique);
        return this;
    }
    
    public MissionBuilder addTechnique(String name, TechniqueType type, String ownerName, long damage) {
        Sorcerer owner = findSorcererByName(ownerName);
        Technique technique = new Technique(name, type, owner, damage);
        mission.addTechnique(technique);
        return this;
    }
    
    public MissionBuilder setEconomicAssessment(EconomicAssessment assessment) {
        mission.setEconomicAssessment(assessment);
        return this;
    }
    
    public MissionBuilder setEnemyActivity(EnemyActivity activity) {
        mission.setEnemyActivity(activity);
        return this;
    }
    
    public MissionBuilder setEnvironmentConditions(EnvironmentConditions conditions) {
        mission.setEnvironmentConditions(conditions);
        return this;
    }
    
    public MissionBuilder setCivilianImpact(CivilianImpact impact) {
        mission.setCivilianImpact(impact);
        return this;
    }
    
    public MissionBuilder addTimelineEvent(OperationTimeline event) {
        mission.addTimelineEvent(event);
        return this;
    }
    
    public MissionBuilder addMissionTag(String tag) {
        mission.addMissionTag(tag);
        return this;
    }
    
    public MissionBuilder addSupportUnit(String unit) {
        mission.addSupportUnit(unit);
        return this;
    }
    
    public MissionBuilder addRecommendation(String rec) {
        mission.addRecommendation(rec);
        return this;
    }
    
    public MissionBuilder addArtifact(String artifact) {
        mission.addArtifact(artifact);
        return this;
    }
    
    public MissionBuilder addEvacuationZone(String zone) {
        mission.addEvacuationZone(zone);
        return this;
    }
    
    public MissionBuilder addStatusEffect(String effect) {
        mission.addStatusEffect(effect);
        return this;
    }
    
    private Sorcerer findSorcererByName(String name) {
        return mission.getSorcerers().stream()
            .filter(s -> s.getName() != null && s.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    public Mission build() {
        for (Technique t : mission.getTechniques()) {
            if (t.getOwner() == null && t.getOwnerName() != null) {
                Sorcerer owner = findSorcererByName(t.getOwnerName());
                if (owner != null) {
                    t.setOwner(owner);
                }
            }
        }
        return mission;
    }
}