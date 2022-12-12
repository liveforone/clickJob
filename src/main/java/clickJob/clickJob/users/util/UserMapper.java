package clickJob.clickJob.users.util;

import clickJob.clickJob.users.dto.UserRequest;
import clickJob.clickJob.users.dto.UserResponse;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.utility.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    //== dto -> entity ==//
    public static Users dtoToEntity(UserRequest user) {
        return Users.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .auth(user.getAuth())
                .nickname(user.getNickname())
                .build();
    }

    //== UserResponse builder method ==//
    private static UserResponse dtoBuilder(Users users) {
        return UserResponse.builder()
                .id(users.getId())
                .email(users.getEmail())
                .auth(users.getAuth())
                .nickname(users.getNickname())
                .build();
    }

    //== entity -> dto1 - detail ==//
    public static UserResponse entityToDtoDetail(Users users) {

        if (CommonUtils.isNull(users)) {
            return null;
        }
        return UserMapper.dtoBuilder(users);
    }

    //== entity -> dto2 - list ==//
    public static List<UserResponse> entityToDtoList(List<Users> usersList) {
        return usersList
                .stream()
                .map(UserMapper::dtoBuilder)
                .collect(Collectors.toList());
    }
}
