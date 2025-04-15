package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.util.Resp;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {

         // 부가 로직
         if(errors.hasErrors()) {
            List<FieldError> fErrors = errors.getFieldErrors();

            // 필드가 여러 개지만 에러 메세지를 유저에게 여러개 전달X
            for(FieldError fieldError : fErrors) {
                // 순서 보장X
                throw new Exception400(fieldError.getField()+":"+fieldError.getDefaultMessage()); // fieldError.getField() = > e.g. username
            }
         }

        // 유효성 검사 -> AOP
        // 빈생성자 + setter 호출됨
//        boolean r1 = Pattern.matches("^[a-zA-Z0-9]{2,20}$", joinDTO.getUsername());
//        boolean r2 = Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{6,20}$", joinDTO.getPassword());
//        boolean r3 = Pattern.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", joinDTO.getEmail());
//
//        if (!r1) throw new Exception400("유저네임은 2-20자이며, 특수문자,한글이 포함될 수 없습니다");
//        if (!r2) throw new Exception400("패스워드는 4-20자이며, 특수문자,영어 대문자,소문자, 숫자가 포함되어야 하며, 공백이 있을 수 없습니다");
//        if (!r3) throw new Exception400("이메일 형식에 맞게 적어주세요");

        // 핵심 로직
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    // Errors errors 위치 중요!!! @Valid 바로 다음 매개변수 자리에 있어야 됨
    public String login(@Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpServletResponse response) {

        // 부가 로직
        if(errors.hasErrors()) {
            List<FieldError> fErrors = errors.getFieldErrors();
            // 에러 메세지를 유저에게 여러개 전달하는 건 좋은 선택X
            for(FieldError fieldError : fErrors) {
                // 순서 보장X
                throw new Exception400(fieldError.getField()+":"+fieldError.getDefaultMessage()); // fieldError.getField() = > e.g. username
            }
        }

        // 핵심 로직
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
