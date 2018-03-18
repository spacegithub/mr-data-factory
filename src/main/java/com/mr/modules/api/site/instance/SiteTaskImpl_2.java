package com.mr.modules.api.site.instance;

import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 地方证监局
 * 行政处罚决定
 */

@Slf4j
@Component
public class SiteTaskImpl_2 extends SiteTaskExtend {

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site2 task**************");
		String targetUri = "http://www.csrc.gov.cn/pub/beijing/bjxzcf/201803/t20180314_335261.htm";
		log.info(getData(targetUri));
		return null;
	}

}
