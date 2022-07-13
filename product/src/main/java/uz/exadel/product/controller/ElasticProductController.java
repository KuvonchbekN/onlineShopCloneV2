package uz.exadel.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.exadel.product.entity.ProductElastic;
import uz.exadel.product.repo.ElasticProductRepository;

import java.util.List;

@RequestMapping("/api/product")
@RestController
@RequiredArgsConstructor
public class ElasticProductController {
    private final ElasticProductRepository elasticProductRepository;

    @GetMapping("/search/{search}")
    public ResponseEntity<?> searchForProduct( @PathVariable String search){
        List<ProductElastic> list = elasticProductRepository.getAllByName(search);
        return ResponseEntity.ok(list);
    }
}
