package shop.mtcoding.blog._core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Resp<T> { // 응답의 공통 DTO
    private Integer status;
    private String msg;
    private T body; // 타입 결정 X -> 동적으로 받고 싶음

    //public static Resp<?> ok(T body) => 들어올 때 타입 결정 안나므로 T에서 오류남
    //제네릭은 static으로 호출하는게 불가능하다
    //제네릭은 new할 때 타입이 결정된다.
    // static은 외부에서 호출하는 순간 new 안되어있음 -> T 타입 안정해져있음 -> T에서 오류나야하지만 <T>에 의해 받을 때 일단 Object 타입으로 받아짐
    public static <B> Resp<?> ok(B body) { // <T> : new 될 때 T로 타입이 결정남
        return new Resp<>(200, "성공", body); // 여기에 body 들어올 때 new가 되면서 타입이 T로 결정됨 (다운캐스팅 됨)
    }

    public static Resp<?> fail(Integer status, String msg) {
        return new Resp<>(status, msg, null);
    }
}