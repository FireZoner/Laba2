/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum Weather {
    CLEAR,
    RAIN,
    HEAVY_RAIN,
    STORM,
    FOG,
    SNOW;
    
    public static Weather parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Weather.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown weather: " + value);
            return null;
        }
    }
}
