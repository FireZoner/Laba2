/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.enums;

/**
 *
 * @author zubbo
 */
public enum TimelineEventType {
    DETECTION,
    ENGAGEMENT,
    CIVILIAN_EVACUATION,
    REINFORCEMENT_ARRIVAL,
    TECHNIQUE_ACTIVATION,
    RETREAT,
    VICTORY,
    CASUALTY_REPORT,
    AFTERMATH;
    
    public static TimelineEventType parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return TimelineEventType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown timeline event type: " + value);
            return null;
        }
    }
}