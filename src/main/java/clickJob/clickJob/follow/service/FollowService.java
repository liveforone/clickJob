package clickJob.clickJob.follow.service;

import clickJob.clickJob.follow.model.Follow;
import clickJob.clickJob.follow.repository.FollowRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /*
    * follow list
    * what : 내가 팔로우 하는
     */
    public List<String> getMyFollowList(String email) {
        List<Follow> followList = followRepository.findByFollower(email);
        return followList
                .stream()
                .map(follow -> follow.getUsers().getNickname())
                .collect(Collectors.toList());
    }

    /*
    * follow list
    * what : 나를 팔로우 하는
     */
    public List<String> getMyFollowerList(String email) {
        List<Follow> followerList = followRepository.findByUsers(email);
        return followerList
                .stream()
                .map(follow -> follow.getFollower().getNickname())
                .collect(Collectors.toList());
    }

    /*
    * profile
    * what : 해당 프로필의 주인이 팔로우 하는
     */
    public List<String> getProfileFollowList(String nickname) {
        List<Follow> followList = followRepository.findByFollowerNickname(nickname);
        return followList
                .stream()
                .map(follow -> follow.getUsers().getNickname())
                .collect(Collectors.toList());
    }

    /*
    * profile
    * 해당 프로필의 주인을 팔로우 하는
     */
    public List<String> getProfileFollowerList(String nickname) {
        List<Follow> followerList = followRepository.findByUsersNickname(nickname);
        return followerList
                .stream()
                .map(follow -> follow.getFollower().getNickname())
                .collect(Collectors.toList());
    }

    public Follow getFollowEntity(String followerEmail, String userNickname) {
        Users me = userRepository.findByEmail(followerEmail);  //나
        Users myFollow = userRepository.findByNickname(userNickname);  //나의 팔로잉, 팔로잉 당하는 사람
        
        return followRepository.findOneFollow(me, myFollow);
    }

    @Transactional
    public void saveFollow(String users, String follower) {
        Users user = userRepository.findByNickname(users);
        Users user_follower = userRepository.findByEmail(follower);

        Follow follow = Follow.builder()
                .follower(user_follower)
                .users(user)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String follower, String users) {
        Users user_me = userRepository.findByEmail(follower);  //나
        Users user_follow = userRepository.findByNickname(users);  //내가 팔로우하는 사람

        Follow follow = followRepository.findOneFollow(user_me, user_follow);
        followRepository.deleteById(follow.getId());
    }
}
