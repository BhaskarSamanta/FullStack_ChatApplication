package bhaskar.org.chat_application.exceptions;

public class EmailNotVerifiedException extends RuntimeException{
    public EmailNotVerifiedException(String message){
        super(message);
    }
}
