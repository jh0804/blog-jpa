package shop.mtcoding.blog.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        @NotEmpty(message="제목을 입력하세요")
        private String title;
        @NotEmpty(message="내용을 입력하세요")
        private String content;
        private String isPublic;

        // insert는 toEntity가 필요하다.
        // query가 아니라 객체로 만들어서 집어넣을 것이므로 toEntity를 만들어야한다.
        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    // null 아니면 무조건 true가 됨
                    .isPublic(isPublic == null ? false : true)
                    .user(user) // user 객체 필요 -> 이전에는 session으로 id를 받아왔지만 session 객체 그대로 넣으면 됨
                    .build();
        }
    }
    
    @Data
    public static class UpdateDTO {
        // title=제목1&content=내용1 -> isPublic은 null이다. (name(key)이 아예 없으면)
        // title=제목1&content=내용1&isPublic -> isPublic은 ""(비어있음)이다. (name(key)은 있고 value가 없으면)
        // title=제목1&content=내용1&isPublic=  -> isPublic은 " "(space)이다. (name(key)은 있고 value가 없으면)
        @NotEmpty(message="제목을 입력하세요") // null, " "(space), ""(비어있음)
        private String title;
        @NotEmpty(message="내용을 입력하세요")
        private String content;
        private String isPublic;

        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .isPublic(isPublic == null ? false : true)
                    .user(user)
                    .build();
        }
    }
}
