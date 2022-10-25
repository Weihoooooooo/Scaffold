package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.criteria.UserQueryCriteria;
import com.weiho.scaffold.system.entity.vo.UserVO;
import com.weiho.scaffold.system.security.vo.JwtUserVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
public interface UserService extends CommonService<User> {
    /**
     * 根据用户实体生成基础的JwtUserVO对象(权限为空)
     *
     * @param user 用户实体
     * @return 权限为空的JwtUserVO对象
     */
    JwtUserVO getBaseJwtUserVO(User user);

    /**
     * 根据用户名查找用户信息
     *
     * @param username 用户名
     * @return 返回的用户信息对象
     */
    User findByUsername(String username);

    /**
     * 用户修改密码
     *
     * @param username        用户名
     * @param encryptPassword 加密后的新密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 用户修改邮箱
     *
     * @param username 用户名
     * @param newEmail 新邮箱
     */
    void updateEmail(String username, String newEmail);

    /**
     * 用户修改头像
     *
     * @param multipartFile 头像
     */
    void updateAvatar(MultipartFile multipartFile);

    /**
     * 获取用户列表
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String, Object> getUserList(UserQueryCriteria criteria, Pageable pageable);

    /**
     * 根据条件查询所有的用户列表
     *
     * @param criteria 条件
     * @return /
     */
    List<UserVO> getAll(UserQueryCriteria criteria);

    /**
     * 编辑用户
     *
     * @param resource 编辑后的用户信息
     */
    boolean updateUser(UserVO resource);

    /**
     * 导出数据
     *
     * @param all      用户表头
     * @param response /
     */
    void download(List<UserVO> all, HttpServletResponse response) throws IOException;

    /**
     * 新增用户
     *
     * @param resource 用户信息
     * @return Boolean
     */
    boolean createUser(UserVO resource);

    /**
     * 删除用户
     *
     * @param ids 用户ID
     */
    boolean delete(Set<Long> ids);
}
