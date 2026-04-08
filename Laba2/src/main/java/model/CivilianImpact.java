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
public class CivilianImpact {
    private int evacuated;
    private int injured;
    private int missing;
    private PublicExposureRisk publicExposureRisk;
    
    public CivilianImpact() {}
    
    // Getters and Setters
    public int getEvacuated() { 
        return evacuated; 
    }
    
    public void setEvacuated(int evacuated) { 
        this.evacuated = evacuated; 
    }
    
    public int getInjured() { 
        return injured; 
    }
    
    public void setInjured(int injured) { 
        this.injured = injured; 
    }
    
    public int getMissing() { 
        return missing; 
    }
    
    public void setMissing(int missing) { 
        this.missing = missing; 
    }
    
    public PublicExposureRisk getPublicExposureRisk() { 
        return publicExposureRisk; 
    }
    
    public void setPublicExposureRisk(PublicExposureRisk publicExposureRisk) { 
        this.publicExposureRisk = publicExposureRisk; 
    }
}
