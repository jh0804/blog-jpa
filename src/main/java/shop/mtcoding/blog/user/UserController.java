package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.Resp;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/user/update-form")
    public String updateForm() {
        // ViewResolver(view를 찾아줌)의 prefix에 /templates/라고 되어있기 때문. suffix = .mustache
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // update user_tb set password = ?, email = ? where id = ?
        User user = userService.회원정보수정(updateDTO, sessionUser.getId());

        // 세션 동기화 (수정된 객체를 받아서 덮어씌운다)
        session.setAttribute("sessionUser", user);

        return "redirect:/";
    }

    @GetMapping("/api/check-username-available/{username}")
    public @ResponseBody Resp<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> dto = userService.유저네임중복체크(username);
        return Resp.ok(dto);
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO, HttpServletResponse response) {
        //System.out.println(loginDTO);
        User sessionUser = userService.login(loginDTO);
        session.setAttribute("sessionUser", sessionUser);
        if (loginDTO.getRememberMe() == null) {
            // username 쿠키를 지워야 한다.
            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } else { // on일 경우
            Cookie cookie = new Cookie("username", loginDTO.getUsername());
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login-form";
    }
}
