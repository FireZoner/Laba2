/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package report;

import model.*;

/**
 *
 * @author zubbo
 */
public class RiskReport implements MissionReport {
    @Override
    public String visit(Mission mission) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("══════════════════════════════════════════════════════════════\n");
        sb.append("            RISK ASSESSMENT REPORT              \n");
        sb.append("══════════════════════════════════════════════════════════════\n\n");
        
        sb.append("1. THREAT ASSESSMENT\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getCurse() != null && mission.getCurse().getThreatLevel() != null) {
            sb.append(String.format("  Curse Threat Level: %s\n", 
                mission.getCurse().getThreatLevel().name()));
        } else {
            sb.append("  Curse Threat Level: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("2. MISSION OUTCOME\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getOutcome() != null) {
            sb.append(String.format("  Outcome: %s\n", mission.getOutcome().name()));
        } else {
            sb.append("  Outcome: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("3. ESCALATION POTENTIAL\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getEnemyActivity() != null && mission.getEnemyActivity().getEscalationRisk() != null) {
            sb.append(String.format("  Escalation Risk: %s\n", 
                mission.getEnemyActivity().getEscalationRisk().name()));
        } else {
            sb.append("  Escalation Risk: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("4. CIVILIAN IMPACT\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getCivilianImpact() != null) {
            CivilianImpact ci = mission.getCivilianImpact();
            sb.append(String.format("  Evacuated: %d\n", ci.getEvacuated()));
            sb.append(String.format("  Injured:   %d\n", ci.getInjured()));
            sb.append(String.format("  Missing:   %d\n", ci.getMissing()));
            if (ci.getPublicExposureRisk() != null) {
                sb.append(String.format("  Public Exposure Risk: %s\n", ci.getPublicExposureRisk().name()));
            }
        } else {
            sb.append("  Civilian Impact: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("5. ENEMY BEHAVIOR\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getEnemyActivity() != null) {
            EnemyActivity ea = mission.getEnemyActivity();
            if (ea.getBehaviorType() != null) {
                sb.append(String.format("  Behavior Type: %s\n", ea.getBehaviorType().name()));
            }
            if (ea.getMobility() != null) {
                sb.append(String.format("  Mobility: %s\n", ea.getMobility().name()));
            }
            if (!ea.getAttackPatterns().isEmpty()) {
                sb.append("  Attack Patterns:\n");
                for (String pattern : ea.getAttackPatterns()) {
                    sb.append(String.format("    - %s\n", pattern));
                }
            }
        } else {
            sb.append("  Enemy Behavior: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("6. ENVIRONMENT FACTORS\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (mission.getEnvironmentConditions() != null) {
            EnvironmentConditions ec = mission.getEnvironmentConditions();
            if (ec.getWeather() != null) {
                sb.append(String.format("  Weather: %s\n", ec.getWeather().name()));
            }
            if (ec.getTimeOfDay() != null) {
                sb.append(String.format("  Time of Day: %s\n", ec.getTimeOfDay().name()));
            }
            if (ec.getVisibility() != null) {
                sb.append(String.format("  Visibility: %s\n", ec.getVisibility().name()));
            }
            if (ec.getCursedEnergyDensity() > 0) {
                sb.append(String.format("  Cursed Energy Density: %d\n", ec.getCursedEnergyDensity()));
            }
        } else {
            sb.append("  Environment Factors: Not specified\n");
        }
        sb.append("\n");
        
        sb.append("═══════════════════════════════════════════════════════════════\n");
        
        return sb.toString();
    }
}
