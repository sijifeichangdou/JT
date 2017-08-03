package com.jt.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.BaseService;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
//import com.jt.common.service.RedisSentinelService;
//import com.jt.common.service.RedisService;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

import redis.clients.jedis.JedisCluster;
@Service
public class ItemCatService extends BaseService<ItemCat>{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	/*@Autowired
	private RedisService redisService;*/                       //分片
	
	/*@Autowired															
	private RedisSentinelService redisService;*/			   //哨兵
	
	@Autowired
	private JedisCluster  redisService;						   //集群
	
	@SuppressWarnings("unchecked")
	public List<ItemCat> findItemCatList(Long parentId){
		
		ItemCat itemCat = new ItemCat();
		
		itemCat.setParentId(parentId);
		
		//判断缓存中是否有数据
		String ITEM_CAT_ID = "ITEM_CAT_"+parentId;//保证整个项目中唯一
		String jsonData = redisService.get(ITEM_CAT_ID);
		if(StringUtils.isNotEmpty(jsonData)){//缓存中有数据
			JsonNode jsonNode;
			try {
				//json对象转化为jsonNode对象 jackson提供的一种格式
				jsonNode = MAPPER.readTree(jsonData);
				
				Object obj = null;
				if (jsonNode.isArray() && jsonNode.size() > 0) {
				    obj = MAPPER.readValue(jsonNode.traverse(),
				            MAPPER.getTypeFactory().constructCollectionType(List.class, ItemCat.class));
				}
				return (List<ItemCat>) obj;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else{
			
			//执行业务逻辑
			//根据实体类不为null的字段进行查询
			List<ItemCat> ItemCatList = itemCatMapper.select(itemCat);
			//保存数据到缓存中，将java对象装换为json字符串
			try {
				String json = MAPPER.writeValueAsString(ItemCatList);
				redisService.set(ITEM_CAT_ID, json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ItemCatList;
		}
			return null;
	}
	
	public ItemCatResult getItemCat(){
		/**
		 * steps:
		 * 1.获取所有数据
		 * 2.一次循环获取当前节点的所有子节点
		 * 3.三级遍历组织数据
		 */
		// 1
		List<ItemCat> itemCatList = itemCatMapper.select(null);
		// 2
		Map<Long,List<ItemCat>> map = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : itemCatList) {
			if(!map.containsKey(itemCat.getParentId())){
				//不存在 创建key 并创建List
				map.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}

				map.get(itemCat.getParentId()).add(itemCat);
		}
		
		// 3
		//一级菜单
		
		ItemCatResult itemCatResult = new ItemCatResult();
		
		List<ItemCatData> list1 = new ArrayList<ItemCatData>();
		//遍历第一级菜单 parent_id 为 0
		for (ItemCat itemCat1 : map.get(0L)) {
			ItemCatData itemCatData1 = new ItemCatData();
			String url = "/product/"+itemCat1.getId()+".html";
			itemCatData1.setUrl(url);
			itemCatData1.setName("<a href=\""+url+"\">"+itemCat1.getName()+"</a>");
			
			//遍历第二级菜单
			List<ItemCatData> list2 = new ArrayList<ItemCatData>();
			for(ItemCat itemCat2 : map.get(itemCat1.getId())){ 
				ItemCatData itemCatData2 = new ItemCatData();
				url = "/product/"+itemCat2.getId()+".html";
				itemCatData2.setUrl(url);
				itemCatData2.setName(itemCat2.getName());
				
				//遍历第三级菜单
				List<String> list3 = new ArrayList<String>();
				for(ItemCat itemCat3 : map.get(itemCat2.getId())){
					url = "/products/"+itemCat3.getId()+".html";
					list3.add(url+"|"+itemCat3.getName());
				}
				
				
				itemCatData2.setItems(list3);
				list2.add(itemCatData2);
			}
			
			
			itemCatData1.setItems(list2);//二级菜单
			list1.add(itemCatData1);
			itemCatResult.setItemCats(list1);
		}
		return itemCatResult;
	}
}
