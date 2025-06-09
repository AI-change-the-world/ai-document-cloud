package org.xiaoshuyui.db.kb.entity;

import java.util.List;
import lombok.Data;

@Data
public class KBContent {
  private List<KB> children; // 子目录
  private List<KBFile> files; // 当前目录下文件
}
