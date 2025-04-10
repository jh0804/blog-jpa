package shop.mtcoding.blog.board.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRepository;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void save(ReplyRequest.SaveDTO reqDTO, User sessionUser) {
        Board board = boardRepository.findById(reqDTO.getBoardId());
        if (board == null) throw new RuntimeException("Board not found");

        Reply reply = reqDTO.toEntity(sessionUser, board);
        replyRepository.save(reply);
    }

    @Transactional
    public Integer delete(Integer id, Integer sessionUserId) {
        // 댓글 존재 확인
        Reply replyPS = replyRepository.findById(id);
        if (replyPS == null) throw new RuntimeException("Reply not found");

        // 권한 체크
        if (!(replyPS.getUser().getId().equals(sessionUserId))) throw new RuntimeException("User not authorized");

        Integer boardId = replyPS.getBoard().getId();

        replyRepository.deleteById(id);

        return boardId;
    }
}
