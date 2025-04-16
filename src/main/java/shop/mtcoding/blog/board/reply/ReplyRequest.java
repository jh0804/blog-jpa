package shop.mtcoding.blog.board.reply;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

public class ReplyRequest {

    @Data
    public static class SaveDTO {

        @NotNull(message ="내용을 입력하세요")
        private String content;
        @NotNull(message = "boardId가 전달되어야 합니다.")
        private Integer boardId;

        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .content(content)
                    .user(sessionUser)
                    .board(board) // .board(Board.builder().id(boardId).build())
                    .build();
        }
    }
}
