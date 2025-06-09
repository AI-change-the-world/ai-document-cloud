package org.xiaoshuyui.db.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("user")
public class User {
  @TableId(value = "user_id", type = IdType.AUTO)
  Long id;

  @TableField("user_name")
  String username;

  @TableField("role_id")
  Integer roleId;

  @TableField("dept_id")
  Integer deptId;

  @JsonIgnore
  @TableField("is_deleted")
  Integer isDeleted;

  @TableField("created_at")
  LocalDateTime createdAt;

  @TableField("updated_at")
  LocalDateTime updatedAt;
}
