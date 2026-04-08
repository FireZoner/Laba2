/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.enums.*;
import java.util.*;

/**
 * Активность противника
 * @author zubbo
 */
public class EnemyActivity {
    private BehaviorType behaviorType;
    private String targetPriority;
    private List<String> attackPatterns;
    private Mobility mobility;
    private EscalationRisk escalationRisk;
    
    public EnemyActivity() {
        this.attackPatterns = new ArrayList<>();
    }
    
    // Getters and Setters
    public BehaviorType getBehaviorType() { 
        return behaviorType; 
    }
    
    public void setBehaviorType(BehaviorType behaviorType) { 
        this.behaviorType = behaviorType; 
    }
    
    public String getTargetPriority() { 
        return targetPriority; 
    }
    
    public void setTargetPriority(String targetPriority) { 
        this.targetPriority = targetPriority; 
    }
    
    public List<String> getAttackPatterns() { 
        return attackPatterns; 
    }
    
    public void addAttackPattern(String pattern) { 
        this.attackPatterns.add(pattern); 
    }
    
    public Mobility getMobility() { 
        return mobility; 
    }
    public void setMobility(Mobility mobility) { 
        this.mobility = mobility; 
    }
    
    public EscalationRisk getEscalationRisk() { 
        return escalationRisk; 
    }
    
    public void setEscalationRisk(EscalationRisk escalationRisk) { 
        this.escalationRisk = escalationRisk; 
    }
}
