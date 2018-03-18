package com.mr.modules.api.service.impl;

import com.mr.common.util.EhCacheUtils;
import com.mr.modules.api.SiteTaskDict;
import com.mr.modules.api.TaskStatus;
import com.mr.modules.api.service.SiteService;
import com.mr.modules.api.site.SiteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by feng on 18-3-16
 */

@Service
public class SiteServiceImpl implements SiteService {

	private static final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

	/**
	 * @param groupIndex SiteTask enum index 信息
	 * @param callId     调用ID,系统唯一
	 * @return
	 */
	@Override
	public String start(String groupIndex, String callId) throws Exception {
		SiteTask task = null;

		if (!Objects.isNull(getTask(callId))) {
			log.error("task exists...");
			return "task exists...";
		}

		try {
			task = (SiteTask) Class.forName(SiteTaskDict.getName(groupIndex)).newInstance();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			return "SiteTask object instance not found";
		}

		EhCacheUtils.put(callId, task);
		return TaskStatus.getName(task.start());
	}

	public Boolean isFinish(String callId) throws Exception {
		SiteTask task = getTask(callId);
		if (Objects.isNull(getTask(callId))) {
			log.error("task not exists...");
			return false;
		}

		if (task.isFinish()) {
			SiteTask.putFinishQueue(callId);
			return true;
		}

		return false;
	}

	@Override
	public String getResultCode(String callId) throws Exception {
		if (Objects.isNull(getTask(callId))) {
			log.error("task not exists...");
			return "task not exists...";
		}

		if (!isFinish(callId)) {
			return TaskStatus.CALL_ING.name;
		}
		return TaskStatus.getName(getTask(callId).getResultCode());
	}

	@Override
	public String getThrowableInfo(String callId) throws Exception {
		if (Objects.isNull(getTask(callId))) {
			log.error("task not exists...");
			return "task not exists...";
		}

		if (!isFinish(callId)) {
			return "executing...";
		}
		return getTask(callId).getThrowableInfo();
	}

	@Override
	public Boolean delSiteTaskInstance(String callId) throws Exception {
		try {
			if (Objects.isNull(getTask(callId))) {
				log.error("task not exists...");
				return false;
			}
			SiteTask.delSiteTaskInstance(callId);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private SiteTask getTask(String callId) throws Exception {
		return ((SiteTask) EhCacheUtils.get(callId));
	}

}
