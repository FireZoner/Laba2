/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.enums.*;

/**
 *
 * @author zubbo
 */
public class Sorcerer {
    private String name;
    private Rank rank;
    
    public Sorcerer(String name, Rank rank) {
        this.name = name;
        this.rank = rank;
    }
    
    public Sorcerer() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Rank getRank() {
        return this.rank;
    }
    
    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
