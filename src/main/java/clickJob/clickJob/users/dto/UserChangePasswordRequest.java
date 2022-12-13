package clickJob.clickJob.users.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
}
