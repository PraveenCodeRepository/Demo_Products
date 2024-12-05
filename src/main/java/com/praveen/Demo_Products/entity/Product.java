package com.praveen.Demo_Products.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.praveen.Demo_Products.util.CustomIdGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="prod_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {
	
	@Id
	private String productId;
	
	private String productName;
	
	private double productPrice;
	
	private int productQuantity;
	
	@Lob
	private byte[] productImageData;
	
	private String productImageName;
	
	private String productImageType;
	
	private String emailAddress;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.productId = CustomIdGenerator.generateId();
	}
	

}
