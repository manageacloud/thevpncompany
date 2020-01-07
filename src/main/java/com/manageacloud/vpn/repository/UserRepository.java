package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 27/06/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = :email")
    List<User> findByEmail(@Param("email") String email);

    @Query(value = "select u.* from users u where u.email = :email LIMIT 1", nativeQuery = true)
    User findOneByEmail(@Param("email") String email);

    @Query(value = "select u.* from users u where u.login_token = :login_token LIMIT 1", nativeQuery = true)
    User findOneByLoginToken(@Param("login_token") String loginToken);

    User findByUsername(String username);

    @Query("select u from User u where u.cookie = :cookie")
    List<User> findByCookie(@Param("cookie") String cookie);

    @Query(value = "select u.* from users u where md5(u.email) = :emailhash", nativeQuery = true)
    List<User> findByMd5Email(@Param("emailhash") String emailhash);

    @Query(value = "select u.* from users u where u.phone = :phone", nativeQuery = true)
    List<User> findByPhone(@Param("phone") String phone);

    @Query(value = "select u.* from users u WHERE u.email = lower(trim(?1))", nativeQuery = true)
    List<User> findByEmailIgnoreCaseContaining(String email);

    @Query(value = "select u.* from users u WHERE u.email = ?1 and  u.email IN (SELECT username FROM user_roles WHERE role = 'ROLE_ADMIN')", nativeQuery = true)
    List<User> findAdminUserByEmail(String email);

}
