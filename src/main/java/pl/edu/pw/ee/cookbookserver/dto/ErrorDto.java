package pl.edu.pw.ee.cookbookserver.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ErrorDto {

    private int code;
    private String message;

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
