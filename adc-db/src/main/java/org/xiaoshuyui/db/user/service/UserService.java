package org.xiaoshuyui.db.user.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.db.user.entity.User;
import org.xiaoshuyui.db.user.entity.UserSummary;
import org.xiaoshuyui.db.user.mapper.UserMapper;

@Service
public class UserService {
  final UserMapper userMapper;

  public UserService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public List<User> getAllUsers() {
    return userMapper.selectList(null);
  }

  public User getUserById(Long id) {
    return userMapper.selectById(id);
  }

  public User getUser(String username, String password){
    QueryWrapper queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_name", username);
    queryWrapper.eq("upassword", password);

    return userMapper.selectOne(queryWrapper);
  }

  public List<UserSummary> getUserSummary() {
    return userMapper.getAllSummary();
  }
}
