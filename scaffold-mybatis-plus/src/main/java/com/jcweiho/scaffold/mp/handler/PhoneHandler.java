package com.jcweiho.scaffold.mp.handler;

import com.jcweiho.scaffold.common.util.LikeCipherUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Weiho
 * @since 2022/10/21
 */
public class PhoneHandler extends BaseTypeHandler<Object> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, LikeCipherUtils.phoneLikeEncrypt((String) o));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String columnValue = resultSet.getString(s);
        return LikeCipherUtils.phoneLikeDecrypt(columnValue);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        return LikeCipherUtils.phoneLikeDecrypt(columnValue);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        return LikeCipherUtils.phoneLikeDecrypt(columnValue);
    }
}
