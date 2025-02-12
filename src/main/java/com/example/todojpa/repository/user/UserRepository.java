package com.example.todojpa.repository.user;

import com.example.todojpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    List<User> findAllByName(String name);
    Optional<User> findByEmail(String email);

    @Query(" select u.lastLogoutTime " +
            "  from User u " +
            " where u.id = :id " +
            "   and u.deleted = false ")
    Timestamp findLastLogoutTimeById(Long id);

    @Query(value = " select * " +
            "  from user u" +
            " where u.email = :email", nativeQuery = true)
    Optional<User> findByEmailForCheckDuplicate(String email);

}
