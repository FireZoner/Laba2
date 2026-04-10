/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;
import model.*;
import model.enums.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author zubbo
 */
public class StatisticsReport implements MissionReport {
    @Override
    public String visit(Mission mission) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("══════════════════════════════════════════════════════════════\n");
        sb.append("                 STATISTICS REPORT                \n");
        sb.append("══════════════════════════════════════════════════════════════\n\n");
        
        sb.append("PARTICIPATION STATISTICS\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  Total Sorcerers:  %d\n", mission.getSorcerers().size()));
        sb.append(String.format("  Total Techniques: %d\n", mission.getTechniques().size()));
        double techPerSorcerer = mission.getSorcerers().isEmpty() ? 0 : 
            (double) mission.getTechniques().size() / mission.getSorcerers().size();
        sb.append(String.format("  Techniques per Sorcerer: %.2f\n", techPerSorcerer));
        sb.append("\n");
        
        if (!mission.getSorcerers().isEmpty()) {
            sb.append("RANK DISTRIBUTION\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            
            Map<Rank, Long> rankCount = mission.getSorcerers().stream()
                .filter(s -> s.getRank() != null)
                .collect(Collectors.groupingBy(Sorcerer::getRank, Collectors.counting()));
            
            for (Map.Entry<Rank, Long> entry : rankCount.entrySet()) {
                double percentage = (double) entry.getValue() / mission.getSorcerers().size() * 100;
                sb.append(String.format("  %-15s %d (%.1f%%)\n", 
                    entry.getKey().name(), entry.getValue(), percentage));
            }
            sb.append("\n");
        }
        
        if (!mission.getTechniques().isEmpty()) {
            sb.append("TECHNIQUE TYPE DISTRIBUTION\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            
            Map<TechniqueType, Long> typeCount = mission.getTechniques().stream()
                .filter(t -> t.getType() != null)
                .collect(Collectors.groupingBy(Technique::getType, Collectors.counting()));
            
            for (Map.Entry<TechniqueType, Long> entry : typeCount.entrySet()) {
                double percentage = (double) entry.getValue() / mission.getTechniques().size() * 100;
                sb.append(String.format("  %-15s %d (%.1f%%)\n", 
                    entry.getKey().name(), entry.getValue(), percentage));
            }
            sb.append("\n");
        }
        
        if (!mission.getTechniques().isEmpty()) {
            sb.append("DAMAGE ANALYSIS\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            
            long totalTechDamage = mission.getTechniques().stream()
                .mapToLong(Technique::getDamage).sum();
            
            double avgDamage = mission.getTechniques().stream()
                .mapToLong(Technique::getDamage)
                .average().orElse(0);
            
            long maxDamage = mission.getTechniques().stream()
                .mapToLong(Technique::getDamage)
                .max().orElse(0);
            
            long minDamage = mission.getTechniques().stream()
                .mapToLong(Technique::getDamage)
                .min().orElse(0);
            
            long nonZeroDamageCount = mission.getTechniques().stream()
                .filter(t -> t.getDamage() > 0)
                .count();
            
            sb.append(String.format("  Total Tech Damage: %d\n", totalTechDamage));
            sb.append(String.format("  Average Damage:    %.0f\n", avgDamage));
            sb.append(String.format("  Max Damage:        %d\n", maxDamage));
            sb.append(String.format("  Min Damage:        %d\n", minDamage));
            sb.append(String.format("  Techniques with Damage: %d / %d\n", 
                nonZeroDamageCount, mission.getTechniques().size()));
            sb.append(String.format("  Mission Damage:    %d\n", mission.getDamageCost()));
            
            if (mission.getDamageCost() > 0) {
                double damageRatio = (double) totalTechDamage / mission.getDamageCost() * 100;
                sb.append(String.format("  Tech Damage / Mission Damage: %.1f%%\n", damageRatio));
            } else if (totalTechDamage > 0) {
                sb.append("  Tech Damage / Mission Damage: N/A (Mission Damage = 0)\n");
            } else {
                sb.append("  Tech Damage / Mission Damage: N/A\n");
            }
            sb.append("\n");
        }
        
        if (!mission.getTechniques().isEmpty()) {
            sb.append("DAMAGE BY SORCERER\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            
            Map<String, Long> damageBySorcerer = new HashMap<>();
            for (Technique t : mission.getTechniques()) {
                if (t.getOwner() != null && t.getOwner().getName() != null && t.getDamage() > 0) {
                    damageBySorcerer.merge(t.getOwner().getName(), t.getDamage(), Long::sum);
                }
            }
            
            if (damageBySorcerer.isEmpty()) {
                sb.append("  No damage data with valid owner\n");
            } else {
                long totalDamage = damageBySorcerer.values().stream().mapToLong(Long::longValue).sum();
                for (Map.Entry<String, Long> entry : damageBySorcerer.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .collect(Collectors.toList())) {
                    
                    double percentage = totalDamage > 0 ? 
                        (double) entry.getValue() / totalDamage * 100 : 0;
                    sb.append(String.format("  %-20s %12d (%.1f%% of tech damage)\n", 
                        entry.getKey(), entry.getValue(), percentage));
                }
            }
            sb.append("\n");
        }
        
        if (mission.getEconomicAssessment() != null) {
            EconomicAssessment ea = mission.getEconomicAssessment();
            sb.append("ECONOMIC METRICS\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Total Economic Damage: %d\n", ea.getTotalDamageCost()));
            
            if (ea.getRecoveryEstimateDays() > 0) {
                sb.append(String.format("  Damage per Recovery Day: %.0f\n", 
                    (double) ea.getTotalDamageCost() / ea.getRecoveryEstimateDays()));
            }
            
            if (ea.getTotalDamageCost() > 0) {
                sb.append(String.format("  Infrastructure Damage: %d (%.1f%%)\n", 
                    ea.getInfrastructureDamage(),
                    (double) ea.getInfrastructureDamage() / ea.getTotalDamageCost() * 100));
                sb.append(String.format("  Commercial Damage:     %d (%.1f%%)\n",
                    ea.getCommercialDamage(),
                    (double) ea.getCommercialDamage() / ea.getTotalDamageCost() * 100));
                sb.append(String.format("  Transport Damage:      %d (%.1f%%)\n",
                    ea.getTransportDamage(),
                    (double) ea.getTransportDamage() / ea.getTotalDamageCost() * 100));
            } else {
                sb.append("  Damage breakdown: N/A (Total Damage = 0)\n");
            }
            sb.append("\n");
        }
        
        sb.append("═══════════════════════════════════════════════════════════════\n");
        
        return sb.toString();
    }
}
