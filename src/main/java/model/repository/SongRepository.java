package model.repository;

import entity.Song;

import java.util.List;
import java.util.Optional;

public interface SongRepository {
    Song save(Song song);
    Optional<Song> findById(String id);
    List<Song> findAll();
    boolean findLocationIsUsed(String locationKey, String songId);
    // check if a fingerprint exists
    boolean existsByFingerprint(String fingerprint);
    // check if a location is occupied
    //boolean findLocationIsUsed(String locationKey, String excludeSongId)
;}