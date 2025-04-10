package shop.mtcoding.blog._core.error.ex;

// 내가 만든 Exception -> extends 해야 진짜 Exception이 됨
public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }
}
