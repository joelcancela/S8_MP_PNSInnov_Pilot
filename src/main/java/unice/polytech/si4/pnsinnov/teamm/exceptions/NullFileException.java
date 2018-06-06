package unice.polytech.si4.pnsinnov.teamm.exceptions;

public class NullFileException extends Exception {

    public NullFileException() {
        super("Null file");
    }

    public NullFileException(String message) {
        super(message);
    }
}
