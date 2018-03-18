package com.mr.modules.api.site.instance;

import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by feng on 18-3-16
 * 上交所
 * 公司监管
 */

@Slf4j
@Component
public class SiteTaskImpl_4 extends SiteTaskExtend {

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site4 task**************");

		//post请球
		//1、直接分析表格内容即可
		//2、提取链接的pdf的文本内容 SiteTaskImpl_1
		String targetUri1 = "";
		Map<String, String> requestParams = new LinkedHashMap<>();
		log.info(postData(targetUri1, requestParams));

		return null;
	}

}
