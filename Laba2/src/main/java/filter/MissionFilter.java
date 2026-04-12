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
public interface MissionFilter {
    boolean accept(Mission mission);
}
