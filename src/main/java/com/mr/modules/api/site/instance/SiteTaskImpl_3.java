package com.mr.modules.api.site.instance;

import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 保监会
 * 行政处罚决定
 */

@Slf4j
@Component("site3")
@Scope("prototype")
public class SiteTaskImpl_3 extends SiteTaskExtend {

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site3 task**************");

		//保监会处罚
		String targetUri1 = "http://www.circ.gov.cn/web/site0/tab5240/info4099702.htm";
		log.info(getData(targetUri1));

		//保监局处罚
		String targetUri2 = "http://www.circ.gov.cn/web/site0/tab5241/info4102039.htm";
		log.info(getData(targetUri2));

		return null;
	}

}
