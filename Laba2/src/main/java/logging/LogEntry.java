/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logging;

import java.time.LocalDateTime;

/**
 *
 * @author zubbo
 */
public class LogEntry {
    private final String stage;
    private final String message;
    private final LocalDateTime timestamp;

    public LogEntry(String stage, String message) {
        this.stage = stage;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getStage() {
        return stage;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}