package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select u from UserRole u where u.username = :email")
    List<UserRole> findByEmail(@Param("email") String email);

    @Query("select u from UserRole u where u.username = :email")
    List<UserRole> findByUsername(@Param("email") String email);

    @Query(value = "select u.* from user_roles u where u.user_id = :userid", nativeQuery = true)
    List<UserRole> findByUserId(@Param("userid") Long userId);


}
