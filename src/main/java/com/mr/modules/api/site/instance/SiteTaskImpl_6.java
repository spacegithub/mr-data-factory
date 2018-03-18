package com.mr.modules.api.site.instance;

import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 上交所
 * 交易监管
 */

@Slf4j
@Component("site6")
@Scope("prototype")
public class SiteTaskImpl_6 extends SiteTaskExtend {

	protected OCRUtil ocrUtil = SpringUtils.getBean(OCRUtil.class);

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site6 task**************");
		String targetUri = "http://www.sse.com.cn/disclosure/credibility/regulatory/punishment/c/4123713.doc";
		String fileName = downLoadFile(targetUri);
		log.info(ocrUtil.getTextFromDoc(fileName));
		return null;
	}

}
