package shop.mtcoding.blog.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserRequest {

    @Data
    public static class UpdateDTO {
        @Size(min = 4, max = 20)
        private String password;
        @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 적어주세요")
        private String email;
    }

    // insert 용도의 DTO에는 toEntity 메서드를 만든다.
    @Data
    public static class JoinDTO {
        @Pattern(regexp="^[a-zA-Z0-9]{2,20}$", message = "유저네임은 2-20자이며, 특수문자,한글이 포함될 수 없습니다")
        private String username;

        @Size(min = 4, max = 20)
        private String password;

        // setter시에 reflection을 강제
        @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 적어주세요")
        private String email;

        // static이 안붙었기 때문에 JoinDTO가 new가 되어야 쓸 수 있음
        // DTO -> User 객체로 변환
        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        @Pattern(regexp="^[a-zA-Z0-9]{2,20}$", message = "유저네임은 2-20자이며, 특수문자,한글이 포함될 수 없습니다")
        private String username;
        @Size(min = 4, max = 20)
        private String password;
        private String rememberMe; // check 되면 on, 안되면 null 
    }
}
