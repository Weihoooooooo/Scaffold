package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.mapper.UserMapper;
import com.weiho.scaffold.system.service.UserService;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-07-19
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<UserMapper, User> implements UserService {
}
