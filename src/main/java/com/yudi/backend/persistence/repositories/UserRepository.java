package com.yudi.backend.persistence.repositories;

import com.yudi.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Yudi on 28/12/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    public User findByUsername(String username);

}
