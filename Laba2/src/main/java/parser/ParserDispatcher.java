/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import model.*;
import model.builder.MissionBuilder;
import java.io.*;
import java.util.*;

/**
 * @author zubbo
 */
public class ParserDispatcher {
    private final List<ParserStrategy> strategies;
    
    public ParserDispatcher() {
        this.strategies = new ArrayList<>();
    }
    
    public void registerStrategy(ParserStrategy strategy) {
        strategies.add(strategy);
    }
    
    public void registerStrategies(List<ParserStrategy> strategies) {
        this.strategies.addAll(strategies);
    }
    
    public Mission parse(File file) throws IOException {
        ParserStrategy selectedStrategy = strategies.stream()
            .filter(s -> s.supports(file))
            .findFirst()
            .orElseThrow(() -> new UnsupportedOperationException(
                "No parser found for file: " + file.getName()
            ));
        
        MissionBuilder builder = new MissionBuilder();
        return selectedStrategy.parse(file, builder);
    }
    
    public List<Mission> parseBatch(List<File> files) throws IOException {
        List<Mission> missions = new ArrayList<>();
        for (File file : files) {
            try {
                missions.add(parse(file));
            } catch (IOException e) {
                System.out.println("Error parsing " + file.getName() + ": " + e.getMessage());
            }
        }
        return missions;
    }
}
