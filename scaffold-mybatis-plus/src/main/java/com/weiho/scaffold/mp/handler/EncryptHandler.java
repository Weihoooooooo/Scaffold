package com.weiho.scaffold.mp.handler;

import com.weiho.scaffold.common.util.AesUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mybatis 字段加解密 Handler
 * 一定要在实体类的上面的@TableName里面加autoResultMap = true,否则查出来是不解密的
 * 然后在要加密的字段上面的@TableField(value = "real_name")加入typeHandler = EncryptHandler.class
 * 即可完成插入自动加密，查询自动解密
 * <p>
 * 若使用xml自己写sql,需要在resultMap中给typeHandler赋值该类,否则也是不解密
 *
 * @author Weiho
 * @since 2022/8/23
 */
public class EncryptHandler extends BaseTypeHandler<Object> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, AesUtils.encrypt((String) o));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String columnValue = resultSet.getString(s);
        return AesUtils.decrypt(columnValue);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        return AesUtils.decrypt(columnValue);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        return AesUtils.decrypt(columnValue);
    }
}
