package clickJob.clickJob.follow.controller;

import clickJob.clickJob.follow.model.Follow;
import clickJob.clickJob.follow.service.FollowService;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{nickname}")
    public ResponseEntity<?> follow(
            @PathVariable("nickname") String nickname,
            Principal principal
    ) {
        Follow follow = followService.getFollowEntity(principal.getName(), nickname);

        if (!CommonUtils.isNull(follow)) {  //팔로우 중복 check
            return ResponseEntity.ok("이미 팔로우 되어있습니다.");
        }

        String url = "/user/profile/" + nickname;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        followService.saveFollow(
                nickname,
                principal.getName()
        );
        log.info("팔로잉 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/follow/my-follow")
    public ResponseEntity<?> myFollowList(Principal principal) {
        List<String> myFollowList = followService.getMyFollowList(principal.getName());

        if (CommonUtils.isNull(myFollowList)) {
            return ResponseEntity.ok("나의 팔로우가 없습니다.");
        }

        return ResponseEntity.ok(myFollowList);
    }

    @GetMapping("/follow/my-follower")
    public ResponseEntity<?> myFollowerList(Principal principal) {
        List<String> myFollowerList =
                followService.getMyFollowerList(principal.getName());

        if (CommonUtils.isNull(myFollowerList)) {
            return ResponseEntity.ok("나를 팔로우 하는 사람이 없습니다.");
        }

        return ResponseEntity.ok(myFollowerList);
    }

    @PostMapping("/unfollow/{nickname}")
    public ResponseEntity<?> unfollow(
            @PathVariable("nickname") String nickname,
            Principal principal
    ) {
        Follow follow = followService.getFollowEntity(principal.getName(), nickname);

        if (CommonUtils.isNull(follow)) {
            return ResponseEntity.ok("이미 이웃이 아닙니다.");
        }

        String url = "/user/my-page";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        followService.unfollow(
                principal.getName(),
                nickname
        );
        log.info("언팔로우 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/follow/profile-follow/{nickname}")
    public ResponseEntity<?> profileFollow(
            @PathVariable("nickname") String nickname
    ) {
        List<String> followList = followService.getProfileFollowList(nickname);

        if (CommonUtils.isNull(followList)) {
            return ResponseEntity.ok("팔로우하는 사람이 없습니다.");
        }

        return ResponseEntity.ok(followList);
    }

    @GetMapping("/follow/profile-follower/{nickname}")
    public ResponseEntity<?> profileFollower(
            @PathVariable("nickname") String nickname
    ) {
        List<String> followerList = followService.getProfileFollowerList(nickname);

        if (CommonUtils.isNull(followerList)) {
            return ResponseEntity.ok("팔로워가 없습니다.");
        }

        return ResponseEntity.ok(followerList);
    }
}
