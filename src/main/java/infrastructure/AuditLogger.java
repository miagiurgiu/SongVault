package infrastructure;

import model.exception.PersistanceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AuditLogger {

    private final Logger logger = Logger.getLogger("SongWarehouseAudit");

    public AuditLogger(Path logFile) {
        try {
            if (logFile.getParent() != null) {
                Files.createDirectories(logFile.getParent());
            }
            FileHandler handler = new FileHandler(logFile.toString(), true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new PersistanceException("Failed to initialize audit log", e);
        }
    }

    public void log(String operation, String songId, String details) {
        logger.log(Level.INFO, "[{0}] Song ID: {1} | Details: {2}", new Object[]{operation, songId, details});
    }
}