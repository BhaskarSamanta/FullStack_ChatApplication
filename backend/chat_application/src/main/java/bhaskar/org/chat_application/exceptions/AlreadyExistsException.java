package bhaskar.org.chat_application.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message){
        super(message);
    }
}
