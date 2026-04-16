/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum Mobility {
    LOW,
    MEDIUM,
    HIGH,
    EXTREME;
    
    public static Mobility parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Mobility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown mobility: " + value);
            return null;
        }
    }
}
