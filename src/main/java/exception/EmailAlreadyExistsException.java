package exception;

public class EmailAlreadyExistsException extends Exception {

    private String message;

    public EmailAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
