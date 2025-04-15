package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component // IoC에 뜸
@Aspect // proxy
public class GlobalValidationHandler { // 전역으로 유효성 handle

    // AOP 정규표현식 한 번 찾아보기
    // MyBefore 전에 실행?
    // 매개변수 정보까지 알 수 있음 (메서드의 필드안에 있는 ..)
    @Before("@annotation(shop.mtcoding.blog._core.error.anno.MyBefore)") // 패키지명을 풀로 적는다.
    public void beforeAdvice(JoinPoint jp) {
        String name = jp.getSignature().getName(); // annotation이 어디 붙을지 모르니까 메서드일지 클래스일지 등 -> 그래서 Signature
        System.out.println("Before Advice : " + name); // name = 메서드명
    } // reflection 된 메서드의 정보가 jp에 다 있음 (투영되어있다?)

    @After("@annotation(shop.mtcoding.blog._core.error.anno.MyAfter)") // 함수가 종료되고 나서 실행
    public void afterAdvice(JoinPoint jp) {
        System.out.println("매개변수 크기 : " + jp.getArgs().length); // 매개변수에 errors가 있으면 ~으로 처리
        String name = jp.getSignature().getName();
        System.out.println("After Advice : " + name);
    }

    // 앞뒤 다 관리
    @Around("@annotation(shop.mtcoding.blog._core.error.anno.MyAround)")
    public Object aroundAdvice(ProceedingJoinPoint jp) {
        String name = jp.getSignature().getName();
        System.out.println("Around Advice 직전 : " + name);

        try {
            // around에만 있는 메서드 proceed
            // 호출 직전이 before
            // 뭐가 응답될지 모르므로 Object type
            // proceed = invoke인데 proxy한테 맡긴것과 같다.
            Object result = jp.proceed(); // 이 자리에서 controller 함수가 직접 호출됨
            // 호출 직후가 after
            System.out.println("Around Advice 직후 : " + name);
            System.out.println("result : " + result);
            return result; // return 해야 DS에 넘어감
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
