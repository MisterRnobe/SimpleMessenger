package server.core;

import common.objects.Body;
import common.objects.requests.UserPasswordData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

public class LoginHandlerTest {
    private ClassPathXmlApplicationContext applicationContext;
    private LoginHandler loginHandler;

    @Before
    public void init(){
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        loginHandler = applicationContext.getBean(LoginHandler.class);
    }
    @Test
    public void shouldReturnNullBody(){
        //Given
        UserPasswordData userPasswordData = new UserPasswordData();
        userPasswordData.setLogin("nikita");
        userPasswordData.setPassword("123456");
        //When
        Body b = loginHandler.onHandle(userPasswordData);
        //Then
        assertEquals(Body.NULL_BODY, b);
    }
    @Test(expected = HandleError.class)
    public void shouldThrowHandlerException(){
        //Given
        UserPasswordData userPasswordData = new UserPasswordData();
        userPasswordData.setLogin("1");
        userPasswordData.setPassword("2");
        //When
        Body b = loginHandler.onHandle(userPasswordData);
    }

}