package clickJob.clickJob.apply.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyUserResponse {

    private String company;

    @Builder
    public ApplyUserResponse(String company) {
        this.company = company;
    }
}
