package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception403;
import shop.mtcoding.blog._core.error.ex.Exception404;
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

    // TODO : ajax 사용X
    @Transactional
    public void 글삭제() {

    }// 객체 찾아서 객체 상태 바꾸고 dirty checking으로

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
        Board board = boardRepository.findByIdJoinUserAndReplies(id); // 양방향 매핑

        Love love = loveRepository.findByUserIdAndBoardId(userId, id); // (userId, boardId)
        Long loveCount = loveRepository.findByBoardId(id);

        Integer loveId = love == null ? null : love.getId();
        Boolean isLove = love == null ? false : true;

        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(board, userId, isLove, loveCount, loveId);
        return detailDTO;
    }

    public Board 업데이트글보기(int id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) throw new Exception404("자원을 찾을 수 없습니다.");

        // getId()는 LAZY 로딩 안됨
        if (!(boardPS.getUser().getId().equals(sessionUserId))) throw new Exception403("권한이 없습니다.");

        return boardPS;
    }

    @Transactional
    public void 글수정하기(Integer id, BoardRequest.UpdateDTO reqDTO, Integer sessionUserId) { // 정확하게 받을 것만 받는 게 좋다(User 객체보다는 id만
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) throw new Exception404("자원을 찾을 수 없습니다.");

        if (!(boardPS.getUser().getId().equals(sessionUserId))) throw new Exception403("권한이 없습니다.");

        boardPS.update(reqDTO.getTitle(), reqDTO.getContent(), reqDTO.getIsPublic());
    } // 더티 체킹 (상태 변경해서 update)
}
