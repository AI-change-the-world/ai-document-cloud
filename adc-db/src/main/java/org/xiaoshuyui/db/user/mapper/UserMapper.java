package org.xiaoshuyui.db.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaoshuyui.db.user.entity.User;
import org.xiaoshuyui.db.user.entity.UserSummary;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Select("select user_id as id, user_name as name from user")
    List<UserSummary> getAllSummary();

    List<User> getUsers(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);
}
