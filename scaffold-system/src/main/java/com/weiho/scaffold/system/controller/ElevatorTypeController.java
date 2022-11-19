package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/elevator-types")
public class ElevatorTypeController extends CommonController<ElevatorTypeService, ElevatorType> {

}
