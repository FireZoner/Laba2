/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.Mission;
import model.enums.ThreatLevel;

/**
 * Заготовка для валидации согласованности данных
 * TODO: Проверка, что SPECIAL_GRADE требует особого комментария
 * @author zubbo
 */

public class ConsistencyValidator implements MissionValidator {
    @Override
    public ValidationResult validate(Mission mission) {
        ValidationResult result = new ValidationResult();
        
        if (mission.getCurse() != null && 
            mission.getCurse().getThreatLevel() == ThreatLevel.SPECIAL_GRADE) {
            if (mission.getComment() == null || mission.getComment().isEmpty()) {
                result.addWarning("SPECIAL GRADE curse should have a comment");
            }
        }
        return result;
    }
}
