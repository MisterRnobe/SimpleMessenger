package server.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.core.LoginHandler;
import server.dto.responses.DialogCommonData;

import java.util.List;

import static org.junit.Assert.*;

public class DialogProviderServiceTest {
    private ClassPathXmlApplicationContext applicationContext;
    private DialogProviderService dialogProviderService;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        dialogProviderService = applicationContext.getBean(DialogProviderService.class);
    }

    @Test
    public void shouldProvideDialog() {
        //When
        List<DialogCommonData> data = dialogProviderService.findDialogsForUser("typa_flex", 0, 0);
        //Then
        assertEquals(1, data.size());
    }

}