package com.examen.catalogapp.service;

import com.examen.catalogapp.dto.ProductRequestDTO;
import com.examen.catalogapp.dto.ProductResponseDTO;
import com.examen.catalogapp.model.Product;
import com.examen.catalogapp.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.examen.catalogapp.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        return toResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = new Product();

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());

        Product savedProduct = productRepository.save(product);

        return toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());

        Product updatedProduct = productRepository.save(product);

        return toResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        productRepository.delete(product);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}