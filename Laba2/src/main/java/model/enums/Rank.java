/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model.enums;

/**
 *
 * @author zubbo
 */
public enum Rank {
    SPECIAL_GRADE,
    GRADE_1,
    GRADE_2,
    SEMI_GRADE_1;
    
    public static Rank parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Rank.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown rank: " + value);
            return null;
        }
    }
}
