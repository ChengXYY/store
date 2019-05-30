package com.cy.store.exception;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private static final Log logger = LogFactory.getLog(ExceptionHandler.class);

    private String basePackage = "com.cy.store";

    @Override
    public Response toResponse(Exception exception) {
        ErrorCodes code = ErrorCodes.SERVER_IS_WRONG;
        Status statusCode = Status.OK;
        String errMsg = "";

        if(exception instanceof BaseException){
            code = ((BaseException) exception).getErrorCode();
            errMsg = ((BaseException) exception).getMsg();
        }else if(exception instanceof WebApplicationException){
            code = ErrorCodes.HTTP_IS_WRONG;
            if(exception instanceof NotFoundException){
                statusCode = Status.NOT_FOUND;
            }else if(exception instanceof NotAllowedException){
                statusCode = Status.METHOD_NOT_ALLOWED;
            }else if(exception instanceof NotAcceptableException){
                statusCode = Status.NOT_ACCEPTABLE;
            }else if(exception instanceof InternalServerErrorException){
                statusCode = Status.INTERNAL_SERVER_ERROR;
            }
        }

        Integer errorCode = code.getRetCode();
        errMsg = StringUtils.isBlank(errMsg)?code.getRetMsg():errMsg;
        //只打印业务代码异常栈
        exception.setStackTrace(Lists.newArrayList(exception.getStackTrace()).stream().filter(s -> s.getClassName().contains(basePackage)).collect(Collectors.toList()).toArray(new StackTraceElement[]{}));
        logger.error(errorCode,exception);
        return Response.ok(new RestResp<>(errorCode,errMsg)).status(statusCode).build();
    }

}
