package cvut.gartnkry;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static cvut.gartnkry.Settings.*;

public class AppLogger {
    private static final Logger LOGGER = Logger.getGlobal();

    public static void init() {
        try {
            LOGGER.setLevel(logLevel);
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT.%1$tL %4$s %5$s%6$s%n");
            if (logConsole) {
                // ...
            }
            if (logFile) {
                FileHandler fileHandler = new FileHandler("log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
            }
        } catch (Exception e) {
            warning(() -> "Unable to initialize logging:\n" + e.getLocalizedMessage());
        }
    }

    public static void severe(Message message) {
        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.severe(message.getMessage());
        }
    }

    public static void warning(Message message) {
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, message.getMessage());
        }
    }

    public static void info(Message message) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(message.getMessage());
        }
    }

    public static void fine(Message message) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(message.getMessage());
        }
    }

    public static void finer(Message message) {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(message.getMessage());
        }
    }

    public interface Message {
        String getMessage();
    }
}