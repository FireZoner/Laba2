/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum PublicExposureRisk {
    NONE,
    MINIMAL,
    MODERATE,
    HIGH,
    CRITICAL,
    EXPOSED;
    
    public static PublicExposureRisk parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return PublicExposureRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown public exposure risk: " + value);
            return null;
        }
    }
}
