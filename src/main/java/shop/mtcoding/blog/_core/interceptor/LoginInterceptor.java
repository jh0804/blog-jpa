package shop.mtcoding.blog._core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.mtcoding.blog._core.error.ex.Exception401;
import shop.mtcoding.blog._core.error.ex.ExceptionApi401;
import shop.mtcoding.blog.user.User;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI(); // request가 담고 있는 것들 예전에 찍어봣는데 기억안나면 해보기ㅣㅣ

        HttpSession session = request.getSession(); // request로 session 영역에 접근할 수 있는
        // 인증이 안되면 throw가 터짐
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            if (uri.contains("/api")) {
                throw new ExceptionApi401("인증이 필요합니다.");
                // 만약에 직접 처리해야한다면 (filter로 해야하면)
//                response.setStatus(401);
//                response.setHeader("Content-Type", "application/json");
//                PrintWriter out = response.getWriter();
//                Resp<?> resp = Resp.fail(401, "인증이 필요합니다");
//                ObjectMapper mapper = new ObjectMapper();
//                String responseBody = mapper.writeValueAsString(resp);
//                out.println(responseBody);
//                return false;
            } else {
                throw new Exception401("인증이 필요합니다.");
                // 만약에 직접 처리해야한다면
//                response.setStatus(401);
//                response.setHeader("Content-Type", "text/html");
//                PrintWriter out = response.getWriter();
//                out.println(Script.href("인증이 필요합니다", "/login-form"));
//                return false;
            }
        }
        return true; // true : controller 호출O / false : controller 안때림
    }
}
