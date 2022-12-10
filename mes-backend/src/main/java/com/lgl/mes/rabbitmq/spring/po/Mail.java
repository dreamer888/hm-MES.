package com.lgl.mes.rabbitmq.spring.po;

import lombok.Data;

import java.io.Serializable;

//public class Mail {
@Data
public class Mail implements Serializable{
	
	private static final long serialVersionUID = -8140693840257585779L;
	private String mailId;
	private String orderNo;
	private String productId;
	private String quality;
	private Double quantity;

	
	
	public Mail() {
	}
	public Mail( String mailId, String orderNo,String productId,String quality,Double quantity) {
		this.mailId = mailId;
		this.orderNo = orderNo;
		this.productId = productId;
		this.quality = quality;
		this.quantity = quantity;//Double.valueOf(0);
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Mail [mailId=" + mailId +
				", orderNo=" + orderNo +
				", productId=" + productId+
				", quality=" + quality+
				", quantity=" + quantity
				+ "]";
	}

}
