/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum TimeOfDay {
    DAWN,
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT,
    MIDNIGHT;
    
    public static TimeOfDay parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return TimeOfDay.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown timeOfDay: " + value);
            return null;
        }
    }
}