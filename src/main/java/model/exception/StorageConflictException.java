package model.exception;

public class StorageConflictException extends RuntimeException{
    public StorageConflictException(String message) {
        super(message);
    }
}
