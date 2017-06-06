package org.fiodorov.repository;

import org.fiodorov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Roman Fiodorov on 24.11.2015.
 */
@Repository(value = "userRepository")
public interface UserRepository  extends JpaRepository<User,Long>{

    User findOneByEmail(String username);

    @Query("select u from User u where u.facebookId = ?1")
    User findOneByFacebookId(String facebookId);
}
