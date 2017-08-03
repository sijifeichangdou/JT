package com.jt.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
@Controller
public class FileUploadController {
	
	private static final Logger logger = Logger.getLogger(ItemController.class);
	/**
	 * 文件上传的步骤
		 1.采用文件正确的接收方式接收(修改三处配置文件、接口类型等)
		 2.判断是否为一个图片，0表示为无异常 (jpg|gif|png)
		 3.判断是不是一个“正经”的图片，判断是否有width和height
		 4.编辑磁盘目录
		 5.编辑相对路径 url:image.jt.com/XXX
		 6.将文件保存
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult picUploadResult(MultipartFile uploadFile){
		PicUploadResult picUpload = new PicUploadResult();
		String fileName = uploadFile.getOriginalFilename();
		String endName = fileName.substring(fileName.lastIndexOf("."));
		if(!endName.matches("^.*(jpg|gif|png)$")){
			logger.error("~~~~~~~~~~~~~文件后缀名不符合图片格式");
			picUpload.setError(1);
			return picUpload;
		}
		
		try {
			BufferedImage bufferImage = ImageIO.read(uploadFile.getInputStream());
			//获取宽度和高度 如果获取有问题则会报异常
			int height = bufferImage.getHeight();
			int width = bufferImage.getWidth();
			picUpload.setHeight(height+"");
			picUpload.setWidth(width+"");
			String localPath = "F:/jt-upload/images/";
			String datePath = new SimpleDateFormat("yyyy/MM/dd/HH/mm").format(new Date());
			String urlPath = "http://image.jt.com/images/";
			//F:/jt-upload/images/yyyy/MM/dd/HH/mm
			localPath += datePath+"/"+fileName;
			urlPath += datePath+"/"+fileName;
			
			File file = new File(localPath);
			
			if(!file.exists()){
				file.mkdirs();//创建多个文件夹
			}
			uploadFile.transferTo(file);
			
			picUpload.setUrl(urlPath);
			
			logger.info("文件写入成功"+localPath);
			
			return picUpload;
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("~~~~~~~~~~~~~该文件是一个非法文件"+e.getMessage());
			picUpload.setError(1);
			return picUpload;
		}
	}
}
