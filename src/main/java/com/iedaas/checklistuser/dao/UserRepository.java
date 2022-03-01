package com.iedaas.checklistuser.dao;

import com.iedaas.checklistuser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from user u where u.user_uid= ?1", nativeQuery = true)
    User findByUUID(String userUid);

    @Query(value = "select * from user u where u.email_id= ?1", nativeQuery = true)
    User findByEmail(String userEmail);
}
