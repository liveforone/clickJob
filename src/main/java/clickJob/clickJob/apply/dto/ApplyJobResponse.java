package clickJob.clickJob.apply.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyJobResponse {

    private String name;

    @Builder
    public ApplyJobResponse(String name) {
        this.name = name;
    }
}
