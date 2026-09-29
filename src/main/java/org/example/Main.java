package org.example;
import java.nio.file.Path;

import controller.SongController;
import model.repository.FileRepository;
import model.repository.SongRepository;
import model.service.ArchiveService;
import model.service.ReportService;
import model.service.SearchService;
import infrastructure.AuditLogger;
import infrastructure.IdGenerator;
import view.ConsoleView;

public class Main{
    public static void main(String[] args){
        Path dataDir=Path.of("data");
        Path songsFile=dataDir.resolve("songs.tsv");
        Path auditFile=dataDir.resolve("audit.log");
        SongRepository repository=new FileRepository(songsFile);
        AuditLogger auditLogger=new AuditLogger(auditFile);
        IdGenerator idGenerator= new IdGenerator();
        ArchiveService archiveService=new ArchiveService(repository);
        SearchService searchService=new SearchService(repository);
        ReportService reportService=new ReportService(repository);
        ConsoleView view=new ConsoleView();
        SongController controller=new SongController(view,archiveService,searchService,reportService,idGenerator,auditLogger);
        controller.run();
    }
}