/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model.enums;

/**
 *
 * @author zubbo
 */
public enum TechniqueType {
    INNATE,
    SHIKIGAMI,
    BODY,
    WEAPON;
    
    public static TechniqueType parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return TechniqueType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown technique type: " + value);
            return null;
        }
    }
}
