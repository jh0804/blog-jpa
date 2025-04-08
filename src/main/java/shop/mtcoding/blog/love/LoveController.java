package shop.mtcoding.blog.love;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.Resp;
import shop.mtcoding.blog.user.User;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class LoveController {
    private final LoveService loveService;
    private final HttpSession session;

    @PostMapping("/love")
    public @ResponseBody Resp<?> save(@RequestBody LoveRequest.SaveDTO saveDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다.");

        Map<String, Integer> dto = loveService.save(saveDTO, sessionUser);
        return Resp.ok(dto);
    }
}
