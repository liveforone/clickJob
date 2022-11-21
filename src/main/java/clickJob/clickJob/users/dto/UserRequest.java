package clickJob.clickJob.users.dto;

import clickJob.clickJob.users.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    private Long id;
    private String email;
    private String password;
    private Role auth;
    private String nickname;
}
