package com.yudi.backend.persistence.repositories;

import com.yudi.backend.persistence.domain.backend.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Yudi on 28/12/2017.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer>{

}
