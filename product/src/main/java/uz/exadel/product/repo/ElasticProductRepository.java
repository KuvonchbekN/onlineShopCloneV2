package uz.exadel.product.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import uz.exadel.product.entity.ProductElastic;

import java.util.List;

@Repository
public interface ElasticProductRepository extends ElasticsearchRepository<ProductElastic, String> {
    List<ProductElastic> getAllByName(String name);
}
