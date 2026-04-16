 /* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model.enums;

/**
 *
 * @author zubbo
 */
public enum ThreatLevel {
    HIGH,
    SPECIAL_GRADE;
    
    public static ThreatLevel parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return ThreatLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown threatLevel: " + value);
            return null;
        }
    }
}
