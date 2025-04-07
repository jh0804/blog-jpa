package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    // userId가 없을 수도 있으므로 Integer 타입으로 지정해서 null까지 처리할 수 있도록 한다.
    public List<Board> 글목록보기(Integer userId) { // 매개변수에 User sessionUser처럼 필요하지 않은 것들을 전부 포함해서 호출하지 말자!
        return boardRepository.findAll(userId);
    }
}
