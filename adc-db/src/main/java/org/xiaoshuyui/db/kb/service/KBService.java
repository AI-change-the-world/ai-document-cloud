package org.xiaoshuyui.db.kb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.xiaoshuyui.db.kb.entity.KB;
import org.xiaoshuyui.db.kb.entity.KBContent;

public interface KBService extends IService<KB> {
  List<KB> getKBTreeByOrganization(Long organizationId);

  boolean saveKB(KB kb);

  List<KB> getOrgAndSubKBs(Long orgId);

  KBContent getKBContent(Long kbId);
}
