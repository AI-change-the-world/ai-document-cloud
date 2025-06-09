package org.xiaoshuyui.db.organization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")
public class Organization {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;

  private Long parentId;

  private Long adminId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @JsonIgnore private Integer isDeleted;

  @TableField(value = "children", exist = false)
  private List<Organization> children;
}
