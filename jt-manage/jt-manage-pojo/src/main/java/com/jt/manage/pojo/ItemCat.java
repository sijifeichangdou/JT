package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;


@JsonIgnoreProperties(ignoreUnknown=true)

@SuppressWarnings("serial")
@Table(name="tb_item_cat")//表示对象与数据表一一对应
public class ItemCat extends BasePojo{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long parentId;
	private String name;
	private Integer status;//1.正常 2.删除
	private Integer sortOrder;//排序号
	private Boolean isParent;//是否为上级目录

	/**
	 * 为了满足easyUI的树形结构添加getXXX方法 text
	 * @return
	 */
	public String getText(){
		return this.name;
	}
	/**
	 * 为了满足easyUI的树形结构添加getXXX方法 state
	 * 如果是上级菜单应该是closed 如果不是上级菜单则打开
	 * @return
	 */
	public String getState(){
		return this.isParent ? "closed" : "open";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
}
