package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.board.reply.Reply;
import shop.mtcoding.blog.user.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // ORM

    // OneToMany는 컬럼이 만들어지지 X, 조회 용도 -> getter
    // FK의 필드명 -> FK가 어디인지 알려준다 (FK의 주인)
    //
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>(); // 없을 수도 있으니까 초기화

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, String title, String content, Boolean isPublic, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.user = user;
        this.createdAt = createdAt;
    }
}