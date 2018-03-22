package com.mr.modules.api.site.instance;

import com.google.common.collect.Maps;
import com.mr.modules.api.site.SiteTaskExtend;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by feng on 18-3-16
 * 上交所
 * 公司监管
 */

@Slf4j
@Component("site4")
@Scope("prototype")
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
		//2、提取链接的pdf的文本内容 SiteTaskImpl_4

		//公开认定解析
		String targetUri1 = "http://query.sse.com.cn/commonSoaQuery.do";
		Map<String, String> headParams = Maps.newHashMap();
		headParams.put("Referer", "http://www.sse.com.cn/disclosure/credibility/supervision/measures/");
		headParams.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");

		Map<String, String> params = Maps.newHashMap();
		params.put("jsonCallBack", "jsonpCallback89106");
		params.put("extWTFL", "公开认定");
		params.put("siteId", "28");
		params.put("sqlId", "BS_GGLL");
		params.put("channelId", "10007,10008,10009,10010");
		params.put("order", "createTime|desc,stockcode|asc");
		params.put("isPagination", "fasle");
		params.put("pageHelp.pageSize", "100");
		params.put("pageHelp.pageNo", "1");
		params.put("pageHelp.beginPage", "1");
		params.put("pageHelp.cacheSize", "1");


		String fullText = getData(targetUri1, params, headParams);
		fullText = fullText.replace("jsonpCallback89106(", "");
		fullText = fullText.substring(0, fullText.length()) + "}";
		JSONObject jsonObject = JSONUtil.parseObj(fullText);
		JSONArray resultStr = jsonObject.getJSONArray("result");
		log.info(resultStr.toString());
		JSONArray results = JSONUtil.parseArray(resultStr.toString());
		for(int i = 0; i< results.size(); i++){
			JSONObject jsObj = results.getJSONObject(i);
			String docTitle = jsObj.getStr("docTitle");
		}

		Map<String, String> requestParams = new LinkedHashMap<>();
		log.info(postData(targetUri1, requestParams));

		return null;
	}

}
