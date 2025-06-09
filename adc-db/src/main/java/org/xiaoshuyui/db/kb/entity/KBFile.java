package org.xiaoshuyui.db.kb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("kb_file")
public class KBFile {

  @TableId(value = "file_id", type = IdType.AUTO)
  private Long id;

  @TableField("file_name")
  private String name;

  @TableField("file_type")
  private String type;

  @TableField("file_url")
  private String url;

  @TableField("kb_id")
  private Long kbId;

  @TableField("organization_id")
  private Long organizationId;

  @TableField("is_deleted")
  @JsonIgnore
  private Integer isDeleted;

  @TableField("created_at")
  private LocalDateTime createdAt;

  @TableField("updated_at")
  private LocalDateTime updatedAt;

  @TableField("user_id")
  private Long userId; // 上传人 ID
}
