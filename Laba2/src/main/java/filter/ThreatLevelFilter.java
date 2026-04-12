/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package filter;

import model.Mission;
import model.enums.ThreatLevel;

/**
 *
 * @author zubbo
 */
public class ThreatLevelFilter implements MissionFilter {
    private final ThreatLevel level;
    
    public ThreatLevelFilter(ThreatLevel level) {
        this.level = level;
    }
    
    @Override
    public boolean accept(Mission mission) {
        if (mission.getCurse() == null) return false;
        return mission.getCurse().getThreatLevel() == level;
    }
}