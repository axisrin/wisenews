package site.magazine.wisenews.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import site.magazine.wisenews.Models.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
}