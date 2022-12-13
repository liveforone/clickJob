package clickJob.clickJob.users.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserUtils {

    /*
    * 무작위 닉네임 생성
    * 반환 타입 : 숫자 + 문자인 String
     */
    public static String makeRandomNickname() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /*
    * 비밀번호 복호화
     */
    public static int checkPasswordMatching(String inputPassword, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(inputPassword, password)) {
            return UserConstants.PASSWORD_MATCH.getValue();
        }
        return UserConstants.PASSWORD_NOT_MATCH.getValue();
    }
}
