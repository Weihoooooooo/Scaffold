package com.weiho.scaffold.system.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 电梯信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
@Api(tags = "电梯管理")
@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
public class ElevatorController {

}
