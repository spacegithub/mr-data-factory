package com.mr.modules.api.site.instance;

import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 深交所
 * 上市公司处罚与处分记录
 */

@Slf4j
@Component
public class SiteTaskImpl_7 extends SiteTaskExtend {

	protected OCRUtil ocrUtil = SpringUtils.getBean(OCRUtil.class);

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site7 task**************");
		//可直接载excel
		String exlUri = "http://www.szse.cn/szseWeb/ShowReport.szse?SHOWTYPE=xlsx&CATALOGID=1759_cxda&tab1PAGENO=1&ENCODE=1&TABKEY=tab1";

		//download pdf并解析成文本
		String targetUri = "http://www.szse.cn/UpFiles/cfwj/2018-03-14_300025332.pdf";
		String fileName = downLoadFile(targetUri);
		log.info(ocrUtil.getTextFromPdf(fileName));

		return null;
	}

}
