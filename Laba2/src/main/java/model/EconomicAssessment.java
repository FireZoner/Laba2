/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author zubbo
 */
public class EconomicAssessment {
    private long totalDamageCost;
    private long infrastructureDamage;
    private long commercialDamage;
    private long transportDamage;
    private int recoveryEstimateDays;
    private boolean insuranceCovered;
    
    public EconomicAssessment() {
    }
    
    public EconomicAssessment(long totalDamageCost, long infrastructureDamage, 
                              long commercialDamage, long transportDamage,
                              int recoveryEstimateDays, boolean insuranceCovered) {
        this.totalDamageCost = totalDamageCost;
        this.infrastructureDamage = infrastructureDamage;
        this.commercialDamage = commercialDamage;
        this.transportDamage = transportDamage;
        this.recoveryEstimateDays = recoveryEstimateDays;
        this.insuranceCovered = insuranceCovered;
    }
    
    public long getTotalDamageCost() { 
        return totalDamageCost; 
    }
    
    public void setTotalDamageCost(long totalDamageCost) { 
        this.totalDamageCost = totalDamageCost; 
    }
    
    public long getInfrastructureDamage() { 
        return infrastructureDamage; 
    }
    
    public void setInfrastructureDamage(long infrastructureDamage) { 
        this.infrastructureDamage = infrastructureDamage; 
    }
    
    public long getCommercialDamage() { 
        return commercialDamage; 
    }
    
    public void setCommercialDamage(long commercialDamage) { 
        this.commercialDamage = commercialDamage; 
    }
    
    public long getTransportDamage() { 
        return transportDamage; 
    }
    
    public void setTransportDamage(long transportDamage) { 
        this.transportDamage = transportDamage; 
    }
    
    public int getRecoveryEstimateDays() { 
        return recoveryEstimateDays; 
    }
    
    public void setRecoveryEstimateDays(int recoveryEstimateDays) { 
        this.recoveryEstimateDays = recoveryEstimateDays; 
    }
    
    public boolean isInsuranceCovered() { 
        return insuranceCovered; 
    }
    
    public void setInsuranceCovered(boolean insuranceCovered) { 
        this.insuranceCovered = insuranceCovered; 
    }
}
