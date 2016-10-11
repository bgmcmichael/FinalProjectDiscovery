package tiy.Timeline;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by fenji on 10/10/2016.
 */

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    User findByPassword(String password);
    User findByEmail(String email);
}
