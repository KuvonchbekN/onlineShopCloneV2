package uz.exadel.user.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import uz.exadel.user.dto.UserDto;


@Slf4j
@Aspect
@Component
public class UserAspect {
        @Before("execution(public * uz.exadel.user.controller.UserController.registerUser(..))")
        public void userTryingToRegister(JoinPoint joinPoint){
                Object[] args = joinPoint.getArgs();
                UserDto user = (UserDto) args[0];

                String fullName = user.getFullName();

//                log.info("{} trying to register to the system!", fullName);
        }
}
