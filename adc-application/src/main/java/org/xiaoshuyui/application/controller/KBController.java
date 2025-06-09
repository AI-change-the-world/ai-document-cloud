package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.db.kb.service.KBServiceImpl;

@RequestMapping("/knowledge-base")
@RestController
public class KBController {

  @Resource private KBServiceImpl kbService;
}
