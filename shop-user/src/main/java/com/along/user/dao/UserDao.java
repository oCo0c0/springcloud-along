package com.along.user.dao;

import com.along.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/5/30
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

}
