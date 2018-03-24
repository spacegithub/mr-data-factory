package com.mr.modules.api.site.instance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-3-16
 * 深交所
 * 信息披露->上市公司信息->上市公司诚信档案->处罚与处分记录
 */

@Slf4j
@Component("site7")
@Scope("prototype")
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
		String xlsName = downLoadFile(exlUri,"处罚与处分记录.xlsx");
		log.info("fileName=" + xlsName);
		List<LinkedHashMap<String, String>> lists = extract(xlsName);
		exportToXls("site7.xlsx", lists);

		return null;
	}

	/**
	 * 提取所需信息
	 * 公司代码、公司简称、处分日期、处分类别、当事人、标题、全文
	 */
	private List<LinkedHashMap<String, String>> extract(String xlsName) throws Exception {
		List<LinkedHashMap<String, String>> lists = Lists.newLinkedList();
		String[] columeNames = {
				"companycode",    //公司代码
				"companyAlias",    //涉及公司简称
				"punishDate",    //处分日期
				"punishType",    //处分类别
				"person",        //当事人
				"title",        //标题
				"contentUri"};   //全文URI
		List<Map<String, Object>> maps = importFromXls(xlsName, columeNames);
		for (Map map : maps) {
			log.info(map.toString());
			LinkedHashMap<String, String> linkedHashMap = Maps.newLinkedHashMap();
			linkedHashMap.put("companycode", Objects.isNull(map.get("companycode")) ? "" : (String) map.get("companycode"));
			linkedHashMap.put("companyAlias", Objects.isNull(map.get("companyAlias")) ? "" : (String) map.get("companyAlias"));
			linkedHashMap.put("punishDate", Objects.isNull(map.get("punishDate")) ? "" : (String) map.get("punishDate"));
			linkedHashMap.put("punishType", Objects.isNull(map.get("punishType")) ? "" : (String) map.get("punishType"));
			linkedHashMap.put("person",  Objects.isNull(map.get("person")) ? "" : (String) map.get("person"));
			linkedHashMap.put("title", Objects.isNull(map.get("title")) ? "" : (String) map.get("title"));
			String contentUri = Objects.isNull(map.get("contentUri")) ? "" : (String) map.get("contentUri");
			String contentFile = downLoadFile("http://www.szse.cn/UpFiles/cfwj/" + contentUri.trim());
			if(contentFile.toLowerCase().endsWith("doc")){
				linkedHashMap.put("content",ocrUtil.getTextFromDoc(contentFile));
			}else if(contentFile.toLowerCase().endsWith("pdf")){
				linkedHashMap.put("content",ocrUtil.getTextFromPdf(contentFile));
			}else{
				linkedHashMap.put("content",contentUri);
			}

			lists.add(linkedHashMap);
		}
		return lists;
	}
}
