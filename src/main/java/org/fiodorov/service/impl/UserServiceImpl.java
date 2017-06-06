package org.fiodorov.service.impl;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

import org.fiodorov.converter.UserConverter;
import org.fiodorov.model.GenericModel;
import org.fiodorov.model.User;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.view.UserResponseView;
import org.fiodorov.view.UserView;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Roman Fiodorov on 15.12.2015.
 */

public class UserServiceImpl implements UserServiceApi{

    private final UserRepository userRepository;

    private final UserConverter userConverter;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.userConverter = new UserConverter();
    }

    @Transactional
    public void changeUserRank(GenericModel model, int value){
        User author = model.getCreatedBy();
        int actualRank = author.getRank();
        author.setRank(actualRank + value);
        userRepository.save(author);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findOneByEmail(email));
    }

    @Override
    public UserResponseView getActualUserResponseView(Principal principal) {
        if(principal == null){
            return null;
        }
        Optional<User> user = Optional.ofNullable(userRepository.findOneByEmail(principal.getName()));
        if(user.isPresent()){
            UserView userView = userConverter.convert(user.get());
            return new UserResponseView(userView);
        }
        return null;
    }

}
