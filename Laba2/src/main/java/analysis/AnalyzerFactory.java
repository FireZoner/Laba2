/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analysis;

import analysis.rules.StatisticsAnalyzer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author zubbo
 */
public final class AnalyzerFactory {
    private final Map<String, MissionAnalyzer> analyzers = new LinkedHashMap<>();

    public AnalyzerFactory() {
        register("StatisticsAnalyzer", new StatisticsAnalyzer());
    }

    public void register(String analyzerName, MissionAnalyzer analyzer) {
        analyzers.put(analyzerName, analyzer);
    }

    public Map<String, MissionAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public MissionAnalyzer createAnalysisChain() {
        MissionAnalyzer firstAnalyzer = null;
        MissionAnalyzer currentAnalyzer = null;

        for (MissionAnalyzer analyzer : analyzers.values()) {
            if (firstAnalyzer == null) {
                firstAnalyzer = analyzer;
                currentAnalyzer = analyzer;
            } else {
                currentAnalyzer = currentAnalyzer.setNext(analyzer);
            }
        }

        return firstAnalyzer;
    }
}
