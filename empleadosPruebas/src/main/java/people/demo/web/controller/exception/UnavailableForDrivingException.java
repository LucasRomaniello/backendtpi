package people.demo.web.controller.exception;

    public class UnavailableForDrivingException extends RuntimeException {
    public UnavailableForDrivingException(String message) {
        super(message);
    }
}
