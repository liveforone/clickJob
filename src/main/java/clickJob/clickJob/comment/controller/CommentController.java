package clickJob.clickJob.comment.controller;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.service.BoardService;
import clickJob.clickJob.comment.dto.CommentRequest;
import clickJob.clickJob.comment.dto.CommentResponse;
import clickJob.clickJob.comment.model.Comment;
import clickJob.clickJob.comment.service.CommentService;
import clickJob.clickJob.comment.util.CommentMapper;
import clickJob.clickJob.users.service.UserService;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/comment/{boardId}")
    public ResponseEntity<Map<String, Object>> commentList(
            @PageableDefault(page = 0, size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @PathVariable("boardId") Long boardId,
            Principal principal
    ) {
        Map<String, Object> map = new HashMap<>();
        Page<CommentResponse> commentList = commentService.getCommentList(
                boardId,
                pageable
        );
        String user = userService.getUserByEmail(principal.getName()).getNickname();

        map.put("user", user);
        map.put("body", commentList);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/comment/post/{boardId}")
    public ResponseEntity<?> commentPost(
            @PathVariable("boardId") Long boardId,
            @RequestBody CommentRequest commentRequest,
            Principal principal
    ) {
        Board board = boardService.getBoardEntity(boardId);

        if (CommonUtils.isNull(board)) {
            return ResponseEntity.ok("해당 게시글이 없어 댓글작성이 불가능합니다.");
        }

        String url = "/comment/" + boardId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        commentService.saveComment(
                commentRequest,
                principal.getName(),
                boardId
        );
        log.info("댓글 저장 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/comment/edit/{id}")
    public ResponseEntity<?> commentEditPage(@PathVariable("id") Long id) {
        Comment comment = commentService.getCommentEntity(id);

        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("해당 댓글을 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(CommentMapper.entityToDtoDetail(comment));
    }

    @PostMapping("/comment/edit/{id}")
    public ResponseEntity<?> commentEdit(
            @PathVariable("id") Long id,
            @RequestBody String content,
            Principal principal
    ) {
        Comment comment = commentService.getCommentEntity(id);
        String user = userService.getUserByEmail(principal.getName()).getNickname();

        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("해당 댓글이 없어 수정이 불가능합니다.");
        }

        if (!Objects.equals(comment.getUsers().getNickname(), user)) {
            return ResponseEntity.ok("회원님이 댓글 작성자와 달라 수정할 수 없습니다.");
        }

        String url = "/comment/" + comment.getBoard().getId();
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        commentService.editComment(
                content,
                id
        );
        log.info("댓글 수정 성공 !!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/comment/good/{id}")
    public ResponseEntity<?> commentUpdateGood(@PathVariable("id") Long id) {
        Comment comment = commentService.getCommentEntity(id);

        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("해당 댓글이 없어 좋아요 반영이 불가능합니다.");
        }

        String url = "/comment/" + comment.getBoard().getId();
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        commentService.updateGood(id);
        log.info("댓글 좋아요 업데이트 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/comment/delete/{id}")
    public ResponseEntity<?> commentDelete(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Comment comment = commentService.getCommentEntity(id);
        String user = userService.getUserByEmail(principal.getName()).getNickname();

        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("해당 게시글이 없어 삭제가 불가능합니다.");
        }

        if (!Objects.equals(comment.getUsers().getNickname(), user)) {
            return ResponseEntity
                    .ok("회원님과 작성자가 서로 달라 댓글 삭제가 불가능합니다.");
        }

        Long boardId = comment.getBoard().getId();

        commentService.deleteComment(id);
        log.info("댓글 삭제완료 !!");

        String url = "/comment/" + boardId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
