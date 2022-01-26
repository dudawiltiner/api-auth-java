package api.authjava.service;

import api.authjava.model.User;
import api.authjava.auxi.UserWithOutPass;

import java.util.Optional;


public interface UserService {
    Boolean save(User user);
    User update(UserWithOutPass user);
    Boolean updatePassword(String email, String password);
    Optional<User> getEmailAndPassword(String email, String password);
}
