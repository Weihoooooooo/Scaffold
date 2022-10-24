package com.weiho.scaffold.mp.handler;

import com.weiho.scaffold.common.util.aes.LikeCipher;
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
        preparedStatement.setString(i, LikeCipher.phoneLikeEncrypt((String) o));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String columnValue = resultSet.getString(s);
        return LikeCipher.phoneLikeDecrypt(columnValue);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        return LikeCipher.phoneLikeDecrypt(columnValue);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        return LikeCipher.phoneLikeDecrypt(columnValue);
    }
}
