package org.xiaoshuyui.db.kb.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoshuyui.db.kb.entity.*;
import org.xiaoshuyui.db.kb.mapper.KBFileMapper;
import org.xiaoshuyui.db.kb.mapper.KBFileMountMapper;
import org.xiaoshuyui.db.kb.mapper.KBFileShareMapper;
import org.xiaoshuyui.db.kb.mapper.KBMapper;
import org.xiaoshuyui.db.organization.entity.Organization;
import org.xiaoshuyui.db.organization.mapper.OrganizationMapper;

@Service
public class KBServiceImpl extends ServiceImpl<KBMapper, KB> implements KBService {

  private final KBMapper kbMapper;
  private final OrganizationMapper organizationMapper;

  private final KBFileMapper kbFileMapper;

  private final KBFileShareMapper kbFileShareMapper;

  private final KBFileMountMapper kbFileMountMapper;

  public KBServiceImpl(
      KBMapper kbMapper,
      OrganizationMapper organizationMapper,
      KBFileMapper kbFileMapper,
      KBFileShareMapper kbFileShareMapper,
      KBFileMountMapper kbFileMountMapper) {
    this.kbMapper = kbMapper;
    this.organizationMapper = organizationMapper;
    this.kbFileMapper = kbFileMapper;
    this.kbFileShareMapper = kbFileShareMapper;
    this.kbFileMountMapper = kbFileMountMapper;
  }

  /**
   * 获取所有子组织ID
   *
   * @param orgId 组织ID
   * @return 子组织ID列表
   */
  public List<Long> getSubOrgIds(Long orgId) {
    List<Organization> all = organizationMapper.selectList(null);
    Set<Long> result = new HashSet<>();
    collectSubOrgs(all, orgId, result);
    return new ArrayList<>(result);
  }

  /**
   * 递归收集子组织ID
   *
   * @param all 所有组织列表
   * @param parentId 父组织ID
   * @param result 子组织ID集合
   */
  private void collectSubOrgs(List<Organization> all, Long parentId, Set<Long> result) {
    result.add(parentId);
    for (Organization org : all) {
      if (Objects.equals(org.getParentId(), parentId)) {
        collectSubOrgs(all, org.getId(), result);
      }
    }
  }

  /**
   * 根据组织ID获取知识库树
   *
   * @param organizationId 组织ID
   * @return 知识库列表
   */
  @Override
  public List<KB> getKBTreeByOrganization(Long organizationId) {
    List<KB> list =
        lambdaQuery()
            .eq(KB::getOrganizationId, organizationId)
            .eq(KB::getIsDeleted, 0)
            .orderByAsc(KB::getLevel)
            .list();

    Map<Long, List<KB>> groupByParent =
        list.stream()
            .collect(Collectors.groupingBy(k -> k.getParentId() == null ? 0L : k.getParentId()));

    return buildTree(groupByParent, 0L);
  }

  /**
   * 构建知识库树
   *
   * @param group 按父ID分组的知识库列表
   * @param parentId 父知识库ID
   * @return 子知识库列表
   */
  private List<KB> buildTree(Map<Long, List<KB>> group, Long parentId) {
    List<KB> children = group.getOrDefault(parentId, new ArrayList<>());
    for (KB node : children) {
      List<KB> sub = buildTree(group, node.getId());
      // 如果你使用 KBVO，可以 setChildren(sub)
    }
    return children;
  }

  /**
   * 保存知识库
   *
   * @param kb 知识库对象
   * @return 保存结果
   */
  @Override
  public boolean saveKB(KB kb) {
    if (kb.getParentId() != null) {
      KB parent = kbMapper.selectById(kb.getParentId());
      if (parent == null) {
        throw new IllegalArgumentException("父级目录不存在");
      }
      if (parent.getLevel() >= 3) {
        throw new IllegalArgumentException("不能超过三级目录");
      }
      kb.setLevel(parent.getLevel() + 1);
    } else {
      kb.setLevel(1); // 顶级目录
    }

    kb.setCreatedAt(LocalDateTime.now());
    kb.setUpdatedAt(LocalDateTime.now());
    return kbMapper.insert(kb) > 0;
  }

  /**
   * 获取组织及其子组织的知识库
   *
   * @param orgId 组织ID
   * @return 知识库列表
   */
  @Override
  public List<KB> getOrgAndSubKBs(Long orgId) {
    List<Long> orgIds = getSubOrgIds(orgId);
    return lambdaQuery()
        .in(KB::getOrganizationId, orgIds)
        .eq(KB::getIsDeleted, 0)
        .orderByAsc(KB::getLevel)
        .list();
  }

  /**
   * 获取知识库内容
   *
   * @param kbId 知识库ID
   * @return 知识库内容对象
   */
  @Override
  public KBContent getKBContent(Long kbId) {
    // 查询直接子目录
    List<KB> children =
        lambdaQuery()
            .eq(KB::getParentId, kbId)
            .eq(KB::getIsDeleted, 0)
            .orderByAsc(KB::getLevel)
            .list();

    // 查询挂载文件 ID 列表
    List<KBFileMount> mounts =
        kbFileMountMapper.selectList(
            new LambdaQueryWrapper<KBFileMount>()
                .eq(KBFileMount::getKbId, kbId)
                .eq(KBFileMount::getIsDeleted, 0));

    if (mounts.isEmpty()) {
      // 没有挂载任何文件
      KBContent content = new KBContent();
      content.setChildren(children);
      content.setFiles(Collections.emptyList());
      return content;
    }

    // 挂载文件ID集合
    List<Long> fileIds =
        mounts.stream().map(KBFileMount::getFileId).distinct().collect(Collectors.toList());

    List<KBFile> files =
        kbFileMapper.selectList(
            new LambdaQueryWrapper<KBFile>()
                .in(KBFile::getId, fileIds)
                .eq(KBFile::getIsDeleted, 0));

    KBContent content = new KBContent();
    content.setChildren(children);
    content.setFiles(files);
    return content;
  }

  /**
   * 检查用户是否有权限分享文件
   *
   * @param fileId 文件ID
   * @param currentUserId 当前用户ID
   * @return 权限检查结果
   */
  public boolean canShareFile(Long fileId, Long currentUserId) {
    KBFile file = kbFileMapper.selectById(fileId);
    if (file == null) return false;

    // 上传者本人
    if (Objects.equals(file.getUserId(), currentUserId)) {
      return true;
    }

    // 当前用户是否是该组织管理员
    Organization org = organizationMapper.selectById(file.getOrganizationId());
    return org != null && Objects.equals(org.getAdminId(), currentUserId);
  }

  /**
   * 分享文件
   *
   * @param fileId 文件ID
   * @param toUserId 接收用户ID
   * @param currentUserId 当前用户ID
   * @return 分享结果
   */
  @Transactional
  public boolean shareFile(Long fileId, Long toUserId, Long currentUserId) {
    if (!canShareFile(fileId, currentUserId)) {
      throw new RuntimeException("无权限分享该文件");
    }

    // 创建或更新分享记录
    KBFileShare share = new KBFileShare();
    share.setFileId(fileId);
    share.setToUserId(toUserId);
    share.setFromUserId(currentUserId);
    share.setSharedAt(LocalDateTime.now());
    share.setIsDeleted(0);
    share.setCreatedAt(LocalDateTime.now());
    share.setUpdatedAt(LocalDateTime.now());

    LambdaQueryWrapper<KBFileShare> shareQuery =
        new LambdaQueryWrapper<KBFileShare>()
            .eq(KBFileShare::getFileId, fileId)
            .eq(KBFileShare::getToUserId, toUserId);

    KBFileShare existingShare = kbFileShareMapper.selectOne(shareQuery);
    if (existingShare != null) {
      share.setId(existingShare.getId());
      kbFileShareMapper.updateById(share);
    } else {
      kbFileShareMapper.insert(share);
    }

    // 挂载到共享目录
    Long sharedKbId = getOrCreateSharedKB(toUserId);

    KBFileMount mount = new KBFileMount();
    mount.setFileId(fileId);
    mount.setOwnerUserId(toUserId);
    mount.setKbId(sharedKbId);
    mount.setIsShared(1);
    mount.setIsDeleted(0);
    mount.setCreatedAt(LocalDateTime.now());
    mount.setUpdatedAt(LocalDateTime.now());

    LambdaQueryWrapper<KBFileMount> mountQuery =
        new LambdaQueryWrapper<KBFileMount>()
            .eq(KBFileMount::getFileId, fileId)
            .eq(KBFileMount::getOwnerUserId, toUserId);

    KBFileMount existingMount = kbFileMountMapper.selectOne(mountQuery);
    if (existingMount != null) {
      mount.setId(existingMount.getId());
      kbFileMountMapper.updateById(mount);
    } else {
      kbFileMountMapper.insert(mount);
    }

    return true;
  }

  /**
   * 取消分享文件
   *
   * @param fileId 文件ID
   * @param toUserId 接收用户ID
   * @param currentUserId 当前用户ID
   * @return 取消分享结果
   */
  @Transactional
  public boolean unshareFile(Long fileId, Long toUserId, Long currentUserId) {
    if (!canShareFile(fileId, currentUserId)) {
      throw new RuntimeException("无权限取消分享");
    }

    // 标记分享记录为删除
    kbFileShareMapper.update(
        null,
        new LambdaUpdateWrapper<KBFileShare>()
            .eq(KBFileShare::getFileId, fileId)
            .eq(KBFileShare::getToUserId, toUserId)
            .set(KBFileShare::getIsDeleted, 1)
            .set(KBFileShare::getUpdatedAt, LocalDateTime.now()));

    // 删除挂载记录
    kbFileMountMapper.update(
        null,
        new LambdaUpdateWrapper<KBFileMount>()
            .eq(KBFileMount::getFileId, fileId)
            .eq(KBFileMount::getOwnerUserId, toUserId)
            .set(KBFileMount::getIsDeleted, 1)
            .set(KBFileMount::getUpdatedAt, LocalDateTime.now()));

    return true;
  }

  /**
   * 获取或创建共享知识库
   *
   * @param userId 用户ID
   * @return 共享知识库ID
   */
  private Long getOrCreateSharedKB(Long userId) {
    KB existing =
        kbMapper.selectOne(
            new LambdaQueryWrapper<KB>()
                .eq(KB::getName, "MyShared")
                .eq(KB::getCreatedBy, userId)
                .eq(KB::getIsDeleted, 0));

    if (existing != null) return existing.getId();

    KB newShared = new KB();
    newShared.setName("MyShared");
    newShared.setDescription("共享文件目录");
    newShared.setCreatedBy(userId);
    newShared.setLevel(1);
    newShared.setIsDeleted(0);
    newShared.setCreatedAt(LocalDateTime.now());
    newShared.setUpdatedAt(LocalDateTime.now());

    kbMapper.insert(newShared);
    return newShared.getId();
  }

  /**
   * 移动文件到指定知识库
   *
   * @param fileId 文件ID
   * @param userId 用户ID
   * @param targetKbId 目标知识库ID
   * @return 移动结果
   */
  public boolean moveFile(Long fileId, Long userId, Long targetKbId) {
    LambdaUpdateWrapper<KBFileMount> wrapper =
        new LambdaUpdateWrapper<KBFileMount>()
            .eq(KBFileMount::getFileId, fileId)
            .eq(KBFileMount::getOwnerUserId, userId)
            .eq(KBFileMount::getIsDeleted, 0)
            .set(KBFileMount::getKbId, targetKbId)
            .set(KBFileMount::getUpdatedAt, LocalDateTime.now());

    return kbFileMountMapper.update(null, wrapper) > 0;
  }
}
