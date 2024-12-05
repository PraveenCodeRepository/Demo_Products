package com.praveen.Demo_Products.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.praveen.Demo_Products.dto.ProductDto;
import com.praveen.Demo_Products.service.ProductService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ProductDto> uploadProductDetails(@RequestPart("image") MultipartFile file,
            @RequestPart("json") ProductDto productDto, HttpServletRequest request) throws IOException, MessagingException {

        try {
            ProductDto productDtoResponse = productService.saveProductDetails(file, productDto);
            return new ResponseEntity<>(productDtoResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error in uploading product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadProduct(@PathVariable String id) {
        try {
            ProductDto productDto = productService.getProductDetailsById(id);
            String fileOriginalName = productDto.getProductImageName();
            String contentType = productDto.getProductImageType();
            byte[] imageData = productDto.getProductImageData();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", fileOriginalName);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error downloading product image: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
