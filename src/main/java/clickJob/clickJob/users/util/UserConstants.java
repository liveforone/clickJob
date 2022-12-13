package clickJob.clickJob.users.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserConstants {

    PASSWORD_MATCH(1),
    PASSWORD_NOT_MATCH(0),
    DUPLICATE(0),
    NOT_DUPLICATE(1);

    private int value;
}
