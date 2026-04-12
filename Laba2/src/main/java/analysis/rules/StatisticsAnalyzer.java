/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package analysis.rules;

import analysis.AnalysisResult;
import analysis.Analyzer;
import model.Mission;
import model.Technique;

/**
 *
 * @author zubbo
 */
public class StatisticsAnalyzer extends Analyzer {
    @Override
    protected void check(Mission mission, AnalysisResult analysisResult) {
        int sorcerersCount = mission.getSorcerers() == null ? 0 : mission.getSorcerers().size();
        int techniquesCount = mission.getTechniques() == null ? 0 : mission.getTechniques().size();
        long totalDamage = 0;

        if (mission.getTechniques() != null) {
            for (Technique technique : mission.getTechniques()) {
                totalDamage += technique.getDamage();
            }
        }

        analysisResult.putMetric("sorcerers.count", sorcerersCount);
        analysisResult.putMetric("techniques.count", techniquesCount);
        analysisResult.putMetric("techniques.totalDamage", totalDamage);

        if (techniquesCount > 0) {
            analysisResult.putMetric("techniques.avgDamage", totalDamage / techniquesCount);
        }
        
        if (mission.getDamageCost() > 0) {
            analysisResult.putMetric("damage.total", mission.getDamageCost());
        }
    }
}
