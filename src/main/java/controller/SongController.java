package controller;

import entity.AiTrainingPolicy;
import entity.Song;
import entity.StorageLocation;
import infrastructure.AuditLogger;
import infrastructure.IdGenerator;
import model.service.ArchiveService;
import model.service.ReportService;
import model.service.SearchService;
import view.ConsoleView;

public class SongController {

    private final ConsoleView view;
    private final ArchiveService archiveService;
    private final SearchService searchService;
    private final ReportService reportService;
    private final IdGenerator idGenerator;
    private final AuditLogger auditLogger;

    public SongController(
            ConsoleView view,
            ArchiveService archiveService,
            SearchService searchService,
            ReportService reportService,
            IdGenerator idGenerator,
            AuditLogger auditLogger
    ) {
        this.view = view;
        this.archiveService = archiveService;
        this.searchService = searchService;
        this.reportService = reportService;
        this.idGenerator = idGenerator;
        this.auditLogger = auditLogger;
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.showMenu();
            int choice = view.readInt("");
            try {
                switch (choice) {
                    case 1 -> registerSong();
                    case 2 -> view.showSongs(archiveService.getAllSongs());
                    case 3 -> searchSongs();
                    case 4 -> checkIn();
                    case 5 -> checkOut();
                    case 6 -> moveSong();
                    case 7 -> archive();
                    case 8 -> inspectSong();
                    case 9 -> view.showMessage(reportService.generateDashboard());
                    case 0 -> {
                        running = false;
                        view.showMessage("Exiting Application.");
                    }
                    default -> view.showMessage("Invalid choice, please select 0-9.");
                }
            } catch (Exception e) {
                view.showMessage("ERROR: " + e.getMessage());
            }
        }
    }

    private void registerSong() {
        String id = idGenerator.generate();
        String title = view.readLine("Title: ");
        String artist = view.readLine("Artist: ");
        String album = view.readLine("Album: ");
        String genre = view.readLine("Genre: ");
        int year = view.readInt("Release year: ");
        int duration = view.readInt("Duration in seconds: ");
        String creator = view.readLine("Creator: ");
        AiTrainingPolicy policy = view.readPolicy();
        String fingerprint = view.readLine("Fingerprint: ");
        String notes = view.readLine("Notes: ");

        Song song = new Song(artist, id, title, album, genre, year, duration, creator, policy, fingerprint, notes);
        archiveService.registerSong(song);
        auditLogger.log("REGISTER", id, "Title: " + title);
        view.showMessage("Successfully registered song with ID: " + id);
    }

    private void searchSongs() {
        view.showMessage("--- Search Songs (Press ENTER to skip a field) ---");
        String title = view.readLine("Title filter: ");
        title = title.isBlank() ? null : title;

        String artist = view.readLine("Artist filter: ");
        artist = artist.isBlank() ? null : artist;

        String genre = view.readLine("Genre filter: ");
        genre = genre.isBlank() ? null : genre;

        String minYearStr = view.readLine("Min release year filter: ");
        Integer minYear = minYearStr.isBlank() ? null : Integer.parseInt(minYearStr);

        var results = searchService.search(title, artist, genre, null, null, minYear);
        view.showSongs(results);
    }

    private void checkIn() {
        String id = view.readLine("Song ID: ");
        StorageLocation loc = view.readLocation();
        archiveService.checkIn(id, loc);
        auditLogger.log("CHECK_IN", id, "Location: " + loc.display());
        view.showMessage("Checked in successfully.");
    }

    private void checkOut() {
        String id = view.readLine("Song ID: ");
        archiveService.checkOut(id);
        auditLogger.log("CHECK_OUT", id, "Checked out");
        view.showMessage("Checked out successfully.");
    }

    private void moveSong() {
        String id = view.readLine("Song ID: ");
        StorageLocation loc = view.readLocation();
        archiveService.moveSong(id, loc);
        auditLogger.log("MOVE", id, "New Location: " + loc.display());
        view.showMessage("Moved successfully.");
    }

    private void archive() {
        String id = view.readLine("Song ID: ");
        archiveService.archiveSong(id);
        auditLogger.log("ARCHIVE", id, "Archived");
        view.showMessage("Archived successfully.");
    }

    private void inspectSong() {
        String id = view.readLine("Song ID: ");
        Song song = archiveService.getSong(id);
        view.showSong(song);
    }
}