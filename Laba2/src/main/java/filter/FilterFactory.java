/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author zubbo
 */
public class FilterFactory {
    private final Map<String, MissionFilter> filters = new LinkedHashMap<>();

    public void register(String filterName, MissionFilter filter) {
        filters.put(filterName, filter);
    }

    public Map<String, MissionFilter> getFilters() {
        return filters;
    }

    public MissionFilter createFilterChain() throws Exception {
        if(filters.isEmpty()) {
            throw new Exception("Необходимо подключить фильтры!");
        }

        MissionFilter firstFilter = null;
        MissionFilter currentFilter = null;

        for(MissionFilter filter : filters.values()) {
            if (firstFilter == null) {
                firstFilter = filter;
                currentFilter = filter;
            } else {
                currentFilter = currentFilter.setNext(filter);
            }
        }

        return firstFilter;
    }
}