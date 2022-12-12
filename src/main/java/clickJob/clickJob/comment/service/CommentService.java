package clickJob.clickJob.comment.service;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.repository.BoardRepository;
import clickJob.clickJob.comment.dto.CommentRequest;
import clickJob.clickJob.comment.dto.CommentResponse;
import clickJob.clickJob.comment.model.Comment;
import clickJob.clickJob.comment.repository.CommentRepository;
import clickJob.clickJob.comment.util.CommentMapper;
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

    public Page<CommentResponse> getCommentList(Long boardId, Pageable pageable) {
        return CommentMapper.entityToDtoPage(
                commentRepository.findByBoardId(
                        boardId,
                        pageable
                )
        );
    }

    public Comment getCommentEntity(Long id) {
        return commentRepository.findOneById(id);
    }

    @Transactional
    public void saveComment(
            CommentRequest commentRequest,
            String writer,
            Long boardId
    ) {
        Board board = boardRepository.findOneById(boardId);
        Users users = userRepository.findByEmail(writer);
        commentRequest.setUsers(users);
        commentRequest.setBoard(board);

        commentRepository.save(
                CommentMapper.dtoToEntity(commentRequest)
        );
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
