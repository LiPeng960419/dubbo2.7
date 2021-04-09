package com.lipeng.common.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.util.DigestUtils;

/**
 * @Author: lipeng
 * @Date: 2021/03/04 13:59
 */
@Activate(group = CommonConstants.PROVIDER, order = 2)
@Slf4j
public class CustomProviderLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(CustomConsumerSignFilter.TRACEID);
        String sign = RpcContext.getContext().getAttachment(CustomConsumerSignFilter.SIGN);
        String signNew = DigestUtils.md5DigestAsHex(invoker.getUrl().getServiceKey().getBytes());
        String serviceKey = invoker.getUrl().getServiceKey();
        Object[] args = invocation.getArguments();
        String prefix = "[traceId:" + traceId + "] DUBBO调用日志:[" + serviceKey + "]";
        if (sign == null || !sign.equals(signNew)) {
			log.error(prefix + " 校验sign失败");
			throw new RpcException("校验sign失败");
		}
        log.info(prefix + " 入参=>" + JSON.toJSONString(args));
        long start = System.currentTimeMillis();
        Result r = invoker.invoke(invocation);
        if (r.hasException()) {
            Throwable e = r.getException();
            if (e.getClass().getName().equals("java.lang.RuntimeException")) {
                log.error(prefix + " 运行时异常=>" + JSON.toJSONString(r));
            } else {
                log.error(prefix + " 异常=>" + JSON.toJSONString(r));
            }
        } else {
            log.info(prefix + " 返参=>" + JSON.toJSONString(r.getValue()));
            log.info(prefix + " 调用耗时=>" + (System.currentTimeMillis() - start) + "ms");
        }
        return r;

    }

}