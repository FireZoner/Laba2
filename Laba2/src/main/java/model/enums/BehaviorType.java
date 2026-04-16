/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum BehaviorType {
    AMBUSH_PREDATOR,
    DIRECT_ASSAULT,
    TRAP_USAGE;
    
    public static BehaviorType parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return BehaviorType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown behavior type: " + value);
            return null;
        }
    }
}