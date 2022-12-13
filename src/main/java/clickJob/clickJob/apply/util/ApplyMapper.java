package clickJob.clickJob.apply.util;

import clickJob.clickJob.apply.dto.ApplyJobResponse;
import clickJob.clickJob.apply.dto.ApplyRequest;
import clickJob.clickJob.apply.dto.ApplyUserResponse;
import clickJob.clickJob.apply.model.Apply;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyMapper {

    /*
     * dto ->  entity 변환 편의 메소드
     */
    public static Apply dtoToEntity(ApplyRequest apply) {
        return Apply.builder()
                .id(apply.getId())
                .users(apply.getUsers())
                .job(apply.getJob())
                .build();
    }

    /*
     * ApplyUserResponse builder 편의 메소드
     */
    private static ApplyUserResponse applyUserResponseBuilder(Apply apply) {
        return ApplyUserResponse.builder()
                .company(apply.getJob().getCompany())
                .build();
    }

    /*
     * ApplyJobResponse builder 편의 메소드
     */
    private static ApplyJobResponse applyJobResponseBuilder(Apply apply) {
        return ApplyJobResponse.builder()
                .name(apply.getUsers().getNickname())
                .build();
    }

    /*
     * entity -> ApplyUserResponse 편의 메소드1
     * 반환 타입 : 리스트형식
     */
    public static List<ApplyUserResponse> entityToDtoListUserResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(ApplyMapper::applyUserResponseBuilder)
                .collect(Collectors.toList());
    }

    /*
     * entity -> ApplyJobResponse 편의 메소드1
     * 반환 타입 : 리스트형식
     */
    public static List<ApplyJobResponse> entityToDtoJobResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(ApplyMapper::applyJobResponseBuilder)
                .collect(Collectors.toList());
    }
}
