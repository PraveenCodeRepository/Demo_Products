package com.praveen.Demo_Products.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.praveen.Demo_Products.dto.ProductDto;
import com.praveen.Demo_Products.entity.Product;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.from}")
    private String emailFrom;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Method to send email
    public void sendEmail(String recipientEmail,  Product product , byte[] attachment) 
            throws MessagingException, IOException {
        
        try {
            // Load the HTML template from file
            String emailTemplate = loadEmailTemplate();

            // Replace placeholders in the template with actual values
            String emailBody = emailTemplate
                    .replace( "{productName}", product.getProductName() )
                    .replace( "{productPrice}", String.valueOf( product.getProductPrice() ) )
                    .replace(" {productQuantity}", String.valueOf( product.getProductQuantity() ) );

            // Create and configure the email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            
            String subject = "Product Uploaded Successfully";

            helper.setFrom(emailFrom);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(emailBody, true);  // Set the email body as HTML

            // Attach the product image inline
            ByteArrayResource file = new ByteArrayResource(attachment);
            helper.addInline("image1", file, "image/jpeg");  // "image1" is the CID used in the HTML
            helper.addAttachment(product.getProductName(), file);  

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", recipientEmail);

        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            throw new MessagingException("Error sending email", e);
        }
    }

    // Load the email template from a file
    private String loadEmailTemplate() throws IOException {
        String templatePath = "src/main/resources/templates/email-template.html";  // Path to your template file
        String template = Files.lines(Paths.get(templatePath))
        		               .collect(Collectors.joining("\n"));
        System.out.println("Template "+template);
        return template;
    }
}
