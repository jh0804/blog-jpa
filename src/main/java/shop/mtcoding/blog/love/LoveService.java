package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // DI
@Service // IoC
public class LoveService {
    private final LoveRepository loveRepository;
}
