package clickJob.clickJob.board.service;

import clickJob.clickJob.board.dto.BoardRequest;
import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.repository.BoardRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //== BoardResponse builder method ==//
    public BoardResponse dtoBuilder(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .view(board.getView())
                .good(board.getGood())
                .createdDate(board.getCreatedDate())
                .build();
    }

    //== dto -> entity ==//
    public Board dtoToEntity(BoardRequest board) {
        return Board.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .users(board.getUsers())
                .view(board.getView())
                .good(board.getGood())
                .build();
    }

    //== entity ->  dto 편의메소드1 - 페이징 ==//
    public Page<BoardResponse> entityToDtoPage(Page<Board> boardList) {
        return boardList.map(this::dtoBuilder);
    }

    //== entity -> dto 편의메소드2 - 엔티티 ==//
    public BoardResponse entityToDtoDetail(Board board) {

        if (board == null) {
            return null;
        }
        return dtoBuilder(board);
    }

    public Page<BoardResponse> getAllBoard(Pageable pageable) {
        return entityToDtoPage(
                boardRepository.findAllBoard(pageable)
        );
    }

    public Page<BoardResponse> getSearchList(String keyword, Pageable pageable) {
        return entityToDtoPage(
                boardRepository.searchByTitle(
                    keyword,
                    pageable
                )
        );
    }

    public Page<BoardResponse> getBoardByEmail(String email, Pageable pageable) {
        return entityToDtoPage(
                boardRepository.findBoardByEmail(
                    email,
                    pageable
                )
        );
    }

    public Page<BoardResponse> getBoardByNickname(String nickname, Pageable pageable) {
        return entityToDtoPage(
                boardRepository.findBoardByNickname(
                    nickname,
                    pageable
                )
        );
    }

    public Board getBoardEntity(Long id) {
        return boardRepository.findOneById(id);
    }

    @Transactional
    public Long saveBoard(BoardRequest boardRequest, String email) {
        Users users = userRepository.findByEmail(email);

        boardRequest.setUsers(users);

        return boardRepository.save(
                dtoToEntity(boardRequest)).getId();
    }

    @Transactional
    public void updateView(Long id) {
        boardRepository.updateView(id);
    }

    @Transactional
    public void editBoard(Long id, BoardRequest boardRequest) {
        Board board = boardRepository.findOneById(id);

        boardRequest.setId(id);
        boardRequest.setUsers(board.getUsers());
        boardRequest.setGood(board.getGood());
        boardRequest.setView(board.getView());

        boardRepository.save(
                dtoToEntity(boardRequest)
        );
    }

    @Transactional
    public void updateGood(Long id) {
        boardRepository.updateGood(id);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
