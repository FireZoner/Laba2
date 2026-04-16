/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model.enums;

/**
 *
 * @author zubbo
 */
public enum Outcome {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILURE;
    
    public static Outcome parse(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Outcome.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown outcome: " + value);
            return null;
        }
    }
}
