package io.github.moyusowo.neoartisan.util;

import java.util.logging.Logger;

public class Todos {
    private static final Logger LOGGER = Logger.getLogger("TODO");

    public enum Priority {
        CRITICAL, MEDIUM, LOW
    }

    public static void fail(String task, Priority priority) {
        String msg = String.format("[%s] %s", priority, task);
        if (priority == Priority.CRITICAL) {
            throw new UnsupportedOperationException("🚨 " + msg);
        } else if (priority == Priority.MEDIUM) {
            LOGGER.severe(msg);
        } else if (priority == Priority.LOW) {
            LOGGER.warning(msg);
        }
    }
}
