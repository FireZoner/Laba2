/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package analysis;

import model.Mission;

/**
 * @author zubbo
 */ 
public abstract class Analyzer implements MissionAnalyzer {
    private MissionAnalyzer nextAnalyzer;

    @Override
    public MissionAnalyzer setNext(MissionAnalyzer nextAnalyzer) {
        this.nextAnalyzer = nextAnalyzer;
        return nextAnalyzer;
    }

    @Override
    public final void analyze(Mission mission, AnalysisResult analysisResult) throws Exception {
        check(mission, analysisResult);

        if (nextAnalyzer != null) {
            nextAnalyzer.analyze(mission, analysisResult);
        }
    }

    protected abstract void check(Mission mission, AnalysisResult analysisResult) throws Exception;
}
