/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum EscalationRisk {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;
    
    public static EscalationRisk parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return EscalationRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown escalation risk: " + value);
            return null;
        }
    }
}
