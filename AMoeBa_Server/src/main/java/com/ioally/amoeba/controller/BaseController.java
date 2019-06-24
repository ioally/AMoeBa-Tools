package com.ioally.amoeba.controller;

import com.ioally.amoeba.dto.BaseRequestDto;
import com.ioally.amoeba.dto.ResponseDto;
import com.ioally.amoeba.exception.OutOfSessionException;
import com.ioally.amoeba.exception.UserPermissionException;
import com.ioally.amoeba.service.AMoeBaService;
import com.ioally.amoeba.session.Session;
import com.ioally.amoeba.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@ControllerAdvice
public class BaseController implements ResponseBodyAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AMoeBaService aMoeBaService;

    @Value("${amoeba.sessionTimeout}")
    private int sessionTimeout;

    /**
     * 拦截请求的sessionId
     *
     * @param request HttpServletRequest
     * @throws Exception
     */
    @ModelAttribute
    private void initAMoeBaSession(HttpServletRequest request) throws Exception {
        String jsessionid = request.getSession().getId();
        if (StringUtils.isEmpty(jsessionid)) {
            throw new RuntimeException("获取sessionId失败！");
        }
        LOGGER.debug("请求sessionId：{}", jsessionid);
        BaseRequestDto.sessionId.set(jsessionid);
        Session session = sessionManager.getSession();
        session.setSessionId(jsessionid);
        BaseRequestDto.sessionThreadLocal.set(session);
        Session threadLocalSession = BaseRequestDto.sessionThreadLocal.get();
        String contextPath = request.getServletPath();
        if (threadLocalSession.getaMoeBaSession().isLogin()) {
            threadLocalSession.setLastTime(new Date());
        } else if (!contextPath.equals("/amoeba/login")
                && !contextPath.equals("/amoeba/queryLogin")
                && !contextPath.equals("/amoeba/config/sys")) {
            throw new UserPermissionException("未登录，无权操作！");
        }
        aMoeBaService.session(threadLocalSession.getaMoeBaSession());
        LOGGER.debug("获取AMoeBaSession：id = {}", threadLocalSession.getId());
    }

    /**
     * 全局的异常拦截器
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseDto exception(Exception e) {
        LOGGER.error("处理请求异常！", e);
        ResponseDto responseDto = ResponseDto.newInstance();
        responseDto.setStatusCode("500");
        if (e instanceof UserPermissionException) {
            responseDto.setStatusCode("401"); // 未登录，没有权限
        } else if (e instanceof OutOfSessionException) {
            responseDto.setStatusCode("203"); // 服务端拒绝处理，没有足够的会话池可以用来处理请求
        }
        responseDto.setMessage(e.getMessage());
        return responseDto;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        sessionManager.freedSessionByTimeOut(BaseRequestDto.sessionThreadLocal.get().getId(), sessionTimeout);
        return o;
    }
}
