package org.xiaoshuyui.db.user.service;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.db.global.PageResult;
import org.xiaoshuyui.db.user.entity.User;
import org.xiaoshuyui.db.user.entity.UserSummary;
import org.xiaoshuyui.db.user.mapper.UserMapper;

@Service
public class UserService {
  final UserMapper userMapper;

  public UserService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public PageResult getAllUsers(int page, int size, String keyword) {
    PageResult<User> pageResult = new PageResult<>();

    if (page < 0 || size <= 0) {
      pageResult.setRecords(new ArrayList<>());
      pageResult.setTotal(0);
      return pageResult;
    }
    Long total = userMapper.selectCount(new QueryWrapper<User>().eq("is_deleted", 0));
    int offset = (page - 1) * size;
    List<User> users = userMapper.getUsers(offset, size, keyword);
    pageResult.setRecords(users);
    pageResult.setTotal(total);
    return pageResult;
  }

  public User getUserById(Long id) {
    return userMapper.selectById(id);
  }

  public User getUser(String username, String password) {
    QueryWrapper queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_name", username);
    queryWrapper.eq("upassword", password);

    return userMapper.selectOne(queryWrapper);
  }

  public List<UserSummary> getUserSummary() {
    return userMapper.getAllSummary();
  }
}
