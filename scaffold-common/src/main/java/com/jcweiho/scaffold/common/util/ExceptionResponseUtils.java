package com.jcweiho.scaffold.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcweiho.scaffold.common.exception.ResponsePackException;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Security通用发送响应体工具类
 *
 * @author Weiho
 */
@UtilityClass
public class ExceptionResponseUtils {
    /**
     * Spring Security统一错误返回体
     *
     * @param response HttpServletResponse
     * @param message  错误信息
     */
    public void sendResponse(HttpServletResponse response, String message) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String resBody = mapper.writeValueAsString(Result.of(ResultCodeEnum.SYSTEM_FORBIDDEN, message));
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resBody);
            printWriter.flush();
            printWriter.close();
        } catch (JsonProcessingException ignored) {
            throw new ResponsePackException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
