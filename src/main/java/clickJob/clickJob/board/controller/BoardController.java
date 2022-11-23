package clickJob.clickJob.board.controller;

import clickJob.clickJob.board.dto.BoardRequest;
import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.service.BoardService;
import clickJob.clickJob.users.service.UserService;
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

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/board")
    public ResponseEntity<Page<BoardResponse>> boardHome(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<BoardResponse> board = boardService.getAllBoard(pageable);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/board/best")
    public ResponseEntity<Page<BoardResponse>> boardBestPage(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "view", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<BoardResponse> board = boardService.getAllBoard(pageable);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/board/search")
    public ResponseEntity<Page<BoardResponse>> boardSearchPage(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @RequestParam("keyword") String keyword
    ) {
        Page<BoardResponse> board = boardService.getSearchList(keyword, pageable);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/board/post")
    public ResponseEntity<?> postPage() {
        return ResponseEntity.ok("게시글 등록 페이지");
    }

    @PostMapping("/board/post")
    public ResponseEntity<?> boardPost(
            @RequestBody BoardRequest boardRequest,
            Principal principal
    ) {
        Long boardId = boardService.saveBoard(boardRequest, principal.getName());
        log.info("게시글 저장 성공");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(
                "/board/" + boardId
        ));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<?> boardDetail(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Board boardEntity = boardService.getBoardEntity(id);

        if (boardEntity == null) {
            return ResponseEntity.ok("해당 게시글이 존재하지 않아 조회가 불가능합니다.");
        }

        boardService.updateView(id);
        log.info("게시글 조회수 + 1");

        Map<String, Object> map = new HashMap<>();
        BoardResponse board = boardService.entityToDtoDetail(boardEntity);
        String writer = boardEntity.getUsers().getNickname();
        String user = userService.getUserByEmail(principal.getName()).getNickname();

        map.put("user", user);
        map.put("writer", writer);
        map.put("board", board);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/board/good/{id}")
    public ResponseEntity<?> boardGood(@PathVariable("id") Long id) {
        Board board = boardService.getBoardEntity(id);

        if (board == null) {
            return ResponseEntity.ok("해당 게시글이 존재하지 않아 좋아요가 불가능합니다.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(
                "/board/" + id
        ));

        boardService.updateGood(id);
        log.info("게시글 좋아요 업데이트 성공");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/board/edit/{id}")
    public ResponseEntity<?> boardEditPage(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Board boardEntity = boardService.getBoardEntity(id);

        if (boardEntity == null) {
            return ResponseEntity.ok("게시글이 존재하지않아 조회가 불가능합니다.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("게시글 작성자와 회원님이 달라 수정이 불가능합니다.");
        }

        return ResponseEntity.ok(
                boardService.entityToDtoDetail(boardEntity)
        );
    }

    @PostMapping("/board/edit/{id}")
    public ResponseEntity<?> boardEdit(
            @PathVariable("id") Long id,
            @RequestBody BoardRequest boardRequest,
            Principal principal
    ) {
        Board boardEntity = boardService.getBoardEntity(id);

        if (boardEntity == null) {
            return ResponseEntity.ok("게시글 존재하지 않아 수정이 불가능합니다.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("작성자와 회원님이 달라 수정이 불가능합니다.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(
                "/board/" + id
        ));

        boardService.editBoard(
                id,
                boardRequest
        );
        log.info("게시글 id=" + id + " 수정성공");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/board/delete/{id}")
    public ResponseEntity<?> boardDelete(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Board boardEntity = boardService.getBoardEntity(id);

        if (boardEntity == null) {
            return ResponseEntity
                    .ok("게시글이 존재하지 않아 조회가 삭제가 불가능합니다.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("작성자와 회원님이 달라 삭제가 불가능합니다.");
        }

        boardService.deleteBoard(id);
        log.info("게시글 id=" + id + " 삭제 성공");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(
                "/board"
        ));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
