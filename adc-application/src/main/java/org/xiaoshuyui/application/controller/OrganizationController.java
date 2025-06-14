package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.common.ApiResponse;
import org.xiaoshuyui.db.organization.service.OrganizationServiceImpl;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

  @Resource private OrganizationServiceImpl orgService;

  @GetMapping("/tree")
  public ApiResponse getOrgTree() {
    return ApiResponse.success(orgService.getOrganizationTree()) ;
  }
}
