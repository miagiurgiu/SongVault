package model.service;

import entity.Song;
import entity.StorageLocation;
import model.exception.SongNotFoundException;
import model.exception.StorageConflictException;
import model.repository.SongRepository;

import java.util.List;

public class ArchiveService {

    private final SongRepository songRepository;

    public ArchiveService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song registerSong(Song song) {
        if (songRepository.existsByFingerprint(song.getFingerprint())) {
            throw new StorageConflictException(
                    "A song with this fingerprint already exists."
            );
        }

        return songRepository.save(song);
    }

    public void checkIn(String id, StorageLocation location) {
        Song song = getSong(id);

        if (songRepository.findLocationIsUsed(location.key(), id)) {
            throw new StorageConflictException(
                    "The storage location is already occupied."
            );
        }

        song.checkIn(location);
        songRepository.save(song);
    }

    public void checkOut(String id) {
        Song song = getSong(id);

        song.checkOut();
        songRepository.save(song);
    }

    public void moveSong(String id, StorageLocation location) {
        Song song = getSong(id);

        if (songRepository.findLocationIsUsed(location.key(), id)) {
            throw new StorageConflictException(
                    "The storage location is already occupied."
            );
        }

        song.moveSong(location);
        songRepository.save(song);
    }

    public void archiveSong(String id) {
        Song song = getSong(id);

        song.archiveSong();
        songRepository.save(song);
    }

    public Song getSong(String id) {
        return songRepository.findById(id)
                .orElseThrow(() ->
                        new SongNotFoundException(
                                "Song not found: " + id
                        )
                );
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
}