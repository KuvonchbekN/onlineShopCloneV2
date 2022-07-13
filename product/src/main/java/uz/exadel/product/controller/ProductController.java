package uz.exadel.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.product.clientDto.OrderItemDto;
import uz.exadel.product.entity.Product;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.payload.ResponseItem;
import uz.exadel.product.service.ProductService;

import java.util.List;


@RequestMapping("/api/product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @GetMapping
    public ResponseEntity<?> getAllProductList(){
        return ResponseEntity.ok(new ResponseItem("All categories list", productService.getAll()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId){
        Product productById = productService.getById(productId);
        return ResponseEntity.ok(new ResponseItem("Single product", productById));
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto){
        String savedProductId = productService.create(productDto);
        return ResponseEntity.ok(new ResponseItem("Added successfully", savedProductId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto){
        String updateProductId = productService.update(productDto, productId);
        return ResponseEntity.ok(new ResponseItem("Updated Product id", updateProductId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
        return ResponseEntity.ok(new ResponseItem("Deleted Successfully"));
    }

    //for feign client
    @PostMapping("/checkSufficiency")
    public void isThereEnoughProductInWarehouse(@RequestBody List<OrderItemDto> orderItemDtoList){
        productService.checkIfThereIsEnoughProductInWarehouse(orderItemDtoList);
    }


}
