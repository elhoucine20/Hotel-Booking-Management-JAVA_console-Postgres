package exception;

public class RoomNotAvailableException extends Exception{

    private String message;

    public RoomNotAvailableException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
