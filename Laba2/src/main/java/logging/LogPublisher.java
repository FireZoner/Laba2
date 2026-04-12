/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logging;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author zubbo
 */
public class LogPublisher {
    private final Map<String, MissionLogger> loggers = new LinkedHashMap<>();

    public LogPublisher() {
        register("ConsoleLogger", new ConsoleLogger());
    }

    public final void register(String loggerName, MissionLogger logger) {
        loggers.put(loggerName, logger);
    }

    public void publish(String stage, String message) {
        LogEntry logEntry = new LogEntry(stage, message);

        for(MissionLogger logger : loggers.values()) {
            logger.log(logEntry);
        }
    }
}
