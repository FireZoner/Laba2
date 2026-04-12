/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import model.Mission;

/**
 *
 * @author zubbo
 */
public abstract class Filter implements MissionFilter {
    private MissionFilter nextFilter;

    @Override
    public MissionFilter setNext(MissionFilter nextFilter) {
        this.nextFilter = nextFilter;
        return nextFilter;
    }

    @Override
    public final void filter(Mission mission) throws Exception {
        check(mission);

        if(nextFilter != null) {
            nextFilter.filter(mission);
        }
    }

    protected abstract void check(Mission mission) throws Exception;
}
