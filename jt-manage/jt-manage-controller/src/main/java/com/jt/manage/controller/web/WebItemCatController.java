package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.ItemCatResult;
import com.jt.manage.service.ItemCatService;

@Controller
public class WebItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/web/item/cat/list")
	@ResponseBody
	public MappingJacksonValue getItemCat(String callback){
		MappingJacksonValue mjv = new MappingJacksonValue(itemCatService);
		mjv.setJsonpFunction(callback);
		return mjv;
	}
}
