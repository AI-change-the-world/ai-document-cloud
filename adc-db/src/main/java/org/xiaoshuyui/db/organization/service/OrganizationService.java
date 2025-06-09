package org.xiaoshuyui.db.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.xiaoshuyui.db.organization.entity.Organization;

public interface OrganizationService extends IService<Organization> {

  List<Organization> getOrganizationTree();

  boolean assignAdmin(Long organizationId, Long adminId);
}
