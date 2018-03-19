package com.mr.modules.api.site.instance;

import com.mr.common.OCRUtil;
import com.mr.common.util.SpringUtils;
import com.mr.modules.api.site.SiteTaskExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 18-3-16
 * 全国中小企业股转系统
 * 全国中小企业股转系统-监管公告
 */

@Slf4j
@Component("site1")
@Scope("prototype")
public class SiteTaskImpl_1 extends SiteTaskExtend {

	protected OCRUtil ocrUtil = SpringUtils.getBean(OCRUtil.class);

	/**
	 * @return ""或者null为成功， 其它为失败
	 * @throws Throwable
	 */
	@Override
	protected String execute() throws Throwable {
		log.info("*******************call site1 task**************");
		String targetUri = "http://www.neeq.com.cn/uploads/1/file/public/201802/20180226182405_lc6vjyqntd.pdf";
		String fileName = downLoadFile(targetUri);
		//fullTxt 正文详细文本信息
		String fullTxt = ocrUtil.getTextFromPdf(fileName);
		log.info(fullTxt);
		extract(fullTxt);


		return null;
	}

	/**
	 * 提取所需要的信息
	 * 当事人、公司、住所地、法定代表人、一码通代码（当事人为个人）、当事人补充情况、违规情况、相关法规、处罚结果、处罚结果补充情况
	 */
	private void extract(String fullTxt) {
		//当事人 从链接中提取
		String person = "河南牧宝车居股份有限公司";

		//公司 从链接中提取
		String company = "河南牧宝车居股份有限公司";

		//住所地
		String address = "";
		//法定代表人
		String holder = "";
		//当事人补充情况
		String holderAddition = "";
		//违规情况
		String violation = "";
		//相关法规
		String rule = "";
		//处罚结果
		String result = "";
		//处罚结果补充情况
		String resultAddition = "";


		int indx = fullTxt.indexOf("法定代表人：");
		//当事人为公司，格式规则
		if (indx > -1) {
			int sIndx = 0;
			address = fullTxt.substring(fullTxt.indexOf("住所地："), indx).replace("住所地：", "").trim();
			address = address.substring(0, address.length() - 1).replace("\n", "");

			holder = fullTxt.substring(indx, fullTxt.indexOf("经查明")).replace("法定代表人：", "").trim().replace("\n", "");

			sIndx = fullTxt.indexOf("当事人：");
			holderAddition = fullTxt.substring(sIndx, fullTxt.indexOf("，住所地")).replace("当事人：", "").trim().replace("\n", "");

			sIndx = fullTxt.indexOf("经查明，");
			violation = fullTxt.substring(sIndx, fullTxt.indexOf("你公司的上述行为违反了")).replace("经查明，", "").trim().replace("\n", "");

			sIndx = fullTxt.indexOf("鉴于上述违规事实和情节，");
			result = fullTxt.substring(sIndx, fullTxt.indexOf("你公司应自收到本决定书之日起")).replace("鉴于上述违规事实和情节，", "").trim().replace("\n", "");

			sIndx = fullTxt.indexOf("你公司应自收到本决定书之");
			String tmpStr = fullTxt.substring(fullTxt.indexOf("你公司应自收到本决定书之")).trim();
			resultAddition = tmpStr.substring(0, tmpStr.trim().indexOf("\n \n")).trim().replace("\n", "");

		} else {
			//当事人为公司，格式规则

		}

		//当事人为个人



	}

}
