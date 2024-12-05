package com.praveen.Demo_Products.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String productId;
	
	private String productName;
	
	private double productPrice;
	
	private int productQuantity;
	
	private byte[] productImageData;
	
	private String productImageName;
	
	private String productImageType;
	
	private String emailAddress;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	
	
	

}
