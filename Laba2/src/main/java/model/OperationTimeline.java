/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import model.enums.*;

/**
 *
 * @author zubbo
 */
public class OperationTimeline {
    private LocalDateTime timestamp;
    private TimelineEventType type;
    private String description;
    
    public OperationTimeline() {}
    
    public OperationTimeline(LocalDateTime timestamp, TimelineEventType type, String description) {
        this.timestamp = timestamp;
        this.type = type;
        this.description = description;
    }
    
    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp; 
    }
    
    public TimelineEventType getType() { 
        return type; 
    }
    
    public void setType(TimelineEventType type) { 
        this.type = type; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) {
        this.description = description; 
    }
}
