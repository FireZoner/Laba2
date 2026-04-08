/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import model.*;
import parser.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author zubbo
 */


public class MissionAnalyzerGUI extends JFrame {
    
    private JTextArea textArea;
    private JButton openButton;
    private JLabel statusLabel;
    
    public MissionAnalyzerGUI() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Mission Analyzer");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        openButton = new JButton("Открыть файл миссии");
        openButton.setPreferredSize(new Dimension(200, 40));
        openButton.addActionListener(e -> openFile());
        
        statusLabel = new JLabel(" Выберите файл (txt, json, xml)");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        topPanel.add(openButton, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.CENTER);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        textArea.setMargin(new Insets(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".txt") || name.endsWith(".json") || name.endsWith(".xml");
            }
            @Override
            public String getDescription() {
                return "Файлы миссий (*.txt, *.json, *.xml)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadMission(file);
        }
    }
    
    private void loadMission(File file) {
        try {
            statusLabel.setText("Загрузка: " + file.getName());
            
            MissionParser parser = ParserFactory.getParser(file);
            Mission mission = parser.parse(file);
            
            textArea.setText(formatMission(mission));
            textArea.setCaretPosition(0);
            statusLabel.setText("Загружено: " + file.getName());
            
        } catch (IOException e) {
            textArea.setText("ОШИБКА:\n" + e.getMessage());
            statusLabel.setText("Ошибка загрузки");
        }
    }
    
    private String formatMission(Mission m) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    MISSION ANALYZER                   \n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");
        
        sb.append("МИССИЯ:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  ID:          %s\n", nullToEmpty(m.getMissionId())));
        sb.append(String.format("  Дата:        %s\n", nullToEmpty(m.getDate())));
        sb.append(String.format("  Локация:     %s\n", nullToEmpty(m.getLocation())));
        sb.append(String.format("  Результат:   %s\n", m.getOutcome() != null ? m.getOutcome() : "-"));
        sb.append(String.format("  Ущерб:       %,d иен\n", m.getDamageCost()));
        sb.append(String.format("  Комментарий: %s\n\n", nullToEmpty(m.getComment())));
        
        if (m.getCurse() != null) {
            sb.append("ПРОКЛЯТИЕ:\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Имя:          %s\n", nullToEmpty(m.getCurse().getName())));
            sb.append(String.format("  Уровень угрозы: %s\n\n", 
                m.getCurse().getThreatLevel() != null ? m.getCurse().getThreatLevel() : "-"));
        }
        
        if (!m.getSorcerers().isEmpty()) {
            sb.append("МАГИ:\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            for (int i = 0; i < m.getSorcerers().size(); i++) {
                Sorcerer s = m.getSorcerers().get(i);
                sb.append(String.format("  %d. %s (%s)\n", 
                    i + 1, 
                    nullToEmpty(s.getName()), 
                    s.getRank() != null ? s.getRank() : "-"));
            }
            sb.append("\n");
        }
        
        if (!m.getTechniques().isEmpty()) {
            sb.append("ТЕХНИКИ:\n");
            sb.append("───────────────────────────────────────────────────────────────\n");
            for (int i = 0; i < m.getTechniques().size(); i++) {
                Technique t = m.getTechniques().get(i);
                sb.append(String.format("  %d. %s [%s]\n", 
                    i + 1, 
                    nullToEmpty(t.getName()), 
                    t.getType() != null ? t.getType() : "-"));
                
                String ownerName = t.getOwner() != null ? t.getOwner().getName() : "неизвестен";
                sb.append(String.format("     Владелец: %s\n", nullToEmpty(ownerName)));
                sb.append(String.format("     Урон: %,d\n\n", t.getDamage()));
            }
        }
        
        sb.append("═══════════════════════════════════════════════════════════════\n");
        return sb.toString();
    }
    
    private String nullToEmpty(String s) {
        return s != null ? s : "";
    }
}