package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.error.ex.Exception401;
import shop.mtcoding.blog._core.error.ex.Exception404;

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
            // Exception400 (Bad Request -> 잘못된 요청입니다.)
            throw new Exception400("이미 존재하는 아이디로 회원가입 하지 마세요. postman도 쓰지 마세요.");
        }
    }

    public User login(UserRequest.LoginDTO loginDTO) {
        // 1. username 일치 검사
        User user = userRespository.findByUsername(loginDTO.getUsername());
        // 2. username 불일치 -> Exception
        if (user == null) throw new Exception401("username 혹은 password가 틀렸습니다.");
        // 3. password 불일치 -> Exception
        if (!(user.getPassword().equals(loginDTO.getPassword())))
            throw new Exception401("username 혹은 password가 틀렸습니다.");
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

    @Transactional
    public User 회원정보수정(UserRequest.UpdateDTO updateDTO, Integer id) {
        User user = userRespository.findById(id); // 조회됨 -> 영속화된 객체
        if (user == null) throw new Exception404("회원을 찾을 수 없습니다."); // 없는 데이터를 업데이트할 필요 없음 (고립X)
        user.update(updateDTO.getPassword(), updateDTO.getEmail()); // (PC를 거쳐서 DB에서 조회한)영속화된 객체의 상태가 변경된다.
        return user;
    } // 함수가 종료될 때 dirty checking -> 상태가 변경되면 update query
}
