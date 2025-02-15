package top.ckxgzxa.filestoragesharesystem.interceptor;

import cn.hutool.http.HttpStatus;
import cn.hutool.log.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ckxgzxa.filestoragesharesystem.common.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 赵希奥
 * @date 2023/3/19 18:13
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        try {
            // 从请求头中获取token
            token = request.getHeader("Authorization");

            // 验证token的合法性和有效性
            if (JwtUtils.validateJwt(token)) {
                // 验证通过，放行
                return true;
            }
        } catch (Exception e) {
            Log.get().error("从请求头中获取token失败", e);
        }

        // 设置响应状态码为未授权
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        // 设置响应内容为json格式
        response.setContentType("application/json;charset=utf-8");

        // ObjectMapper mapper = new ObjectMapper(); // 创建ObjectMapper对象
        // Map<String, Object> map = new HashMap<>(); // 创建一个Map用于存储错误信息
        // map.put("success", false);
        // map.put("message", "请先登录！");
        // response.getWriter().write(mapper.writeValueAsString(map)); // 将Map转换为JSON并写入响应中

        // 获取响应输出流
        PrintWriter out = response.getWriter();
        // 并输出响应内容
        out.write("{\"code\":401,\"msg\":\"token无效或过期\"}");
        out.flush();
        out.close();
        // 拦截请求
        return false;
    }
}
