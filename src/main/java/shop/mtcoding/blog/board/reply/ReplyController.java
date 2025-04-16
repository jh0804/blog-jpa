package shop.mtcoding.blog.board.reply;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final ReplyService replyService;
    private final HttpSession session;

    @PostMapping("/reply/save")
    public String saveReply(@Valid ReplyRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        replyService.save(reqDTO, sessionUser);

        return "redirect:/board/" + reqDTO.getBoardId();
    }

    @PostMapping("/reply/{id}/delete")
    public String deleteReply(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 권한 체크 필요 -> sessionUser 넘겨야 됨
        Integer boardId = replyService.delete(id, sessionUser.getId());

        return "redirect:/board/" + boardId;
    }
}
