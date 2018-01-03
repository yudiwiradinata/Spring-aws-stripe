package com.yudi.backend.persistence.repositories;

import com.yudi.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yudi on 28/12/2017.
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long>{

    User findByUsername(String username);

    User findByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);

}
