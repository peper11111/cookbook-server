package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class SearchDto {

    private String type;
    private Long id;
    private Long avatarId;
    private String header;
    private String caption;
}
