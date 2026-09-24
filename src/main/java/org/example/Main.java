package org.example;

import controller.SongController;
import infrastructure.AuditLogger;
import infrastructure.IdGenerator;
import model.repository.FileRepository;
import model.repository.SongRepository;
import model.service.ArchiveService;
import model.service.ReportService;
import model.service.SearchService;
import view.ConsoleView;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Path songsFile = Path.of("data", "songs.tsv");
        Path auditFile = Path.of("data", "audit.log");

        SongRepository songRepository = new FileRepository(songsFile);
        ArchiveService archiveService = new ArchiveService(songRepository);
        SearchService searchService = new SearchService(songRepository);
        ReportService reportService = new ReportService(songRepository);

        ConsoleView view = new ConsoleView();
        IdGenerator idGenerator = new IdGenerator();
        AuditLogger auditLogger = new AuditLogger(auditFile);

        SongController controller = new SongController(
                view,
                archiveService,
                searchService,
                reportService,
                idGenerator,
                auditLogger
        );

        controller.run();
    }
}