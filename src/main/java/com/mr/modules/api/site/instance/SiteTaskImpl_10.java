package com.mr.modules.api.site.instance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by feng on 18-3-16
 * 证监会
 */

@Slf4j
@Component("site10")
@Scope("prototype")
public class SiteTaskImpl_10 extends SiteTaskExtend {

	protected OCRUtil ocrUtil = SpringUtils.getBean(OCRUtil.class);

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site10 task**************");
		List<LinkedHashMap<String, String>> lists = Lists.newLinkedList();

		String targetUri = "http://www.csrc.gov.cn/pub/newsite/gszqjgb/rcjg/zjjgrcjg/index.html";
		lists.addAll(extractPage(targetUri));

		String targetUri_1 = "http://www.csrc.gov.cn/pub/newsite/gszqjgb/rcjg/zjjgrcjg/index_1.html";
		lists.addAll(extractPage(targetUri_1));
		Collections.reverse(lists);
		exportToXls("site10.xlsx", lists);

		return null;
	}


	/**
	 * 提取所需要的信息
	 * 标题、对象、处罚机构、处罚时间、处罚事由、违反条例、处罚结果、整改时限
	 */
	private List<LinkedHashMap<String, String>> extractPage(String targetUri) {
		List<LinkedHashMap<String, String>> lists = Lists.newLinkedList();

		String recordPageTxt = getData(targetUri);
		Document doc = Jsoup.parse(recordPageTxt);
		Element divElement = doc.getElementById("documentContainer");
		for (Element liElement : divElement.getElementsByTag("li")) {
			String title = "";    //标题
			String punishObj = "";    //对象
			String punishDate = "";    //处罚时间
			String href = "";

			Element aElement = liElement.getElementsByTag("a").get(0);
			title = aElement.attr("title");
			log.info(title);

			LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
			try {
				if (title.contains("关于对") && title.contains("采取")) {
					punishObj = title.substring(title.indexOf("关于对"), title.lastIndexOf("采取"));
				}
				punishDate = liElement.getElementsByTag("span").get(0).text();

				href = "http://www.csrc.gov.cn/pub/newsite/gszqjgb/rcjg/zjjgrcjg/"
						+ aElement.attr("href").substring(2);
				if(!href.endsWith(".pdf")) throw new Exception("href is not pdf");

				log.info(href);
				String fileName = downLoadFile(href);

				String fullTxt = ocrUtil.getTextFromImg(fileName);
				log.info(fullTxt);
				map = extract(fullTxt);
			} catch (Exception e) {
				log.error(e.getMessage());
				map.put("punishOrg", "");
				map.put("punishContent", "");
				map.put("punishClause", "");
				map.put("punishResult", "");
				map.put("improveLimit", "");
			}

			map.put("href", href);
			map.put("title", title);
			map.put("punishObj", punishObj);
			map.put("punishDate", punishDate);
			lists.add(map);
		}
		return lists;
	}

	/**
	 * 提取Content
	 */
	private LinkedHashMap<String, String> extract(String fullTxt) {

		LinkedHashMap<String, String> map = Maps.newLinkedHashMap();

		String punishOrg = "";    //处罚机构
		int poPostion = fullTxt.indexOf("证监局办公室");
		for (int i = poPostion; ; i--) {
			if (fullTxt.substring(i, i + 1).equals("\n")) {
				punishOrg = fullTxt.substring(i + 1, poPostion) + "证监局办公室";
				break;
			}
		}


		String punishContent = "";    //处罚事由
		String sTmps[] = {"你公司的上述问题违反了", "上诉情况", "上述问题", "上述行为", "违反了"};
		int pcPosition = -1;
		for (String sTmp : sTmps) {
			if (fullTxt.contains(sTmp)) {
				punishContent = fullTxt.substring(fullTxt.indexOf(":") - 1, fullTxt.indexOf(sTmp)).trim();
				break;
			}
		}

		String punishClause = "";    //违反条例
		String punishResult = "";    //处罚结果
		String improveLimit = "";    //整改时限

		if (fullTxt.contains("违反了") && fullTxt.contains("如果对")) {
			String punishSub = fullTxt.substring(fullTxt.indexOf("违反了"), fullTxt.indexOf("如果对")).trim();
			if (punishSub.contains("根据")) {
				punishClause = punishSub.substring(0, punishSub.indexOf("根据"));
				punishResult = punishSub.substring(punishSub.indexOf("根据"));
				if (extracterZH(punishSub).contains("年月日")) {
					improveLimit = punishSub.substring(punishSub.indexOf("年") - 4, punishSub.lastIndexOf("日"));
				} else if (extracterZH(punishSub).contains("年月曰")) {
					int i = punishSub.indexOf("曰");
					improveLimit = punishSub.substring(punishSub.indexOf("年") - 4, punishSub.lastIndexOf("曰"));
				}
			} else if (punishSub.contains("按照")) {
				punishClause = punishSub.substring(0, punishSub.indexOf("按照"));
				punishResult = punishSub.substring(punishSub.indexOf("按照"));
				if (extracterZH(punishSub).contains("年月日")) {
					improveLimit = punishSub.substring(punishSub.indexOf("年") - 4, punishSub.lastIndexOf("日"));
				} else if (extracterZH(punishSub).contains("年月曰")) {
					improveLimit = punishSub.substring(punishSub.indexOf("年") - 4, punishSub.lastIndexOf("曰"));
				}
			}
		}


		map.put("punishOrg", punishOrg);
		map.put("punishContent", punishContent);
		map.put("punishClause", punishClause);
		map.put("punishResult", punishResult);
		map.put("improveLimit", improveLimit);

		return map;
	}
}
