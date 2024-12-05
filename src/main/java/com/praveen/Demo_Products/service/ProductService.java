package com.praveen.Demo_Products.service;

import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.praveen.Demo_Products.dto.ProductDto;
import com.praveen.Demo_Products.entity.Product;
import com.praveen.Demo_Products.repository.ProductRepository;
import jakarta.mail.MessagingException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EmailService emailService;
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    public ProductService(ProductRepository productRepository, EmailService emailService) {
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    public ProductDto saveProductDetails(MultipartFile file, ProductDto productDto) throws IOException, MessagingException {
        try {
            String productImageName = file.getOriginalFilename();
            String productImageType = file.getContentType();
            byte[] productImageData = file.getBytes();

            // Set productDto properties
            productDto.setProductName(productDto.getProductName());
            productDto.setProductPrice(productDto.getProductPrice());
            productDto.setProductQuantity(productDto.getProductQuantity());
            productDto.setEmailAddress(productDto.getEmailAddress());

            // Save product to database
            Product product = Product.builder()
                    .productName(productDto.getProductName())
                    .productPrice(productDto.getProductPrice())
                    .productQuantity(productDto.getProductQuantity())
                    .emailAddress(productDto.getEmailAddress())
                    .productImageName(productImageName)
                    .productImageType(productImageType)
                    .productImageData(productImageData)
                    .build();

            Product productSaved = productRepository.save(product);

            // Prepare email
            String productName = productSaved.getProductName();
            String productPrice = String.valueOf(productSaved.getProductPrice());
            String productQuantity = String.valueOf(productSaved.getProductQuantity());

            String recipientEmail = productSaved.getEmailAddress();
            byte[] attachment = productSaved.getProductImageData();
            
            // Validate the email address
            if (recipientEmail == null || recipientEmail.isEmpty()) {
                throw new RuntimeException("Recipient email address is missing");
            }
            
            System.out.println("Recipient email: " + recipientEmail);


            // Send email
            emailService.sendEmail(recipientEmail, productSaved,  attachment);

            return ProductDto.builder()
                    .productId(productSaved.getProductId())
                    .productName(productSaved.getProductName())
                    .productPrice(productSaved.getProductPrice())
                    .productQuantity(productSaved.getProductQuantity())
                    .productImageName(productSaved.getProductImageName())
                    .productImageType(productSaved.getProductImageType())
                    .productImageData(productSaved.getProductImageData())
                    .createdAt(productSaved.getCreatedAt())
                    .updatedAt(productSaved.getUpdatedAt())
                    .emailAddress(productSaved.getEmailAddress())
                    .build();

        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error saving product details: {}", e.getMessage());
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }

    public ProductDto getProductDetailsById(String id) {
        Optional<Product> optionalProduct = productRepository.findProductById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return ProductDto.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .productPrice(product.getProductPrice())
                    .productQuantity(product.getProductQuantity())
                    .productImageName(product.getProductImageName())
                    .productImageType(product.getProductImageType())
                    .productImageData(product.getProductImageData())
                    .build();
        } else {
            throw new RuntimeException("Product details not found");
        }
    }
}
	
	
	
	


