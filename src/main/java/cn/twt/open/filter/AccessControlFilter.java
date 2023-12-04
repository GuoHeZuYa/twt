package cn.twt.open.filter;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.AccessControlDao;
import cn.twt.open.pojo.Access;
import cn.twt.open.utils.APIResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author Lino
 */
@WebFilter(urlPatterns = "/api/*", filterName = "accessControlFilter")
@Slf4j
public class AccessControlFilter implements Filter {
    @Autowired
    AccessControlDao accessControlDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("Request Method: "+request.getMethod());
        log.info("Origin: "+request.getHeader("Origin"));
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String site = request.getHeader("domain");
            log.info("site "+site);
            List<Access> accessInfo = accessControlDao.getAccessInfo(site);
            if (accessInfo.isEmpty()){
                log.info(site+"访问接口未遂");
                servletResponse.setContentType("application/json; charset=UTF-8");
                servletResponse.getWriter().println(
                        JSON.toJSONString(
                                APIResponse.error(ErrorCode.NO_SUCH_URL)
                        )
                );
            } else {
                log.info(site+"访问接口");
                String app_key = accessInfo.get(0).getAppKey();
                String app_secret = accessInfo.get(0).getAppSecret();
                servletRequest.setAttribute("app_key", app_key);
                servletRequest.setAttribute("app_secret", app_secret);
                // 这条必须写在里面，如果写在外面，请求会继续向下dispatch，
                // 可能导致response的writer被调用两次，而缓冲区已经有内容，出现冲突
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
