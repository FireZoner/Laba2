/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.enums.*;

/**
 *
 * @author zubbo
 */
public class Curse {
    private String name;
    private ThreatLevel threatLevel;
    
    public Curse(String name, ThreatLevel threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }
    
    public Curse() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ThreatLevel getThreatLevel() {
        return this.threatLevel;
    }
    
    public void setThreatLevel(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel;
    }
}
