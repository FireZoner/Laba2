/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.Mission;

/**
 * Интерфейс для валидации миссии
 * TODO: Реализовать цепочку валидаторов
 * @author zubbo
 */

public interface MissionValidator {
    public ValidationResult validate(Mission mission);
}
