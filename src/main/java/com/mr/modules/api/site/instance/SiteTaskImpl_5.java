package com.mr.modules.api.site.instance;

import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by feng on 18-3-16
 * 上交所
 *
 */

@Slf4j
@Component("site5")
@Scope("prototype")
public class SiteTaskImpl_5 extends SiteTaskExtend {

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site5 task**************");

		//post请球，
		//1、直接分析表格内容即可
		//2、分析链接后的页面内容
		String targetUri1 = "";
		Map<String, String> requestParams = new LinkedHashMap<>();
		log.info(postData(targetUri1, requestParams));

		return null;
	}

}
