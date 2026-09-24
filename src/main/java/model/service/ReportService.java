package model.service;

import entity.AiTrainingPolicy;
import entity.Song;
import entity.SongStatus;
import model.repository.SongRepository;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final SongRepository songRepository;

    public ReportService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public String generateDashboard() {
        List<Song> songs = songRepository.findAll();

        int total = songs.size();
        int stored = 0;
        int checkedOut = 0;
        int archived = 0;
        int storedDuration = 0;
        int occupiedLocations = 0;

        Map<AiTrainingPolicy, Integer> policyCounts =
                new EnumMap<>(AiTrainingPolicy.class);

        Map<String, Integer> genreCounts =
                new HashMap<>();

        for (Song song : songs) {

            if (song.getSongStatus() == SongStatus.IN_STORAGE) {
                stored++;
                storedDuration += song.getDurationInSeconds();
                occupiedLocations++;
            }

            if (song.getSongStatus() == SongStatus.CHECKED_OUT) {
                checkedOut++;
            }

            if (song.getSongStatus() == SongStatus.ARCHIVED) {
                archived++;
            }

            policyCounts.merge(
                    song.getAiTrainingPolicy(),
                    1,
                    Integer::sum
            );

            genreCounts.merge(
                    song.getGenre(),
                    1,
                    Integer::sum
            );
        }

        return """
                ===== DASHBOARD =====
                Total songs: %d
                Stored: %d
                Checked out: %d
                Archived: %d
                Stored duration: %d seconds
                Occupied locations: %d

                Counts by AI training policy:
                %s

                Counts by genre:
                %s
                """.formatted(
                total,
                stored,
                checkedOut,
                archived,
                storedDuration,
                occupiedLocations,
                policyCounts,
                genreCounts
        );
    }
}