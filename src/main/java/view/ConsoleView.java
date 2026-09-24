package view;

import entity.AiTrainingPolicy;
import entity.Song;
import entity.SongStatus;
import entity.StorageLocation;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println();
        System.out.println("===== PROTECTED SONG WAREHOUSE =====");
        System.out.println("1. Register song");
        System.out.println("2. List all songs");
        System.out.println("3. Search songs");
        System.out.println("4. Check in");
        System.out.println("5. Check out");
        System.out.println("6. Move stored song");
        System.out.println("7. Archive song");
        System.out.println("8. Inspect song");
        System.out.println("9. Dashboard report");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    public String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(readLine(message));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public AiTrainingPolicy readPolicy() {
        while (true) {
            try {
                return AiTrainingPolicy.valueOf(
                        readLine(
                                "AI training policy " +
                                        "(ALLOWED, ALLOWED_WITH_ATTRIBUTION, PROHIBITED, UNKNOWN): "
                        ).trim().toUpperCase()
                );
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid AI training policy.");
            }
        }
    }

    public SongStatus readStatus() {
        while (true) {
            try {
                return SongStatus.valueOf(
                        readLine(
                                "Status " +
                                        "(REGISTERED, IN_STORAGE, CHECKED_OUT, ARCHIVED): "
                        ).trim().toUpperCase()
                );
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status.");
            }
        }
    }

    public StorageLocation readLocation() {
        String zone = readLine("Zone: ");
        int shelf = readInt("Shelf: ");
        int slot = readInt("Slot: ");

        return new StorageLocation(zone, shelf, slot);
    }

    public void showSongs(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        for (Song song : songs) {
            System.out.println(song);
        }
    }

    public void showSong(Song song) {
        System.out.println();
        System.out.println("===== SONG =====");
        System.out.println("ID: " + song.getId());
        System.out.println("Title: " + song.getTitle());
        System.out.println("Artist: " + song.getArtist());
        System.out.println("Album: " + song.getAlbum());
        System.out.println("Genre: " + song.getGenre());
        System.out.println("Release year: " + song.getReleaseYear());
        System.out.println("Duration: " + song.getDurationInSeconds());
        System.out.println("Creator: " + song.getCreator());
        System.out.println("AI training policy: " + song.getAiTrainingPolicy());
        System.out.println("Fingerprint: " + song.getFingerprint());
        System.out.println("Notes: " + song.getNotes());
        System.out.println("Status: " + song.getSongStatus());
        System.out.println(
                "Location: " +
                        (song.getStorageLocation() == null
                                ? "none"
                                : song.getStorageLocation().display())
        );
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}