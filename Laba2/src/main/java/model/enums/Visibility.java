/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum Visibility {
    EXCELLENT,
    GOOD,
    MODERATE,
    POOR,
    VERY_POOR,
    LOW,
    ZERO;
    
    public static Visibility parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Visibility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown visibility: " + value);
            return null;
        }
    }
}
