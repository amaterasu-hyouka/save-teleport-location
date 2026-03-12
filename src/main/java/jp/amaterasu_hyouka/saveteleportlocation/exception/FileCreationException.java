package jp.amaterasu_hyouka.saveteleportlocation.exception;

public class FileCreationException extends Exception {

    public FileCreationException() {
        super();
    }

    public FileCreationException(String message) {
        super(message);
    }

    public FileCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCreationException(Throwable cause) {
        super(cause);
    }

}
