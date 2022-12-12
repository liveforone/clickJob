package clickJob.clickJob.apply.util;

import clickJob.clickJob.apply.dto.ApplyJobResponse;
import clickJob.clickJob.apply.dto.ApplyRequest;
import clickJob.clickJob.apply.dto.ApplyUserResponse;
import clickJob.clickJob.apply.model.Apply;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyMapper {

    //== dto -> entity ==//
    public static Apply dtoToEntity(ApplyRequest apply) {
        return Apply.builder()
                .id(apply.getId())
                .users(apply.getUsers())
                .job(apply.getJob())
                .build();
    }

    //== ApplyUserResponse builder ==//
    private static ApplyUserResponse applyUserResponseBuilder(Apply apply) {
        return ApplyUserResponse.builder()
                .company(apply.getJob().getCompany())
                .build();
    }

    //== ApplyJobResponse builder ==//
    private static ApplyJobResponse applyJobResponseBuilder(Apply apply) {
        return ApplyJobResponse.builder()
                .name(apply.getUsers().getNickname())
                .build();
    }

    //entity -> dto1 - user response list ==//
    public static List<ApplyUserResponse> entityToDtoListUserResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(ApplyMapper::applyUserResponseBuilder)
                .collect(Collectors.toList());
    }

    //== entity -> dto2 - job response list ==//
    public static List<ApplyJobResponse> entityToDtoJobResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(ApplyMapper::applyJobResponseBuilder)
                .collect(Collectors.toList());
    }
}
