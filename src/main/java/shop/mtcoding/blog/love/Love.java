package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

import java.sql.Timestamp;


@NoArgsConstructor
@Getter
@Table(
        name = "love_tb",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "board_id"})
        }
)
@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // 항상 가지고 와야하지만(EAGER) id만으로도 조회가능하니까(LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @CreationTimestamp
    private Timestamp createdAt;

    // Builder 만들기 : 생성자 만들고 @Builder 걸기
    @Builder
    public Love(Integer id, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.createdAt = createdAt;
    }
}
