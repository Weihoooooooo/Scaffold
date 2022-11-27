package com.weiho.scaffold.mp.handler;

import com.weiho.scaffold.common.util.LikeCipherUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Weiho
 * @since 2022/10/24
 */
public class NameHandler extends BaseTypeHandler<Object> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, LikeCipherUtils.likeEncrypt((String) o));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return LikeCipherUtils.likeDecrypt(resultSet.getString(s));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return LikeCipherUtils.likeDecrypt(resultSet.getString(i));
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return LikeCipherUtils.likeDecrypt(callableStatement.getString(i));
    }
}
