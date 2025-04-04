package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
// 책임 : 트랜잭션 처리, 비지니스 로직, DTO 완료
public class UserService {
    private final UserRespository userRespository;

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) {
        try {
            userRespository.save(joinDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("이미 존재하는 아이디로 회원가입 하지 마세요. postman도 쓰지 마세요.");
        }
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

    public Map<String, Object> 유저네임중복체크(String username) {
        User user = userRespository.findByUsername(username);
        Map<String, Object> dto = new HashMap<>();

        if (user == null) {
            dto.put("available", true);
        } else {
            dto.put("available", false);
        }
        return dto;
    }
}
