package shop.mtcoding.blog.board.reply;

import lombok.Data;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private Integer id;
        private String content;
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
