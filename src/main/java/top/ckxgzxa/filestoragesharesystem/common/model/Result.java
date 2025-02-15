package top.ckxgzxa.filestoragesharesystem.common.model;

import cn.hutool.http.HttpStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 赵希奥
 * @date 2022/10/12 11:08
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 返回数据
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T result;

    public static <T> Result<T> success(Integer code, String msg) {
        return result(code, msg, null);
    }

    public static <T> Result<T> success(Integer code, String msg, T data) {
        return result(code, msg, data);
    }

    public static <T> Result<T> success(String msg) {
        return result(HttpStatus.HTTP_OK, msg, null);
    }

    private static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.HTTP_OK);
        result.setMessage("操作成功!");
        result.setResult(data);
        return result;
    }


    public static <T> Result<T> success(String msg, T data) {
        return result(HttpStatus.HTTP_OK, msg, data);
    }

    public static <T> Result<T> failed(Integer code, String msg) {
        return result(code, msg, null);
    }

    private static <T> Result<T> failed(Integer code, String msg, T data) {
        return result(code, msg, data);
    }

    private static <T> Result<T> result(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setResult(data);
        result.setMessage(msg);
        return result;
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && HttpStatus.HTTP_OK == result.getCode();
    }
}
