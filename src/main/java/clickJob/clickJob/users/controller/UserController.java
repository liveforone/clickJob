package clickJob.clickJob.users.controller;

import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.service.BoardService;
import clickJob.clickJob.users.dto.UserChangeEmailRequest;
import clickJob.clickJob.users.dto.UserChangePasswordRequest;
import clickJob.clickJob.users.dto.UserRequest;
import clickJob.clickJob.users.dto.UserResponse;
import clickJob.clickJob.users.model.Role;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.service.UserService;
import clickJob.clickJob.users.util.UserConstants;
import clickJob.clickJob.users.util.UserUtils;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("home");
    }

    @GetMapping("/user/signup")
    public ResponseEntity<?> signupPage() {
        return ResponseEntity.ok("회원가입페이지");
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest userRequest) {
        int checkEmail = userService.checkDuplicateEmail(userRequest.getEmail());

        if (checkEmail != UserConstants.NOT_DUPLICATE.getValue()) {  //이메일 중복 check
            return ResponseEntity
                    .ok("중복되는 이메일이 있어 회원가입이 불가능합니다.");
        }

        String url = "/";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        userService.joinUser(userRequest);
        log.info("회원 가입 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/user/login")
    public ResponseEntity<?> loginPage() {
        return ResponseEntity.ok("로그인 페이지");
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginPage(
            @RequestBody UserRequest userRequest,
            HttpSession session
    ) {
        Users users = userService.getUserEntity(userRequest.getEmail());

        if (CommonUtils.isNull(users)) {  //회원 존재 check
            return ResponseEntity.ok("해당 이메일의 회원은 존재하지 않습니다.");
        }

        int checkPassword = UserUtils.checkPasswordMatching(
                userRequest.getPassword(),
                users.getPassword()
        );

        if (checkPassword != UserConstants.PASSWORD_MATCH.getValue()) {  //PW check
            return ResponseEntity.ok("비밀번호가 일치하지 않습니다.");
        }

        String url = "/";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        userService.login(
                userRequest,
                session
        );
        log.info("로그인 성공!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    /*
    * 로그아웃은 시큐리티 단에서 이루어짐.
    * url : /user/logout
    * method : POST
     */

    @GetMapping("/user/prohibition")
    public ResponseEntity<?> prohibition() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("접근 권한이 없습니다.");
    }

    @GetMapping("/user/my-page")
    public ResponseEntity<?> myPage(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            Principal principal
    ) {
        UserResponse users = userService.getUserByEmail(principal.getName());

        if (CommonUtils.isNull(users)) {
            return ResponseEntity.ok("해당 유저가 없어 조회할 수 없습니다.");
        }

        Map<String, Object> map = new HashMap<>();
        Page<BoardResponse> board = boardService.getBoardByEmail(
                principal.getName(),
                pageable
        );

        map.put("users", users);
        map.put("board", board);

        return ResponseEntity.ok(map);
    }

    /*
    * profile
    * who : 다른 사람이 보는 내 프로필
     */
    @GetMapping("/user/profile/{nickname}")
    public ResponseEntity<?> ProfilePage(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @PathVariable("nickname") String nickname
    ) {
        UserResponse users = userService.getUserByNickname(nickname);

        if (CommonUtils.isNull(users)) {
            return ResponseEntity.ok("해당 유저가 없어 조회할 수 없습니다.");
        }

        Map<String, Object> map = new HashMap<>();
        Page<BoardResponse> board =
                boardService.getBoardByNickname(nickname, pageable);

        map.put("users", users);
        map.put("board", board);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/user/nickname-post")
    public ResponseEntity<?> nicknamePost(
            @RequestBody String nickname,
            Principal principal
    ) {
        int checkNickname = userService.checkDuplicateNickname(nickname);

        if (checkNickname != UserConstants.NOT_DUPLICATE.getValue()) {  //이메일 중복 check
            return ResponseEntity
                    .ok("중복되는 닉네임이 있어 수정 불가능합니다.");
        }

        String url = "/user/my-page";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        userService.updateNickname(
                nickname,
                principal.getName()
        );
        log.info("닉네임 수정 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/user/search")
    public ResponseEntity<?> searchPage(
            @RequestParam("nickname") String nickname
    ) {
        List<UserResponse> userList = userService.getUserListByNickName(nickname);

        if (CommonUtils.isNull(userList)) {
            return ResponseEntity.ok("해당 닉네임의 유저가 존재하지 않습니다.");
        }

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin(Principal principal) {
        Users users = userService.getUserEntity(principal.getName());

        if (!users.getAuth().equals(Role.ADMIN)) {  //권한 검증 - auth = admin check
            log.info("어드민 페이지 접속에 실패했습니다.");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        log.info("어드민이 어드민 페이지에 접속했습니다.");
        return ResponseEntity.ok(userService.getAllUsersForAdmin());
    }

    @PostMapping("/user/change-email")
    public ResponseEntity<?> changeEmail(
            @RequestBody UserChangeEmailRequest userRequest,
            Principal principal
    ) {
        Users users = userService.getUserEntity(principal.getName());
        UserResponse changeEmail = userService.getUserByEmail(userRequest.getEmail());

        if (CommonUtils.isNull(users)) {
            return ResponseEntity.ok("해당 유저를 조회할 수 없어 이메일 변경이 불가능합니다.");
        }

        if (!CommonUtils.isNull(changeEmail)) {  //이메일 중복 check
            return ResponseEntity.ok("해당 이메일이 이미 존재합니다. 다시 입력해주세요");
        }

        int checkPassword = UserUtils.checkPasswordMatching(
                userRequest.getPassword(),
                users.getPassword()
        );

        if (checkPassword != UserConstants.PASSWORD_MATCH.getValue()) {
            log.info("비밀번호 일치하지 않음.");
            return ResponseEntity.ok("비밀번호가 다릅니다. 다시 입력해주세요.");
        }

        String url = "/user/logout";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        userService.updateEmail(
                principal.getName(),
                userRequest.getEmail()
        );
        log.info("이메일 변경 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePasswordRequest userRequest,
            Principal principal
    ) {
        Users users = userService.getUserEntity(principal.getName());

        if (CommonUtils.isNull(users)) {
            return ResponseEntity
                    .ok("해당 유저를 조회할 수 없어 비밀번호 변경이 불가능합니다.");
        }

        int checkPassword = UserUtils.checkPasswordMatching(
                userRequest.getOldPassword(),
                users.getPassword()
        );

        if (checkPassword != UserConstants.PASSWORD_MATCH.getValue()) {
            log.info("비밀번호 일치하지 않음.");
            return ResponseEntity.ok("비밀번호가 다릅니다. 다시 입력해주세요.");
        }

        String url = "/user/logout";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        userService.updatePassword(
                users.getId(),
                userRequest.getNewPassword()
        );
        log.info("비밀번호 변경 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/user/withdraw")
    public ResponseEntity<?> userWithdraw(
            @RequestBody String password,
            Principal principal
    ) {
        Users users = userService.getUserEntity(principal.getName());

        if (CommonUtils.isNull(users)) {
            return ResponseEntity.ok("해당 유저를 조회할 수 없어 탈퇴가 불가능합니다.");
        }

        int checkPassword = UserUtils.checkPasswordMatching(
                password,
                users.getPassword()
        );

        if (checkPassword != UserConstants.PASSWORD_MATCH.getValue()) {
            log.info("비밀번호 일치하지 않음.");
            return ResponseEntity.ok("비밀번호가 다릅니다. 다시 입력해주세요.");
        }

        log.info("회원 : " + users.getId() + " 탈퇴 성공!!");
        userService.deleteUser(users.getId());

        return ResponseEntity.ok("그동안 서비스를 이용해주셔서 감사합니다.");
    }
}
