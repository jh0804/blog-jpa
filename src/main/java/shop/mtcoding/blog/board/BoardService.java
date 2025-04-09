package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.reply.ReplyRepository;
import shop.mtcoding.blog.love.Love;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    // userId가 없을 수도 있으므로 Integer 타입으로 지정해서 null까지 처리할 수 있도록 한다.
    public List<Board> 글목록보기(Integer userId) { // 매개변수에 User sessionUser처럼 필요하지 않은 것들을 전부 포함해서 호출하지 말자!
        return boardRepository.findAll(userId);
    }

    public BoardResponse.DetailDTO 글상세보기(int id, Integer userId) {
        // Board board = boardRepository.findById(id); // LAZY 로딩이므로 Board만 조회해서 board 정보 밖에 없다.
        // board.getUser().getEmail(); // 원래 null인데 lazy로딩이 발동해서 해당 유저 id로 select가 발동해서 값을 넣어준다. -> 비효율적이므로 안쓴다!
        Board board = boardRepository.findByIdJoinUserAndReplies(id);

        Love love = loveRepository.findByUserIdAndBoardId(userId, id); // (userId, boardId)
        Long loveCount = loveRepository.findByBoardId(id);

        Integer loveId = love == null ? null : love.getId();
        Boolean isLove = love == null ? false : true;

        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(board, userId, isLove, loveCount, loveId);
        return detailDTO;
    }
}
