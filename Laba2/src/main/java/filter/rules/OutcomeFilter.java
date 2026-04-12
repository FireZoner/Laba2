/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter.rules;

import filter.Filter;
import model.Mission;

/**
 *
 * @author zubbo
 */
public class OutcomeFilter extends Filter {
    private final String expectedOutcome;

    public OutcomeFilter(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if(expectedOutcome == null || expectedOutcome.isBlank()) {
            return;
        }

        if(!expectedOutcome.equals(mission.getOutcome())) {
            throw new Exception("Миссия не прошла фильтр по результату!");
        }
    }
}
