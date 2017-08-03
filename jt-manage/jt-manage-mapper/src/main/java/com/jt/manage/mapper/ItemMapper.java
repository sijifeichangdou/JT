package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	
	 List<Item> findItemList();
	
	 int selectItemCount();

	 List<Item> findPageInfoList(@Param("startNum") int startNum, @Param("rows") int rows);

	 String findItemCatName(Long itemCatId);

}
