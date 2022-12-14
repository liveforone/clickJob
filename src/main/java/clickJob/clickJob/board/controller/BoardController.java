package clickJob.clickJob.board.controller;

import clickJob.clickJob.board.dto.BoardRequest;
import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.service.BoardService;
import clickJob.clickJob.board.util.BoardMapper;
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
        return ResponseEntity.ok("????????? ?????? ?????????");
    }

    @PostMapping("/board/post")
    public ResponseEntity<?> boardPost(
            @RequestBody BoardRequest boardRequest,
            Principal principal
    ) {
        Long boardId = boardService.saveBoard(boardRequest, principal.getName());
        log.info("????????? ?????? ??????");

        String url = "/board/" + boardId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

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

        if (CommonUtils.isNull(boardEntity)) {
            return ResponseEntity.ok("?????? ???????????? ???????????? ?????? ????????? ??????????????????.");
        }

        boardService.updateView(id);
        log.info("????????? ????????? + 1");

        Map<String, Object> map = new HashMap<>();
        BoardResponse board = BoardMapper.entityToDtoDetail(boardEntity);
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

        if (CommonUtils.isNull(board)) {
            return ResponseEntity.ok("?????? ???????????? ???????????? ?????? ???????????? ??????????????????.");
        }

        String url = "/board/" + id;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        boardService.updateGood(id);
        log.info("????????? ????????? ???????????? ??????");

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

        if (CommonUtils.isNull(boardEntity)) {
            return ResponseEntity.ok("???????????? ?????????????????? ????????? ??????????????????.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("????????? ???????????? ???????????? ?????? ????????? ??????????????????.");
        }

        return ResponseEntity.ok(
                BoardMapper.entityToDtoDetail(boardEntity)
        );
    }

    @PostMapping("/board/edit/{id}")
    public ResponseEntity<?> boardEdit(
            @PathVariable("id") Long id,
            @RequestBody BoardRequest boardRequest,
            Principal principal
    ) {
        Board boardEntity = boardService.getBoardEntity(id);

        if (CommonUtils.isNull(boardEntity)) {
            return ResponseEntity.ok("????????? ???????????? ?????? ????????? ??????????????????.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("???????????? ???????????? ?????? ????????? ??????????????????.");
        }

        String url = "/board/" + id;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        boardService.editBoard(
                id,
                boardRequest
        );
        log.info("????????? id=" + id + " ????????????");

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

        if (CommonUtils.isNull(boardEntity)) {
            return ResponseEntity
                    .ok("???????????? ???????????? ?????? ????????? ????????? ??????????????????.");
        }

        if (!Objects.equals(boardEntity.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("???????????? ???????????? ?????? ????????? ??????????????????.");
        }

        boardService.deleteBoard(id);
        log.info("????????? id=" + id + " ?????? ??????");

        String url = "/board";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
