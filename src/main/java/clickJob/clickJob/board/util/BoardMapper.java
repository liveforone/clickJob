package clickJob.clickJob.board.util;

import clickJob.clickJob.board.dto.BoardRequest;
import clickJob.clickJob.board.dto.BoardResponse;
import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.utility.CommonUtils;
import org.springframework.data.domain.Page;

public class BoardMapper {

    //== dto -> entity ==//
    public static Board dtoToEntity(BoardRequest board) {
        return Board.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .users(board.getUsers())
                .view(board.getView())
                .good(board.getGood())
                .build();
    }

    //== BoardResponse builder method ==//
    private static BoardResponse dtoBuilder(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .view(board.getView())
                .good(board.getGood())
                .createdDate(board.getCreatedDate())
                .build();
    }

    //== entity ->  dto 편의메소드1 - 페이징 ==//
    public static Page<BoardResponse> entityToDtoPage(Page<Board> boardList) {
        return boardList.map(BoardMapper::dtoBuilder);
    }

    //== entity -> dto 편의메소드2 - detail ==//
    public static BoardResponse entityToDtoDetail(Board board) {

        if (CommonUtils.isNull(board)) {
            return null;
        }
        return BoardMapper.dtoBuilder(board);
    }
}
