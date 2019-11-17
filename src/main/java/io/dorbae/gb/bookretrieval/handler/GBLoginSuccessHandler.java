package io.dorbae.gb.bookretrieval.handler;

import io.dorbae.gb.bookretrieval.GBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 *****************************************************************
 *
 * GBLoginSuccessHandler
 *
 * @description 로그인 성공 핸들러
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 8:51     dorbae	최초 생성
 * @since 2019/11/14 8:51
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public class GBLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GBLoginSuccessHandler.class);
    /**
     * @param defaultTargetUrl
     */
    public GBLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    /**
     * 인증 성공 이벤트
     *
     * @param request
     * @param response
     * @param authentication
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        LOG.debug("onAuthenticationSuccess");
        if (session != null) {
            // 이전 페이지 가져오기
            String redirectUrl = (String) session.getAttribute(GBConstant.SESSION_ATTR_NAME);
            if (redirectUrl != null) {
                session.removeAttribute(GBConstant.SESSION_ATTR_NAME);
                // 이전 페이지로 이동
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
