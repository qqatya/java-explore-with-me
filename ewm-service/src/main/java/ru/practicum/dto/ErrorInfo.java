package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ErrorInfo {

    private String status;

    private String reason;

    private String message;

    private String timestamp;

    public ErrorInfo(String status, String reason, String message, LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = formatter.format(timestamp);
    }
}
