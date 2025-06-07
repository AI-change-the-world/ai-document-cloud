package org.xiaoshuyui.db.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.db.entity.User;
import org.xiaoshuyui.db.mapper.UserMapper;

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
}
