package shop.mtcoding.blog.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BoardController {

    @GetMapping("/")
    public String list() {
        return "board/list";
    }
}
