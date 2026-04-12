/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.Mission;

/**
 *
 * @author zubbo
 */

public class RequiredFieldsValidator implements MissionValidator {
    @Override
    public ValidationResult validate(Mission mission) {
        ValidationResult result = new ValidationResult();
        
        if (mission.getMissionId() == null || mission.getMissionId().isEmpty()) {
            result.addError("Mission ID is required");
        }
        if (mission.getDate() == null || mission.getDate().isEmpty()) {
            result.addError("Date is required");
        }
        if (mission.getLocation() == null || mission.getLocation().isEmpty()) {
            result.addError("Location is required");
        }
        
        return result;
    }
}
