package com.mr.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jdesktop.swingx.util.OS;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OCRUtil {

	private final static String DOWNLOAD_DIR = "/home/fengjiang/Documents";

	/**
	 * 解析的dirName下的所有图片
	 * dirName  目录名 = 下载的文件所在目录 + 文件名
	 * 如：下载文件名为 test.pdf,下载的目录为 /home/fengjiang/Documents,
	 * 则新建目录test, dirName = /home/fengjiang/Documents/test, 里面为 test.pdf 转成的若干图片，如0.png, 1.png
	 *
	 * @param dirName
	 */
	public static String recognizeTexts(String dirName) throws Exception {
		File testDataDir = new File(dirName);
		//listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
		File[] files = testDataDir.listFiles();
		int imgCount = files.length;
		log.info("tessdata目录下共有 " + imgCount + " 个文件/文件夹");
		//解析image
		StringBuilder sbs = new StringBuilder();
		testDataDir.listFiles();
		for(int i=imgCount - 1; i >= 0; i--){
			sbs.append(recognizeText(files[i]));
		}
		return sbs.toString();
	}

	private final static String LANG_OPTION = "-l";

	//line.separator 行分隔符
	private final static String EOL = System.getProperty("line.separator");

	//tesseract文件放在项目文件里了
	private final static String tesseractPath = new File(".").getAbsolutePath();

	/**
	 * 此方法功能：识别图片中的文字并返回到指定txt文件中
	 *
	 * @param image 输入一张图片（这里放在了项目目录）
	 */
	public static String recognizeText(File image) throws Exception {

		File outputfile = new File(image.getParentFile(), "output");//输出文件的保存目录

		StringBuffer strB = new StringBuffer();
		List<String> cmd = new ArrayList<String>();//数组各个位置存放东西，如[tesseract.exe的路径，识别的图像，输出文件名，命令选项-l，语言选择]

		if (OS.isLinux()) {  //OS需要导入SwingX的jar包
			cmd.add("tesseract");
		} else {
			cmd.add(tesseractPath + "\\tesseract");
		}
		cmd.add("");
		cmd.add(outputfile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("chi_sim");
//        cmd.add("eng");
		cmd.set(1, image.getName());//把cmd数组中的第二个位置放置图片
		log.info("cmd数组:" + cmd);

		ProcessBuilder pb = new ProcessBuilder();//创建有个进程生成器实例  深入研究 http://blog.51cto.com/lavasoft/15662
		pb.directory(image.getParentFile());//设置此进程生成器的工作目录
		log.info("\n识别的图片名为：" + image.getName());
		pb.command(cmd);// 设置此进程生成器的操作系统程序和参数
		pb.redirectErrorStream(true);
//      前面都是在为组装命令做准备:tesseract.exe 1.jpg 1 -l chi_sim,然后执行命令：Runtime.getRuntime().exec("tesseract.exe 1.jpg 1 -l chi_sim");
		Process process = pb.start();

		log.info(cmd.toString());//E:\SoftWare_List_D\Tesseract-OCR\下执行命令[tesseract 2.jpg output -l chi_sim]

		int w = process.waitFor();
		log.info("w的值：" + w);
		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputfile.getAbsolutePath() + ".txt"), "utf-8"));
			String str;
			while ((str = in.readLine()) != null) {
				log.info(str);
				strB.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
				case 1:
					msg = "Errors accessing files. There may be spaces in your image's filename.";
					break;
				case 29:
					msg = "Cannot recognize the image or its selected region.";
					break;
				case 31:
					msg = "Unsupported image format.";
					break;
				default:
					msg = "Errors occurred.";
			}
			throw new RuntimeException(msg);
		}
		new File(outputfile.getAbsolutePath() + ".txt").delete();
//        return strB.toString().replaceAll("\\s*","");
		return strB.toString();
	}

	/**
	 * @param fileName 下载的文件名,不是全路径名
	 *                 将文件移到文件夹内，并改名.
	 * @return
	 */
	public static String image2Dir(String fileName) {
		String entirePathName = DOWNLOAD_DIR + File.separator + fileName;
		String dirs[] = fileName.split("\\.");
		File dirFile = new File(DOWNLOAD_DIR + File.separator + dirs[0]);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		if (dirs[1].equalsIgnoreCase("pdf")) {
			renameTo(entirePathName, dirFile + File.separator + fileName);
			pdf2image(dirFile + File.separator + fileName);
		} else {
			renameTo(entirePathName, dirFile + File.separator + "0." + dirs[1]);
		}
		return dirFile.getAbsolutePath();
	}

	/**
	 * 将pdf转化为png格式的image
	 *
	 * @param pdfName
	 */
	public static void pdf2image(String pdfName) {
		File file = new File(pdfName);
		try {
			PDDocument doc = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(doc);
			int pageCount = doc.getNumberOfPages();
			for (int i = 0; i < pageCount; i++) {
				// 方式1,第二个参数是设置缩放比(即像素)
				BufferedImage image = renderer.renderImageWithDPI(i, 296);
				// 方式2,第二个参数是设置缩放比(即像素)
				// BufferedImage image = renderer.renderImage(i, 2.5f);
				ImageIO.write(image, "PNG", new File(file.getParentFile(), i + ".png"));
			}
			file.delete();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件
	 *
	 * @param src
	 * @param to
	 */
	public static void renameTo(String src, String to) {
		File file = new File(src);   //指定文件名及路径
		file.renameTo(new File(to));   //改名
	}

	public static void main(String[] args) {
		try {
//			renameTo("/home/fengjiang/Documents/nginx.conf", "/home/fengjiang/Documents/projdoc/nginx.conf");
//			pdf2image(DOWNLOAD_DIR + File.separator + "P020171222593212170499.pdf");
//			image2Dir("P020171222593212170499.pdf");
			log.info(recognizeTexts(image2Dir("P020171222593212170499.pdf")));
//			image2Dir("434324.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



