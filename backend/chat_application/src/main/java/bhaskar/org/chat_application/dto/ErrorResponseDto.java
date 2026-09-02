package bhaskar.org.chat_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String message;
    private LocalDateTime timeStamp;
    private Map<String,String> errors;
}
