/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analysis;

import java.util.*;

/**
 *
 * @author zubbo
 */
public class AnalysisResult {
    private final Map<String, Object> metrics = new LinkedHashMap<>();
    private final Map<String, String> errors = new LinkedHashMap<>();
    private final Map<String, String> warnings = new LinkedHashMap<>();

    public void putMetric(String key, Object value) {
        metrics.put(key, value);
    }

    public Object getMetric(String key) {
        return metrics.get(key);
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public void addError(String key, String message) {
        errors.put(key, message);
    }

    public void addWarning(String key, String message) {
        warnings.put(key, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public Map<String, String> getWarnings() {
        return warnings;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
