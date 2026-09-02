package bhaskar.org.chat_application.exceptions;

public class InvalidCredentialException extends RuntimeException{
    public InvalidCredentialException(String message){
        super(message);
    }
}
