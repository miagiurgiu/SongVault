package model.repository;

import entity.Song;

import java.util.List;
import java.util.Optional;

public interface SongRepository {
    Song save(Song song);
    Optional<Song> findById(String id);
    List<Song> findAll();
    boolean findLocationIsUsed(String locationKey, String songId);
}
