package com.jt.manage.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;
@Controller
@RequestMapping("/item")
public class ItemController {
	
	//引入日志文件
	//org.apache.log4j.Logger
	private static final Logger logger = Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * easyUI的全部请求都是AJAX请求
	 * 值的传递都是json形式进行的
	 * 
	 */
	@RequestMapping("/query")
	@ResponseBody //返回值直接转化为JSON串 [{},{},{}]
	public EasyUIResult findItemList(int page,int rows){
		
		return itemService.findItemList(page,rows);
	}
	
	@RequestMapping("/queryItemName")
	public void queryItemName(Long itemCatId,HttpServletResponse response){
		
		response.setContentType("text/html;charset=UTF-8");
		
		String name = itemService.findItemCatName(itemCatId);
		
		try {
			response.getWriter().write(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param item
	 * @param desc 商品描述信息的参数
	 * @return
	 */
	//新增商品信息
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		//SysResult sysResult = new SysResult();
		try {
			
			itemService.saveItem(item,desc);
		  //sysResult.setStatus(200);//正确返回
		  //sysResult.setMsg();
		  //sysResult.setData(data);//返回值结果
			return SysResult.build(200, "新增商品成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~新增商品失败"+e.getMessage());
			return SysResult.build(201, "新增商品失败!请联系管理员");
		}
	}
	
	
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			return SysResult.build(200, "更新商品成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~新增商品失败"+e.getMessage());
			return SysResult.build(201, "更新商品失败!请联系管理员");
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids){
		try {
			itemService.deleteItem(ids);
			return SysResult.build(200, "商品删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~商品删除失败"+e.getMessage());
			return SysResult.build(201, "商品删除失败!请联系管理员");
		}
	}
	
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult instockItem(Long[] ids){
		try {
			itemService.instockItem(ids);
			return SysResult.build(200, "商品下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~商品下架失败"+e.getMessage());
			return SysResult.build(201, "商品下架失败!请联系管理员");
		}
	}
	
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelfItem(Long[] ids){
		try {
			itemService.reshelfItem(ids);
			return SysResult.build(200, "商品上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~商品上架失败"+e.getMessage());
			return SysResult.build(201, "商品上架失败!请联系管理员");
		}
	}
	
	@RequestMapping("/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDesc(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = itemService.findItemDesc(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("~~~~~~~~查询商品描述信息失败"+e.getMessage());
			return SysResult.build(201, "查询商品描述信息失败!请联系管理员");
		}
	}
/*	@RequestMapping("")
	public void findItemList(HttpServletResponse response){
		
		List<Item> findItemList = itemService.findItemList();
		
		try {
			response.getWriter().write("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
}
