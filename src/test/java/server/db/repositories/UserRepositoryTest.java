package server.db.repositories;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.db.dto.UserDB;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;


public class UserRepositoryTest {
    private ClassPathXmlApplicationContext applicationContext;


    private UserRepository userRepository;

    @Before
    public void init(){
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        userRepository = applicationContext.getBean(UserRepository.class);

    }
    @Test
    public void shouldFetchAllUsers(){
        //When
        Iterable<UserDB> users = userRepository.findAll();
        List<UserDB> userDBList = new LinkedList<>();
        users.forEach(userDBList::add);
        userDBList.forEach(System.out::println);
        //Then
        assertFalse(userDBList.isEmpty());
    }

}
