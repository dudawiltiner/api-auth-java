package api.authjava.service;

import api.authjava.model.User;
import api.authjava.auxi.UserWithOutPass;
import api.authjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public Boolean save(User user) {
        Optional<User> newUser = this.userRepository.findByEmail(user.getEmail());
        if(newUser.isPresent()) {
            return false;
        }

        this.userRepository.save(user);
        return true;
    }

    @Override
    public User update(UserWithOutPass user) {
        Optional<User> updateUser = this.userRepository.findByEmail(user.getEmail());
        if(updateUser.isPresent()){
            User upUser = updateUser.get();
            upUser.setFullName(user.getFullName());
//            upUser.setImage(user.getImage());
            upUser.setOrigin(user.getOrigin());
            upUser.setPosition(user.getPosition());
            upUser.setSex(user.getSex());

            return this.userRepository.save(upUser);
        }

        return new User();
    }

    @Override
    public Boolean updatePassword(String email, String password) {
        Optional<User> updateUser = this.userRepository.findByEmail(email);
        if(updateUser.isPresent()) {
            User user = updateUser.get();
            user.setPassword(password);
            this.userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public Optional<User> getEmailAndPassword(String email, String password) {
        return this.userRepository.findByEmailAndPassword(email, password);
    }

}
