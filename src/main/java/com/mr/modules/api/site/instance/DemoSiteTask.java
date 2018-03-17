package com.mr.modules.api.site.instance;

import com.mr.modules.api.site.SiteTask;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by feng on 18-3-16
 */

@Slf4j
public class DemoSiteTask extends SiteTask {

	/**
	 *
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site task**************");
		Thread.sleep(30 * 1000);
		return null;
	}
}
