package org.xiaoshuyui.db.organization.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.db.organization.entity.Organization;
import org.xiaoshuyui.db.organization.mapper.OrganizationMapper;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
    implements OrganizationService {

  @Override
  public List<Organization> getOrganizationTree() {
    List<Organization> all = list();
    Map<Long, List<Organization>> groupByParent =
        all.stream()
            .collect(Collectors.groupingBy(o -> o.getParentId() == null ? 0L : o.getParentId()));

    return buildTree(groupByParent, 0L);
  }

  private List<Organization> buildTree(Map<Long, List<Organization>> group, Long parentId) {
    List<Organization> children = group.getOrDefault(parentId, new ArrayList<>());
    for (Organization org : children) {
      List<Organization> sub = buildTree(group, org.getId());
      org.setChildren(sub);
    }
    return children;
  }

  @Override
  public boolean assignAdmin(Long organizationId, Long adminId) {
    Organization org = getById(organizationId);
    if (org == null) {
      throw new IllegalArgumentException("组织不存在");
    }
    org.setAdminId(adminId);
    return updateById(org);
  }
}
