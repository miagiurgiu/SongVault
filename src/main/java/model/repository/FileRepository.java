package model.repository;

import entity.AiTrainingPolicy;
import entity.Song;
import entity.SongStatus;
import entity.StorageLocation;
import model.exception.PersistanceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileRepository implements SongRepository {

    private final Path filePath;
    private final Map<String, Song> songMap = new LinkedHashMap<>();

    public FileRepository(Path filePath) {
        this.filePath = filePath;
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            loadFromFile();
        } catch (IOException e) {
            throw new PersistanceException("Failed to initialize file storage.", e);
        }
    }

    @Override
    public Song save(Song song) {
        songMap.put(song.getId(), song);
        saveToFile();
        return song;
    }

    @Override
    public Optional<Song> findById(String id) {
        return Optional.ofNullable(songMap.get(id));
    }

    @Override
    public List<Song> findAll() {
        return new ArrayList<>(songMap.values());
    }

    @Override
    public boolean existsByFingerprint(String fingerprint) {
        if (fingerprint == null) return false;
        return songMap.values().stream()
                .anyMatch(song -> fingerprint.equalsIgnoreCase(song.getFingerprint()));
    }

    @Override
    public boolean findLocationIsUsed(String locationKey, String excludeSongId) {
        if (locationKey == null) return false;
        return songMap.values().stream()
                .filter(song -> !song.getId().equals(excludeSongId))
                .filter(song -> song.getSongStatus() == SongStatus.IN_STORAGE)
                .filter(song -> song.getStorageLocation() != null)
                .anyMatch(song -> locationKey.equals(song.getStorageLocation().key()));
    }

    private void loadFromFile() throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] parts = line.split("\t", -1);
            if (parts.length < 13) continue;

            Song song = new Song(
                    parts[2], // artist
                    parts[0], // id
                    parts[1], // title
                    parts[3], // album
                    parts[4], // genre
                    Integer.parseInt(parts[5]), // releaseYear
                    Integer.parseInt(parts[6]), // duration
                    parts[7], // creator
                    AiTrainingPolicy.valueOf(parts[8]),
                    parts[9], // fingerprint
                    parts[12].equals("-") ? "" : parts[12] // notes
            );

            SongStatus status = SongStatus.valueOf(parts[10]);
            StorageLocation location = null;

            if (!parts[11].equals("-")) {
                String[] locParts = parts[11].split(":");
                location = new StorageLocation(locParts[0], Integer.parseInt(locParts[1]), Integer.parseInt(locParts[2]));
            }

            song.restoreState(status, location);
            songMap.put(song.getId(), song);
        }
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Song s : songMap.values()) {
            String locStr = s.getStorageLocation() != null ? s.getStorageLocation().key() : "-";
            String notesStr = s.getNotes() != null && !s.getNotes().isBlank() ? s.getNotes() : "-";

            String line = String.join("\t",
                    s.getId(),
                    s.getTitle(),
                    s.getArtist(),
                    s.getAlbum(),
                    s.getGenre(),
                    String.valueOf(s.getReleaseYear()),
                    String.valueOf(s.getDurationInSeconds()),
                    s.getCreator(),
                    s.getAiTrainingPolicy().name(),
                    s.getFingerprint(),
                    s.getSongStatus().name(),
                    locStr,
                    notesStr
            );
            lines.add(line);
        }

        try {
            Files.write(filePath, lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new PersistanceException("Failed to persist data to file.", e);
        }
    }
}