package shop.mtcoding.blog.love;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Resp;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController // 여기서는 ajax로 데이터만 return하는 controller이므로 그냥 RestController라고 하자
public class LoveController {
    private final LoveService loveService;
    private final HttpSession session;

    @PostMapping("/api/love")
    public Resp<?> saveLove(@RequestBody LoveRequest.SaveDTO reqDTO) { // @RestController이므로 @ResponseBody 안붙여도 된다.
        User sessionUser = (User) session.getAttribute("sessionUser");

        LoveResponse.SaveDTO respDTO = loveService.좋아요(reqDTO, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/api/love/{id}")
    public Resp<?> deleteLove(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        LoveResponse.DeleteDTO respDTO = loveService.좋아요취소(id, sessionUser.getId());

        return Resp.ok(respDTO);
    }
}
