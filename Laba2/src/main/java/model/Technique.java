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
public class Technique {
    private String name;
    private TechniqueType type;
    private Sorcerer owner;
    private long damage;
    
    private String ownerName;
    
    public Technique() {
    }
    
    public Technique(String name, TechniqueType type, Sorcerer owner, long damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TechniqueType getType() {
        return type;
    }
    
    public void setType(TechniqueType type) {
        this.type = type;
    }
    
    public Sorcerer getOwner() {
        return owner;
    }
    
    public void setOwner(Sorcerer owner) {
        this.owner = owner;
    }
    
    public long getDamage() {
        return damage;
    }
    
    public void setDamage(long damage) {
        this.damage = damage;
    }

    public String getOwnerName() { 
        return ownerName; 
    }
    
    public void setOwnerName(String ownerName) { 
        this.ownerName = ownerName; 
    }
}
