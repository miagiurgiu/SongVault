package model.service;

import entity.AiTrainingPolicy;
import entity.Song;
import entity.SongStatus;
import model.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final SongRepository songRepository;

    public SearchService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> search(
            String title,
            String artist,
            String genre,
            AiTrainingPolicy aiTrainingPolicy,
            SongStatus songStatus,
            Integer minimumReleaseYear
    ) {
        List<Song> songs = songRepository.findAll();
        List<Song> result = new ArrayList<>();

        for (Song song : songs) {

            if (title != null &&
                    !song.getTitle().toLowerCase().contains(title.toLowerCase())) {
                continue;
            }

            if (artist != null &&
                    !song.getArtist().toLowerCase().contains(artist.toLowerCase())) {
                continue;
            }

            if (genre != null &&
                    !song.getGenre().toLowerCase().contains(genre.toLowerCase())) {
                continue;
            }

            if (aiTrainingPolicy != null &&
                    song.getAiTrainingPolicy() != aiTrainingPolicy) {
                continue;
            }

            if (songStatus != null &&
                    song.getSongStatus() != songStatus) {
                continue;
            }

            if (minimumReleaseYear != null &&
                    song.getReleaseYear() < minimumReleaseYear) {
                continue;
            }

            result.add(song);
        }

        return result;
    }
}