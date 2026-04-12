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
public class EnvironmentConditions {
    private Weather weather;
    private TimeOfDay timeOfDay;
    private Visibility visibility;
    private int cursedEnergyDensity;
    
    public EnvironmentConditions() {
    }
    
    public Weather getWeather() { 
        return weather; 
    }
    
    public void setWeather(Weather weather) { 
        this.weather = weather; 
    }
    
    public TimeOfDay getTimeOfDay() { 
        return timeOfDay; 
    }
    
    public void setTimeOfDay(TimeOfDay timeOfDay) { 
        this.timeOfDay = timeOfDay; 
    }
    
    public Visibility getVisibility() { 
        return visibility; 
    }
    
    public void setVisibility(Visibility visibility) { 
        this.visibility = visibility; 
    }
    
    public int getCursedEnergyDensity() { 
        return cursedEnergyDensity; 
    }
    
    public void setCursedEnergyDensity(int cursedEnergyDensity) { 
        this.cursedEnergyDensity = cursedEnergyDensity; 
    }
}
