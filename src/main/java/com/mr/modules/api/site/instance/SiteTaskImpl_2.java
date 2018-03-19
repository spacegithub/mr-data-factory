package com.mr.modules.api.site.instance;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mr.modules.api.site.SiteTaskExtend;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by feng on 18-3-16
 * 地方证监局
 * 行政处罚决定
 */

@Slf4j
@Component("site2")
@Scope("prototype")
public class SiteTaskImpl_2 extends SiteTaskExtend {

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site2 task**************");
		String targetUri = "http://www.csrc.gov.cn/pub/beijing/bjxzcf/201803/t20180314_335261.htm";
		String fullTxt = getData(targetUri);
		log.info(fullTxt);
		extract(fullTxt);
		return null;
	}


	/**
	 * 提取所需要的信息
	 * 序号、处罚文号、处罚对象、处罚日期、发布机构、发布日期、名单分类、标题名称、详情
	 */
	private void extract(String fullTxt) {
		//序号 TODO 需确认
		String seqNo = "";
		seqNo = "";

		//处罚文号
		String punishNo = "";

		//处罚对象
		String punishObject = "";
		ArrayList<String> punishObjects = Lists.newArrayList();
		ArrayList<String> filterTags = Lists.newArrayList("<SPAN>", "</SPAN>", "&nbsp;", "　");
		boolean isOn = false;

		//处罚日期
		String punishDate = "";

		//发布机构
		String org = "";
		org = "中国证监会北京监管局"; //从链接中提取

		//发布日期
		String releaseDate = "";
		releaseDate = "2018-03-14";

		//名单分类 TODO 需确认
		String listType = "";

		//标题名称
		String title = "";
		title = "中国证券监督管理委员会北京监管局行政处罚决定书（文细棠）"; //从链接中提取

		//详情
		String detail = "";
		boolean detailIsOn = false;
		ArrayList<String> details = Lists.newArrayList();



		Document doc = Jsoup.parse(fullTxt);
		Elements Ps = doc.getElementsByTag("P");
		for (Element pElement : Ps) {

			//punishNo 处理
			if (Strings.isNullOrEmpty(punishNo)) {
				Elements strongEles = pElement.getElementsByTag("STRONG");
				if (!Collections.isEmpty(strongEles)) {
					punishNo = strongEles.get(0).text().trim();
					continue;
				}
			}

			//punishObject 处理
			if (Collections.isEmpty(punishObjects) || isOn) {
				String pString = filter(pElement.text().trim(),filterTags).trim();
				if (pString.startsWith("当事人：")) {
					isOn = true;	//打开当事人提取开关
					punishObjects.add(pString);
				}
				if (pString.startsWith("依据《中华人民共和国证券法》")) {
					isOn = false;
					punishObject = punishObjects.toString().replace("[","").replace("]", "");
					detailIsOn = true;	//打开详情提取开关
				}
			}

			//punishDate 处理
			{
				String pString = extracterZH(pElement.text().trim());
				if("年月日".equals(pString))
					punishDate = filter(pElement.text().trim(), filterTags);
			}


			//detail 处理
			if(detailIsOn){
				details.add(filter(pElement.text().trim(),filterTags));
				if(filter(pElement.text().trim(),filterTags).startsWith("中国证监会")){
					detailIsOn = false;
					detail = details.toString().replace("[","").replace("]", "");
				}


			}

		}

		log.info("parse demo...\n" + detail);

	}

}
