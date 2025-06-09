package org.xiaoshuyui.db.kb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kb_file_share")
public class KBFileShare {

  @TableId(type = IdType.AUTO)
  private Long id;

  @TableField("file_id")
  private Long fileId;

  @TableField("from_user_id")
  private Long fromUserId;

  @TableField("to_user_id")
  private Long toUserId;

  @TableField("shared_at")
  private LocalDateTime sharedAt;

  @TableField("is_deleted")
  private Integer isDeleted;

  @TableField("created_at")
  private LocalDateTime createdAt;

  @TableField("updated_at")
  private LocalDateTime updatedAt;
}
