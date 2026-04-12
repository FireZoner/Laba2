/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter.rules;

import filter.Filter;
import model.Curse;
import model.Mission;


/**
 *
 * @author zubbo
 */
public class ThreatLevelFilter extends Filter {
    private final String expectedThreatLevel;

    public ThreatLevelFilter(String expectedThreatLevel) {
        this.expectedThreatLevel = expectedThreatLevel;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if(expectedThreatLevel == null || expectedThreatLevel.isBlank()) {
            return;
        }

        Curse curse = mission.getCurse();
        if(curse == null) {
            return;
        }

        if(!expectedThreatLevel.equals(curse.getThreatLevel())) {
            throw new Exception("Миссия не прошла фильтр по уровню угрозы!");
        }
    }
}
