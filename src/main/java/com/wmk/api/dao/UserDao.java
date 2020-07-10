package com.wmk.api.dao;

import com.wmk.api.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    public List<User> userLogin();

    public User selectUserById(@Param("user_id") int user_id);

    public Integer userLogin(@Param("user_name") String user_name);
}
