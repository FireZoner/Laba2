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
public class SummaryReport implements MissionReport {
    @Override
    public String visit(Mission mission) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("══════════════════════════════════════════════════════════════\n");
        sb.append("                  MISSION SUMMARY                 \n");
        sb.append("══════════════════════════════════════════════════════════════\n\n");
        
        sb.append("BASIC INFORMATION\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  ID:       %s\n", nullToEmpty(mission.getMissionId())));
        sb.append(String.format("  Date:     %s\n", nullToEmpty(mission.getDate())));
        sb.append(String.format("  Location: %s\n", nullToEmpty(mission.getLocation())));
        sb.append(String.format("  Outcome:  %s\n", mission.getOutcome() != null ? mission.getOutcome().name() : "-"));
        sb.append(String.format("  Damage:   %d\n\n", mission.getDamageCost()));
        
        if (mission.getCurse() != null) {
            sb.append("CURSE\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Name:  %s\n", nullToEmpty(mission.getCurse().getName())));
            sb.append(String.format("  Level: %s\n\n", mission.getCurse().getThreatLevel() != null ? 
                mission.getCurse().getThreatLevel().name() : "-"));
        }
        
        sb.append("FORCES\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  Sorcerers:  %d\n", mission.getSorcerers().size()));
        sb.append(String.format("  Techniques: %d\n\n", mission.getTechniques().size()));
        
        if (mission.getEconomicAssessment() != null) {
            sb.append("ECONOMIC\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Total Damage: %d\n", 
                mission.getEconomicAssessment().getTotalDamageCost()));
            sb.append(String.format("  Recovery Days: %d\n\n", 
                mission.getEconomicAssessment().getRecoveryEstimateDays()));
        }
        
        if (mission.getEnemyActivity() != null && mission.getEnemyActivity().getEscalationRisk() != null) {
            sb.append("RISK\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Escalation Risk: %s\n", 
                mission.getEnemyActivity().getEscalationRisk().name()));
        }
        
        return sb.toString();
    }
    
    private String nullToEmpty(String s) {
        return s != null ? s : "-";
    }
}
