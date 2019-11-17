package io.dorbae.gb.bookretrieval.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 *****************************************************************
 *
 * GBAccessDeniedHandler
 *
 * @description 403 에러 핸들러
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 4:11     dorbae	최초 생성
 * @since 2019/11/14 4:11
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Controller
public class GBAccessDeniedHandler implements AccessDeniedHandler {
    private static Logger LOG = LoggerFactory.getLogger(GBAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            LOG.info("User '{}' attempted to access the protected URL[{}]", auth.getName(), httpServletRequest.getRequestURI());
        }

        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/access-denied");
    }
}
