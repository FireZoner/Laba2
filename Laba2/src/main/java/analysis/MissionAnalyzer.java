/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package analysis;

import model.Mission;

/**
 * @author zubbo
 */
public interface MissionAnalyzer {
    MissionAnalyzer setNext(MissionAnalyzer nextAnalyzer);
    void analyze(Mission mission, AnalysisResult analysisResult) throws Exception;
}
