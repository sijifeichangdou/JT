package com.jt.manage.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.BaseService;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
@Service
public class ItemService extends BaseService<Item>{
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	public EasyUIResult findItemList(int page, int rows){
		//使用分页插件 page：查询页数 rows：查询的数据量
		//分页开关
		PageHelper.startPage(page, rows);
		
		List<Item> itemList = itemMapper.findItemList();
		//
		PageInfo<Item> info = new PageInfo<Item>(itemList);
		
		return new EasyUIResult(info.getTotal(),info.getList());
		
		/*
		 * 手动的分页配置
	 	int title = itemMapper.selectItemCount();
		
		int startNum = (page-1)*rows;
		
		List<Item> itemList = itemMapper.findPageInfoList(startNum,rows);
		
		//将数据转化为JSON串
		ObjectMapper mapper = new ObjectMapper();
		
		String json = mapper.writeValueAsString(itemList);
		
		EasyUIResult result = new EasyUIResult();
		
		result.setTotal(title);
		
		result.setRows(itemList);
		
		return result;*/
	}
	
	public String findItemCatName(Long itemCatId) {
		return itemMapper.findItemCatName(itemCatId);
	}
	
	public void saveItem(Item item, String desc) {
		//全部插入，即使插入对象中的属性值为null也会进行插入操作
		//itemMapper.insert(item);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		//动态插入
		itemMapper.insertSelective(item);
		
		/**
		 * 问题：商品描述信息中需要进行入库操作 但是入库的id主键应该是商品Id，
		 * 但是现在商品处于要插入状态，mysql还没有为其分配Id值
		 * 
		 * 解决方案：
		 * MyBatis+Mysql+通用Mapper
		 * 利用通用mapper可以解决，调用insertSelective()
		 * 方法时，底层会自动调用查询方法select LAST_INSERT_ID()
		 * 将ID注入到对象中并返回
		 */
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDescMapper.insertSelective(itemDesc);
	}
	
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
	}
	
	public void deleteItem(Long[] ids) {
		
		for (Long id : ids) {
			
			Item item = itemMapper.selectByPrimaryKey(id);
			
			String[] imageUrlPaths = item.getImage().split(",");
			
			for (String path : imageUrlPaths) {
				
				String imageRealPath = path.replace("http://image.jt.com/", "F:/jt-upload/");
				
				File file = new File(imageRealPath);
				
				if(file.exists()){
					file.delete();
				}
			}
		}
		
		itemMapper.deleteByIDS(ids);
		
		itemDescMapper.deleteByIDS(ids);
		
		//删除所有空目录
		delete(new File("F:/jt-upload/images"));
		
	}

	private void delete(File dirPath) {
		if(dirPath.isFile()){
			return;
		}
		if(dirPath.isDirectory()){
			if(dirPath.list().length == 0){
				dirPath.delete();
			}else{
				for (File dir : dirPath.listFiles()) {
					if(dir.isDirectory()){
						delete(dir);
					}
				}
			}
		}
	}

	public void instockItem(Long[] ids) {
		for (Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(0);
			itemMapper.updateByPrimaryKeySelective(item);
		}
	}

	public void reshelfItem(Long[] ids) {
		for (Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(1);
			itemMapper.updateByPrimaryKeySelective(item);
		}
	}

	public ItemDesc findItemDesc(Long itemId) {
		return itemDescMapper.selectByPrimaryKey(itemId);
	}
}
