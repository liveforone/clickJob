package clickJob.clickJob.comment.util;

import clickJob.clickJob.comment.dto.CommentRequest;
import clickJob.clickJob.comment.dto.CommentResponse;
import clickJob.clickJob.comment.model.Comment;
import clickJob.clickJob.utility.CommonUtils;
import org.springframework.data.domain.Page;

public class CommentMapper {

    //== dto -> entity ==//
    public static Comment dtoToEntity(CommentRequest comment) {
        return Comment.builder()
                .id(comment.getId())
                .users(comment.getUsers())
                .content(comment.getContent())
                .board(comment.getBoard())
                .good(comment.getGood())
                .build();
    }

    //== CommentResponse builder method ==//
    private static CommentResponse dtoBuilder(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .writer(comment.getUsers().getNickname())
                .content(comment.getContent())
                .good(comment.getGood())
                .createdDate(comment.getCreatedDate())
                .build();
    }

    //== entity ->  dto 편의메소드1 - 페이징 형식 ==//
    public static Page<CommentResponse> entityToDtoPage(Page<Comment> commentList) {
        return commentList.map(CommentMapper::dtoBuilder);
    }

    //== entity -> dto 편의메소드2 - 엔티티 하나 ==//
    public static CommentResponse entityToDtoDetail(Comment comment) {

        if (CommonUtils.isNull(comment)) {
            return null;
        }
        return CommentMapper.dtoBuilder(comment);
    }
}
