/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logging;

/**
 *
 * @author zubbo
 */
public class ConsoleLogger implements MissionLogger {
    @Override
    public void log(LogEntry logEntry) {
        System.out.println("[" + logEntry.getTimestamp() +
                "] " + "[" + logEntry.getStage() +
                "] " + logEntry.getMessage());
    }
}