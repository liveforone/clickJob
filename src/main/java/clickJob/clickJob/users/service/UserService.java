package clickJob.clickJob.users.service;

import clickJob.clickJob.users.dto.UserRequest;
import clickJob.clickJob.users.dto.UserResponse;
import clickJob.clickJob.users.model.Role;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import clickJob.clickJob.users.util.UserConstants;
import clickJob.clickJob.users.util.UserMapper;
import clickJob.clickJob.users.util.UserUtils;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
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

    /*
    * 이메일 중복 검증
     */
    public int checkDuplicateEmail(String email) {
        Users users = userRepository.findByEmail(email);

        if (CommonUtils.isNull(users)) {
            return UserConstants.NOT_DUPLICATE.getValue();
        }
        return UserConstants.DUPLICATE.getValue();
    }

    /*
    * 닉네임 중복 검증
     */
    public int checkDuplicateNickname(String nickname) {
        Users users = userRepository.findByNickname(nickname);

        if (CommonUtils.isNull(users)) {
            return UserConstants.NOT_DUPLICATE.getValue();
        }
        return UserConstants.DUPLICATE.getValue();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (users.getAuth() == Role.ADMIN) {  //어드민 아이디 지정됨, 비밀번호는 회원가입해야함
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));

        return new User(
                users.getEmail(),
                users.getPassword(),
                authorities
        );
    }

    public Users getUserEntity(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse getUserByEmail(String email) {
        return UserMapper.entityToDtoDetail(
                userRepository.findByEmail(email)
        );
    }

    public List<UserResponse> getUserListByNickName(String nickname) {
        return UserMapper.entityToDtoList(
                userRepository.searchByNickName(nickname)
        );
    }

    /*
    * 전체 유저 반환
    * when : admin page
     */
    public List<Users> getAllUsersForAdmin() {
        return userRepository.findAll();
    }

    public UserResponse getUserByNickname(String nickname) {
        return UserMapper.entityToDtoDetail(
                userRepository.findByNickname(nickname)
        );
    }

    @Transactional
    public void joinUser(UserRequest userRequest) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRequest.setAuth(Role.MEMBER);  //기본 권한 매핑
        userRequest.setNickname(UserUtils.makeRandomNickname());  //무작위 닉네임 생성

        userRepository.save(
                UserMapper.dtoToEntity(userRequest)
        );
    }

    @Transactional
    public void login(UserRequest userRequest, HttpSession httpSession)
            throws UsernameNotFoundException
    {

        String email = userRequest.getEmail();
        String password = userRequest.getPassword();
        Users user = userRepository.findByEmail(email);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(token);
        httpSession.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        /*
        * 처음 어드민이 로그인을 하는경우 이메일로 판별해서 권한을 admin 으로 변경해주고
        * 그 다음부터 어드민이 업데이트 할때에는 auth 칼럼으로 판별해서 db 업데이트 하지않고,
        * GrantedAuthority 만 업데이트 해준다.
         */
        if (user.getAuth() != Role.ADMIN && ("admin@breve.com").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            userRepository.updateAuth(Role.ADMIN, userRequest.getEmail());
        }

        if (user.getAuth() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));

        new User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
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
