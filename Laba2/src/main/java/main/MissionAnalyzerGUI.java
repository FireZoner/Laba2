/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import model.Mission;
import parser.ParserDispatcher;
import parser.*;
import report.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;

public class MissionAnalyzerGUI extends JFrame {
    
    private JTextArea textArea;
    private JButton openButton;
    private JComboBox<ReportType> reportTypeCombo;
    private JLabel statusLabel;
    private ParserDispatcher dispatcher;
    
    public MissionAnalyzerGUI() {
        initDispatcher();
        initUI();
    }
    
    private void initDispatcher() {
        dispatcher = new ParserDispatcher();
        
        dispatcher.registerStrategy(new JsonParserStrategy());
        dispatcher.registerStrategy(new YamlParserStrategy());
        dispatcher.registerStrategy(new XmlParserStrategy());
        dispatcher.registerStrategy(new TxtParserStrategy());
        dispatcher.registerStrategy(new LegacyTxtParserStrategy());
        dispatcher.registerStrategy(new EmptyParserStrategy());
    }
    
    private void initUI() {
        setTitle("Mission Analyzer v2.0");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        openButton = new JButton("Open Mission File");
        openButton.setPreferredSize(new Dimension(180, 40));
        openButton.addActionListener(e -> openFile());
        leftPanel.add(openButton);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(new JLabel("Report Type:"));
        reportTypeCombo = new JComboBox<>(ReportType.values());
        reportTypeCombo.setPreferredSize(new Dimension(130, 30));
        rightPanel.add(reportTypeCombo);
        
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        statusLabel = new JLabel(" Ready - Select a mission file");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".txt") || name.endsWith(".json") || 
                       name.endsWith(".xml") || name.endsWith(".yaml") || 
                       name.endsWith(".yml");
            }
            
            @Override
            public String getDescription() {
                return "Mission Files (*.json, *.yaml, *.xml, *.txt)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadMission(file);
        }
    }
    
    private void loadMission(File file) {
        try {
            statusLabel.setText(" Loading: " + file.getName() + "...");
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            Mission mission = dispatcher.parse(file);
            
            ReportType selectedType = (ReportType) reportTypeCombo.getSelectedItem();
            String report = ReportFactory.generateReport(mission, selectedType);
            
            textArea.setText(report);
            textArea.setCaretPosition(0);
            
            statusLabel.setText(" Loaded: " + file.getName());
            
        } catch (IOException e) {
            textArea.setText("ERROR:\n" + e.getMessage());
            statusLabel.setText(" Error loading file");
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}