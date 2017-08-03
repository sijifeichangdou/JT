package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jt.common.po.BasePojo;


//商品信息

@SuppressWarnings("serial")
@Table(name="tb_item")   //表示对象与数据表一一对应
public class Item extends BasePojo{
	
	//主键信息
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;  //商品id
	private String title;  //商品标题
	
	//@Column(name="sell_point")
	private String sellPoint; //卖点
	private Long price;   //商品价格  后期由js计算 价格/100
	private Integer num;  //商品数量
	private String barcode; //扫描码
	private String image;   //商品图片
	private Long cid; 		//分类号
	private Integer status;  //状态 默认值为1，可选值：1正常，2下架，3删除
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
