package com.bear.animal.dao.repository;

import com.bear.animal.dao.entity.BehaviorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 行为数据库表操作
 * @author bear
 * @date 2021年04月09日 10:32
 */
@Repository
public interface BehaviorRepository extends JpaRepository<BehaviorEntity, Long> {

}
