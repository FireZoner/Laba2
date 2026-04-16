/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import java.util.ArrayList;
import java.util.List;
import model.Mission;

/**
 *
 * @author zubbo
 */
public class ValidatorFactory {
    private final List<MissionValidator> validators = new ArrayList<>();
    
    public ValidatorFactory() {
        register(new RequiredFieldsValidator());
        register(new ConsistencyValidator());
    }
    
    public final void register(MissionValidator validator) {
        validators.add(validator);
    }
    
    public ValidationResult validate(Mission mission) {
        ValidationResult result = new ValidationResult();
        for (MissionValidator validator : validators) {
            ValidationResult partial = validator.validate(mission);
            for (String error : partial.getErrors()) {
                result.addError(error);
            }
            for (String warning : partial.getWarnings()) {
                result.addWarning(warning);
            }
        }
        return result;
    }
}