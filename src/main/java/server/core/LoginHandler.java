package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.requests.UserPasswordData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import server.db.dto.UserDB;
import server.db.repositories.UserRepository;
import server.utils.HashUtils;

import java.util.Optional;

@Component
@Scope(scopeName = "prototype")
public class LoginHandler extends AbstractHandler<UserPasswordData> {
    private final UserRepository userRepository;
    private final HashUtils hashUtils;

    @Autowired
    public LoginHandler(UserRepository userRepository, HashUtils hashUtils) {
        super(UserPasswordData.class, new String[]{"login", "password"}, null);
        this.userRepository = userRepository;
        this.hashUtils = hashUtils;
    }

    @Override
    protected Body onHandle(UserPasswordData body) throws HandleError {
        Optional<UserDB> optionalUser = userRepository
                .findTopByLoginAndPassword(body.getLogin(), hashUtils.hash256(body.getPassword()));
        if (optionalUser.isPresent()) {
            return Body.NULL_BODY;
        } else {
            throw new HandleError(Errors.BAD_PASSWORD);
        }
    }
}
