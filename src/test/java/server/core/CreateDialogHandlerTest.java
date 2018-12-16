package server.core;

import common.objects.Body;
import common.objects.FullDialog;
import common.objects.requests.CreateDialogRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class CreateDialogHandlerTest {
    private ClassPathXmlApplicationContext applicationContext;
    private CreateDialogHandler dialogHandler;

    @Before
    public void init(){
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        dialogHandler = applicationContext.getBean(CreateDialogHandler.class);
        dialogHandler.setLogin("newlogin");
    }
    @Test
    public void shouldReturnSomeId() throws SQLException {
        //Given
        CreateDialogRequest request = new CreateDialogRequest();
        request.setPartner("typa_flex");
        request.setInitialMessage("Здравствуй");
        //When
        FullDialog body = (FullDialog) dialogHandler.onHandle(request);
        //Then
        assertTrue(body.getDialog().getDialogId() > 0);
    }

}