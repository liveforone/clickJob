package clickJob.clickJob.users.service;

import clickJob.clickJob.users.dto.UserRequest;
import clickJob.clickJob.users.dto.UserResponse;
import clickJob.clickJob.users.model.Role;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //== entity -> dto1 - detail ==//
    public UserResponse entityToDtoDetail(Users users) {

        if (users != null) {
            return UserResponse.builder()
                    .id(users.getId())
                    .email(users.getEmail())
                    .auth(users.getAuth())
                    .nickname(users.getNickname())
                    .build();
        } else {
            return null;
        }
    }

    //== entity -> dto2 - list ==//
    public List<UserResponse> entityToDtoList(List<Users> usersList) {
        List<UserResponse> dto = new ArrayList<>();

        for (Users users : usersList) {
            UserResponse userResponse = UserResponse.builder()
                    .id(users.getId())
                    .email(users.getEmail())
                    .auth(users.getAuth())
                    .nickname(users.getNickname())
                    .build();
            dto.add(userResponse);
        }

        return dto;
    }

    //== 무작위 닉네임 생성 - 숫자 + 문자 ==//
    public String makeRandomNickname() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    //== 이메일 중복 검증 ==//
    @Transactional(readOnly = true)
    public int checkSameEmail(String email) {
        Users users = userRepository.findByEmail(email);

        if (users == null) {
            return 1;
        } else {
            return 0;
        }
    }

    //== 닉네임 중복 검증 ==//
    @Transactional(readOnly = true)
    public int checkSameNickname(String nickname) {
        Users users = userRepository.findByNickname(nickname);

        if (users == null) {
            return 1;
        } else {
            return 0;
        }
    }

    //== 비밀번호 복호화 ==//
    public int passwordDecode(String inputPassword, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(encoder.matches(inputPassword, password)) {
            return 1;
        } else {
            return 0;
        }
    }

    //== spring context 반환 메소드(필수) ==//
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (users.getAuth() == Role.ADMIN) {  //어드민 아이디 지정됨, 비밀번호는 회원가입해야함
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(users.getEmail(), users.getPassword(), authorities);
    }

    //== 유저 엔티티 반환 ==//
    public Users getUserEntity(String email) {
        return userRepository.findByEmail(email);
    }

    //== 유저 responsedto 반환 ==//
    public UserResponse getUserByEmail(String email) {
        return entityToDtoDetail(userRepository.findByEmail(email));
    }

    public List<UserResponse> getUserListByNickName(String nickname) {
        return entityToDtoList(userRepository.findSearchByNickName(nickname));
    }

    //== 전체 유저 리턴 for admin ==//
    public List<Users> getAllUsersForAdmin() {
        return userRepository.findAll();
    }

    public UserResponse getUserByNickname(String nickname) {
        return entityToDtoDetail(userRepository.findByNickname(nickname));
    }

    //== 회원 가입 로직 ==//
    @Transactional
    public Long joinUser(UserRequest userRequest) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRequest.setAuth(Role.MEMBER);  //기본 권한 매핑
        userRequest.setNickname(makeRandomNickname());  //무작위 닉네임 생성

        return userRepository.save(userRequest.toEntity()).getId();
    }

    //== 로그인 - 세션과 컨텍스트홀더 사용 ==//
    @Transactional
    public void login(UserRequest userRequest, HttpSession httpSession) throws UsernameNotFoundException {

        String email = userRequest.getEmail();
        String password = userRequest.getPassword();
        Users user = userRepository.findByEmail(email);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(token);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        List<GrantedAuthority> authorities = new ArrayList<>();
        /*
        처음 어드민이 로그인을 하는경우 이메일로 판별해서 권한을 admin으로 변경해주고
        그 다음부터 어드민이 업데이트 할때에는 auth 칼럼으로 판별해서 db 업데이트 하지않고,
        grandtedauthority 만 업데이트 해준다.
         */
        if (user.getAuth() != Role.ADMIN && ("admin@breve.com").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            userRepository.updateAuth(Role.ADMIN, userRequest.getEmail());
        } else if (user.getAuth() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        new User(user.getEmail(), user.getPassword(), authorities);
    }

    @Transactional
    public void updateNickname(String nickname, String email) {
        userRepository.updateNickname(nickname, email);
    }

    @Transactional
    public void updateEmail(String oldEmail, String newEmail) {
        userRepository.updateEmail(oldEmail, newEmail);
    }

    @Transactional
    public void updatePassword(Long id, String inputPassword) {
        //pw 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword =  passwordEncoder.encode(inputPassword);
        
        userRepository.updatePassword(id, newPassword);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}