package org.xiaoshuyui.db.kb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;

@TableName("kb")
@Data
public class KB {

  @TableId(value = "kb_id", type = IdType.AUTO)
  private Long id;

  @TableField("kb_name")
  private String name;

  @TableField("kb_description")
  private String description;

  @TableField("organization_id")
  private Long organizationId;

  @TableField("parent_id")
  private Long parentId; // null 表示顶层目录

  @TableField("level")
  private Integer level; // 目录层级，1~3

  @TableField("updated_at")
  private LocalDateTime updatedAt;

  @JsonIgnore
  @TableField("is_deleted")
  private Integer isDeleted;

  @TableField("created_at")
  private LocalDateTime createdAt;

  @TableField("created_by")
  private Long createdBy;
}
