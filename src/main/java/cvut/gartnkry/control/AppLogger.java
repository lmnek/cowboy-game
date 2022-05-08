package cvut.gartnkry.control;

import java.util.logging.*;

import static cvut.gartnkry.control.Settings.*;

public class AppLogger {
    private static final Logger LOGGER = Logger.getGlobal();

    public static void init() {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(logLevel);
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT.%1$tL %4$s %5$s%6$s%n");

            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(consoleLogLevel);
            LOGGER.addHandler(handler);

            FileHandler fileHandler = new FileHandler("log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(fileLogLevel);
            LOGGER.addHandler(fileHandler);
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