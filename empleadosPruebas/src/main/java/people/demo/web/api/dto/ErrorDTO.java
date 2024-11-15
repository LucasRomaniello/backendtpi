package people.demo.web.api.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ErrorDTO {
    public String message;
}
