package shop.mtcoding.blog.board.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final ReplyService replyService;
    private final HttpSession session;

    @PostMapping("/reply/save")
    public String saveReply(ReplyRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다.");

        replyService.save(reqDTO, sessionUser);

        return "redirect:/board/" + reqDTO.getBoardId();
    }

    @PostMapping("/reply/{id}/delete")
    public String deleteReply(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다.");

        // 권한 체크 필요 -> sessionUser 넘겨야 됨
        Integer boardId = replyService.delete(id, sessionUser.getId());

        return "redirect:/board/" + boardId;
    }
}
