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
    
    public Mission() {
        this.sorcerers = new ArrayList<>();
        this.techniques = new ArrayList<>();
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
    
    public void setSorcerers(List<Sorcerer> sorcerers) {
        this.sorcerers = sorcerers;
    }
    
    public void addSorcerer(Sorcerer sorcerer) {
        this.sorcerers.add(sorcerer);
    }
    
    public List<Technique> getTechniques() {
        return techniques;
    }
    
    public void setTechniques(List<Technique> techniques) {
        this.techniques = techniques;
    }
    
    public void addTechnique(Technique technique) {
        this.techniques.add(technique);
    }
}
