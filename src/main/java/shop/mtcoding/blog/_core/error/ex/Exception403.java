package shop.mtcoding.blog._core.error.ex;

// 403 : 권한 없음 -> controller가 할 수 없음 service 체크해서
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }
}
