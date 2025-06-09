package org.xiaoshuyui.db.kb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;

@TableName("kb_file_mount")
@Data
public class KBFileMount {
  @TableId(type = IdType.AUTO)
  private Long id;

  @TableField("file_id")
  private Long fileId;

  @TableField("owner_user_id")
  private Long ownerUserId;

  @TableField("kb_id")
  private Long kbId;

  @TableField("is_shared")
  private Integer isShared;

  @JsonIgnore
  @TableField("is_deleted")
  private Integer isDeleted;

  @TableField("created_at")
  private LocalDateTime createdAt;

  @TableField("updated_at")
  private LocalDateTime updatedAt;
}
