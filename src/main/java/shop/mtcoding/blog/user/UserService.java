package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRespository userRespository;

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) {
        userRespository.save(joinDTO.toEntity());
    }

    public User login(UserRequest.LoginDTO loginDTO) {
        // 1. username 일치 검사
        User user = userRespository.findByUsername(loginDTO.getUsername());
        // 2. username 불일치 -> Exception
        if (user == null) throw new RuntimeException("해당 username이 존재하지 않습니다.");
        // 3. password 불일치 -> Exception
        if (!(user.getPassword().equals(loginDTO.getPassword()))) throw new RuntimeException("password가 일치하지 않습니다.");

        // 4. username & password 전부 일치
        return user;
    }
}
