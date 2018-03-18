package com.mr.modules.api.site.instance;

import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 证监会
 */

@Slf4j
@Component
public class SiteTaskImpl_10 extends SiteTaskExtend {

	protected OCRUtil ocrUtil = SpringUtils.getBean(OCRUtil.class);

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site10 task**************");
		String targetUri = "http://www.csrc.gov.cn/pub/newsite/gszqjgb/rcjg/zjjgrcjg/201803/P020180302563551437859.pdf";
		String fileName = downLoadFile(targetUri);
		log.info(ocrUtil.getTextFromImg(fileName));
		return null;
	}

}
