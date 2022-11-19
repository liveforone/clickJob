package clickJob.clickJob.comment.service;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.repository.BoardRepository;
import clickJob.clickJob.comment.dto.CommentRequest;
import clickJob.clickJob.comment.dto.CommentResponse;
import clickJob.clickJob.comment.model.Comment;
import clickJob.clickJob.comment.repository.CommentRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //== entity ->  dto 편의메소드1 - 페이징 형식 ==//
    public Page<CommentResponse> entityToDtoPage(Page<Comment> commentList) {
        return commentList.map(m -> CommentResponse.builder()
                .id(m.getId())
                .writer(m.getUsers().getNickname())
                .content(m.getContent())
                .good(m.getGood())
                .createdDate(m.getCreatedDate())
                .build()
        );
    }

    //== entity -> dto 편의메소드2 - 엔티티 하나 ==//
    public CommentResponse entityToDtoDetail(Comment comment) {

        if (comment == null) {
            return null;
        }

        return CommentResponse.builder()
                .id(comment.getId())
                .writer(comment.getUsers().getNickname())
                .content(comment.getContent())
                .good(comment.getGood())
                .createdDate(comment.getCreatedDate())
                .build();
    }

    public Page<CommentResponse> getCommentList(Long boardId, Pageable pageable) {
        return entityToDtoPage(commentRepository.findByBoardId(boardId, pageable));
    }

    public Comment getCommentEntity(Long id) {
        return commentRepository.findOneById(id);
    }

    @Transactional
    public void saveComment(CommentRequest commentRequest, String writer, Long boardId) {
        Board board = boardRepository.findOneById(boardId);
        Users users = userRepository.findByEmail(writer);
        commentRequest.setUsers(users);
        commentRequest.setBoard(board);

        commentRepository.save(commentRequest.toEntity());
    }

    @Transactional
    public void editComment(String content, Long id) {
        commentRepository.updateComment(content, id);
    }

    @Transactional
    public void updateGood(Long id) {
        commentRepository.updateGood(id);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
