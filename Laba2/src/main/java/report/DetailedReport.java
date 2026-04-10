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
public class DetailedReport implements MissionReport {
    private static final String SEPARATOR = "───────────────────────────────────────────────────────────────";
    private static final String LINE = "═══════════════════════════════════════════════════════════════";
    
    @Override
    public String visit(Mission mission) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(LINE).append("\n");
        sb.append("             MISSION ANALYZER - DETAILED REPORT          \n");
        sb.append(LINE).append("\n\n");
        
        sb.append("1. MISSION INFORMATION\n");
        sb.append(SEPARATOR).append("\n");
        sb.append(String.format("  Mission ID:     %s\n", nullToEmpty(mission.getMissionId())));
        sb.append(String.format("  Date:           %s\n", nullToEmpty(mission.getDate())));
        sb.append(String.format("  Location:       %s\n", nullToEmpty(mission.getLocation())));
        sb.append(String.format("  Outcome:        %s\n", mission.getOutcome() != null ? mission.getOutcome().name()
                : "-"));
        sb.append(String.format("  Damage Cost:    %d\n", mission.getDamageCost()));
        if (mission.getComment() != null && !mission.getComment().isEmpty()) {
            sb.append(String.format("  Comment:        %s\n", mission.getComment()));
        }
        sb.append("\n");
        
        if (mission.getCurse() != null) {
            sb.append("2. CURSE INFORMATION\n");
            sb.append(SEPARATOR).append("\n");
            sb.append(String.format("  Name:           %s\n", nullToEmpty(mission.getCurse().getName())));
            sb.append(String.format("  Threat Level:   %s\n", mission.getCurse().getThreatLevel() != null ? 
                mission.getCurse().getThreatLevel().name() : "-"));
            sb.append("\n");
        }
        
        if (!mission.getSorcerers().isEmpty()) {
            sb.append("3. SORCERERS (").append(mission.getSorcerers().size()).append(")\n");
            sb.append(SEPARATOR).append("\n");
            for (int i = 0; i < mission.getSorcerers().size(); i++) {
                Sorcerer s = mission.getSorcerers().get(i);
                sb.append(String.format("  %d. %s", i + 1, nullToEmpty(s.getName())));
                if (s.getRank() != null) {
                    sb.append(String.format(" [%s]", s.getRank().name()));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        
        if (!mission.getTechniques().isEmpty()) {
            sb.append("4. TECHNIQUES (").append(mission.getTechniques().size()).append(")\n");
            sb.append(SEPARATOR).append("\n");
            for (int i = 0; i < mission.getTechniques().size(); i++) {
                Technique t = mission.getTechniques().get(i);
                sb.append(String.format("  %d. %s", i + 1, nullToEmpty(t.getName())));
                if (t.getType() != null) {
                    sb.append(String.format(" [%s]", t.getType().name()));
                }
                sb.append("\n");
                if (t.getOwner() != null && t.getOwner().getName() != null) {
                    sb.append(String.format("     Owner:  %s\n", t.getOwner().getName()));
                }
                if (t.getDamage() > 0) {
                    sb.append(String.format("     Damage: %d\n", t.getDamage()));
                }
                sb.append("\n");
            }
        }
        
        if (mission.getEconomicAssessment() != null) {
            EconomicAssessment ea = mission.getEconomicAssessment();
            sb.append("5. ECONOMIC ASSESSMENT\n");
            sb.append(SEPARATOR).append("\n");
            sb.append(String.format("  Total Damage:        %d\n", ea.getTotalDamageCost()));
            sb.append(String.format("  Infrastructure:      %d\n", ea.getInfrastructureDamage()));
            sb.append(String.format("  Commercial:          %d\n", ea.getCommercialDamage()));
            sb.append(String.format("  Transport:           %d\n", ea.getTransportDamage()));
            sb.append(String.format("  Recovery Estimate:   %d days\n", ea.getRecoveryEstimateDays()));
            sb.append(String.format("  Insurance Covered:   %b\n", ea.isInsuranceCovered()));
            sb.append("\n");
        }
        
        if (mission.getEnvironmentConditions() != null) {
            EnvironmentConditions ec = mission.getEnvironmentConditions();
            sb.append("6. ENVIRONMENT CONDITIONS\n");
            sb.append(SEPARATOR).append("\n");
            if (ec.getWeather() != null) sb.append(String.format("  Weather:            %s\n", ec.getWeather().name()));
            if (ec.getTimeOfDay() != null) sb.append(String.format("  Time of Day:        %s\n", ec.getTimeOfDay().name()));
            if (ec.getVisibility() != null) sb.append(String.format("  Visibility:         %s\n", ec.getVisibility().name()));
            if (ec.getCursedEnergyDensity() > 0) {
                sb.append(String.format("  Cursed Energy:      %d\n", ec.getCursedEnergyDensity()));
            }
            sb.append("\n");
        }
        
        if (mission.getEnemyActivity() != null) {
            EnemyActivity ea = mission.getEnemyActivity();
            sb.append("7. ENEMY ACTIVITY\n");
            sb.append(SEPARATOR).append("\n");
            if (ea.getBehaviorType() != null) sb.append(String.format("  Behavior:           %s\n", 
                    ea.getBehaviorType().name()));
            if (ea.getTargetPriority() != null) sb.append(String.format("  Target Priority:    %s\n", 
                    ea.getTargetPriority()));
            if (ea.getMobility() != null) sb.append(String.format("  Mobility:           %s\n", 
                    ea.getMobility().name()));
            if (ea.getEscalationRisk() != null) sb.append(String.format("  Escalation Risk:    %s\n", 
                    ea.getEscalationRisk().name()));
            
            if (!ea.getAttackPatterns().isEmpty()) {
                sb.append("  Attack Patterns:\n");
                for (String pattern : ea.getAttackPatterns()) {
                    sb.append(String.format("    - %s\n", pattern));
                }
            }
            
            if (!ea.getCountermeasures().isEmpty()) {
                sb.append("  Countermeasures:\n");
                for (String measure : ea.getCountermeasures()) {
                    sb.append(String.format("    - %s\n", measure));
                }
            }
            sb.append("\n");
        }
        
        if (mission.getCivilianImpact() != null) {
            CivilianImpact ci = mission.getCivilianImpact();
            sb.append("8. CIVILIAN IMPACT\n");
            sb.append(SEPARATOR).append("\n");
            sb.append(String.format("  Evacuated:          %d\n", ci.getEvacuated()));
            sb.append(String.format("  Injured:            %d\n", ci.getInjured()));
            sb.append(String.format("  Missing:            %d\n", ci.getMissing()));
            if (ci.getPublicExposureRisk() != null) {
                sb.append(String.format("  Exposure Risk:      %s\n", ci.getPublicExposureRisk().name()));
            }
            sb.append("\n");
        }
        
        if (!mission.getOperationTimeline().isEmpty()) {
            sb.append("9. OPERATION TIMELINE\n");
            sb.append(SEPARATOR).append("\n");
            for (OperationTimeline event : mission.getOperationTimeline()) {
                String time = event.getTimestamp() != null ? event.getTimestamp().toString() : "??";
                sb.append(String.format("  [%s] %s\n", time, event.getDescription()));
                if (event.getType() != null) {
                    sb.append(String.format("        Type: %s\n", event.getType().name()));
                }
            }
            sb.append("\n");
        }
        
        boolean hasAdditional = false;
        StringBuilder additional = new StringBuilder();
        
        if (!mission.getMissionTags().isEmpty()) {
            hasAdditional = true;
            additional.append("  Tags: ").append(String.join(", ", mission.getMissionTags())).append("\n");
        }
        if (!mission.getSupportUnits().isEmpty()) {
            hasAdditional = true;
            additional.append("  Support Units: ").append(String.join(", ", mission.getSupportUnits())).append("\n");
        }
        if (!mission.getRecommendations().isEmpty()) {
            hasAdditional = true;
            additional.append("  Recommendations: ").append(String.join(", ", mission.getRecommendations())).append("\n");
        }
        if (!mission.getArtifactsRecovered().isEmpty()) {
            hasAdditional = true;
            additional.append("  Artifacts: ").append(String.join(", ", mission.getArtifactsRecovered())).append("\n");
        }
        if (!mission.getEvacuationZones().isEmpty()) {
            hasAdditional = true;
            additional.append("  Evacuation Zones: ").append(String.join(", ", mission.getEvacuationZones())).append("\n");
        }
        if (!mission.getStatusEffects().isEmpty()) {
            hasAdditional = true;
            additional.append("  Status Effects: ").append(String.join(", ", mission.getStatusEffects())).append("\n");
        }
        
        if (hasAdditional) {
            sb.append("10. ADDITIONAL DATA\n");
            sb.append(SEPARATOR).append("\n");
            sb.append(additional);
            sb.append("\n");
        }
        
        sb.append(LINE).append("\n");
        sb.append("                    END OF REPORT                                 \n");
        sb.append(LINE).append("\n");
        
        return sb.toString();
    }
    
    private String nullToEmpty(String s) {
        return s != null ? s : "-";
    }
}
