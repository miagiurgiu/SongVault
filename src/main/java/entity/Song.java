package entity;

import java.util.Objects;

public class Song {
    private final String id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int releaseYear;
    private int durationInSeconds;
    private String creator;
    private AiTrainingPolicy aiTrainingPolicy;
    private String fingerprint;
    private String notes;

    private SongStatus songStatus;
    private StorageLocation storageLocation;

    public Song(String artist, String id, String title, String album, String genre, int releaseYear, int durationInSeconds, String creator, AiTrainingPolicy aiTrainingPolicy, String fingerprint, String notes) {
        this.artist = artist;
        this.id = id;
        this.title = title;
        this.album = album;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.durationInSeconds = durationInSeconds;
        this.creator = creator;
        this.aiTrainingPolicy = aiTrainingPolicy;
        this.fingerprint = fingerprint;
        this.notes = notes;
    }

    public void checkIn(StorageLocation storageLocation){
        requiredStatus(SongStatus.REGISTERED,SongStatus.CHECKED_OUT);
        this.storageLocation= Objects.requireNonNull(storageLocation);
        this.songStatus=SongStatus.IN_STORAGE;
    }

    public void checkOut(){
        requiredStatus(SongStatus.IN_STORAGE);
        this.storageLocation=null;
        this.songStatus=SongStatus.CHECKED_OUT;
    }

    public void moveSong(StorageLocation storageLocation){
        requiredStatus(SongStatus.IN_STORAGE);
        this.storageLocation= Objects.requireNonNull(storageLocation);
    }

    public void archiveSong(){
        if(songStatus==SongStatus.ARCHIVED){
            throw new IllegalArgumentException("Song already archived");
        }
        this.storageLocation=null;
        this.songStatus=SongStatus.ARCHIVED;
    }

    private void requiredStatus(SongStatus... allowedStatuses){
        for(SongStatus allowed: allowedStatuses){
            if(allowed==songStatus){
                return;
            }
        }
        throw new IllegalArgumentException("Invalid operation with this status "+songStatus);
    }

    private static String requiredText(String value, String field){
        if(value==null || value.isBlank()){
            throw new IllegalArgumentException("Value cannot be empty"+value);
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public AiTrainingPolicy getAiTrainingPolicy() {
        return aiTrainingPolicy;
    }

    public void setAiTrainingPolicy(AiTrainingPolicy aiTrainingPolicy) {
        this.aiTrainingPolicy = aiTrainingPolicy;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void restoreState(
            SongStatus status,
            StorageLocation location
    ) {

        this.songStatus =
                Objects.requireNonNull(status);

        this.storageLocation = location;
    }

    @Override
    public String toString() {

        return "%s | %s — %s | %s | %d | %s | %s"
                .formatted(
                        id,
                        artist,
                        title,
                        genre,
                        releaseYear,
                        songStatus,
                        aiTrainingPolicy
                );
    }
}
