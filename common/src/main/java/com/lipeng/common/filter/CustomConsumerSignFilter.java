package com.lipeng.common.filter;

import com.lipeng.common.utils.twitter.SnowflakeIdUtils;
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
 * @Date: 2021/03/05 10:26
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER, order = -10000)
public class CustomConsumerSignFilter implements Filter {

	public static final String SIGN = "sign";
	public static final String TRACEID = "traceId";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String sign = DigestUtils.md5DigestAsHex(invoker.getUrl().getServiceKey().getBytes());
		String traceId = SnowflakeIdUtils.nextId();
		RpcContext.getContext().setAttachment(SIGN, sign);
		RpcContext.getContext().setAttachment(TRACEID, traceId);
		log.info("DUBBO消费端调用提供方host:{},port:{},API:{},参数透传:[traceId:{},sign:{}]",
				invoker.getUrl().getHost(), invoker.getUrl().getPort(),
				invoker.getUrl().getServiceKey(), traceId, sign);
		return invoker.invoke(invocation);
	}

}