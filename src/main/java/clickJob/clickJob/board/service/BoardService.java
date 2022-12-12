package clickJob.clickJob.board.service;

import clickJob.clickJob.board.dto.BoardRequest;
import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.board.repository.BoardRepository;
import clickJob.clickJob.board.util.BoardMapper;
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

    public Page<BoardResponse> getAllBoard(Pageable pageable) {
        return BoardMapper.entityToDtoPage(
                boardRepository.findAllBoard(pageable)
        );
    }

    public Page<BoardResponse> getSearchList(String keyword, Pageable pageable) {
        return BoardMapper.entityToDtoPage(
                boardRepository.searchByTitle(
                    keyword,
                    pageable
                )
        );
    }

    public Page<BoardResponse> getBoardByEmail(String email, Pageable pageable) {
        return BoardMapper.entityToDtoPage(
                boardRepository.findBoardByEmail(
                    email,
                    pageable
                )
        );
    }

    public Page<BoardResponse> getBoardByNickname(String nickname, Pageable pageable) {
        return BoardMapper.entityToDtoPage(
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
                BoardMapper.dtoToEntity(boardRequest)).getId();
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
                BoardMapper.dtoToEntity(boardRequest)
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
