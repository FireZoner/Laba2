/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import model.Mission;
import java.util.*;

/**
 * Составной фильтр с поддержкой AND/OR
 * TODO: Реализовать комбинирование фильтров
 * @author zubbo
 */
public class CompositeFilter implements MissionFilter {
    private final List<MissionFilter> filters = new ArrayList<>();
    private final boolean andMode = true;           // true = AND, false = OR
    
    public CompositeFilter addFilter(MissionFilter filter) {
        filters.add(filter);
        return this;
    }
    
    @Override
    public boolean accept(Mission mission) {
        if (andMode) {
            return filters.stream().allMatch(f -> f.accept(mission));
        } else {
            return filters.stream().anyMatch(f -> f.accept(mission));
        }
    }
}