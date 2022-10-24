package com.weiho.scaffold.system.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 业主表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Api(tags = "业主服务接口")
@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

}
