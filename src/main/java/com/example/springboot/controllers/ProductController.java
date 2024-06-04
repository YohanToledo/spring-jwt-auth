package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@PostMapping("/products")
	public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductRecordDto productRecordDto){
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductModel>> findAll(){
		List<ProductModel> productList = productRepository.findAll();
		
		if(!productList.isEmpty()) {
			for(ProductModel p : productList) {
				UUID id = p.getIdProduct();
				p.add(linkTo(methodOn(ProductController.class).findOne(id)).withSelfRel());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(productList);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> findOne(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> productOptional = productRepository.findById(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(productOptional.get());
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto){
		Optional<ProductModel> productOptional = productRepository.findById(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found!");
		}
		
		var productModel = productOptional.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> productOptional = productRepository.findById(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found!");
		}
		
		productRepository.delete(productOptional.get());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
