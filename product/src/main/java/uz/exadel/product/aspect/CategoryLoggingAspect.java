package uz.exadel.product.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class CategoryLoggingAspect {

    @AfterReturning(pointcut = "execution(* uz.exadel.product.service.impl.CategoryServiceImpl.getAll())", returning = "list")
    public void afterGettingCategoryList(List<Category> list) {
         list.forEach(category -> log.info("category_id:" + category.getId() + " |category_name: " + category.getName() + " |category_description:" + category.getDescription()));
    }

    //this works for both category and product
    @AfterReturning(pointcut = "execution(* uz.exadel.product.service.impl.*.getById(*))", returning = "result")
    public void beforeGettingObjectById(JoinPoint joinPoint, Object result){
        Object[] args = joinPoint.getArgs();
        Object param = args[0];

        if (result instanceof Category){
            log.info("Requesting for the category with an id of " + param.toString());
            Category category = (Category) result;
            log.info("categoryId:" + category.getId() + " |categoryName:" + category.getName() + " |categoryDescription:" + category.getDescription());
        }else if (result instanceof Product){
            log.info("Requesting for the product with an id of " + param.toString());
            Product product = (Product) result;
            log.info("productId:" + product.getId() + " |productName:" + product.getName() + " |productDescription:"+ product.getDescription());
        }
    }


    @Around(value = "execution(* uz.exadel.product.service.impl.ProductServiceImpl.getAll())")
    public List<Product> calculateExecutionTimeOfGetProductList(ProceedingJoinPoint proceedingJoinPoint){
        long startingTime = System.currentTimeMillis();
        try {
            List<Product> result = (List<Product>)proceedingJoinPoint.proceed();
            long endingTime = System.currentTimeMillis();
            log.info("Execution time of getting product list: " + (endingTime-startingTime) + "milliseconds");
            return result;
        } catch (Throwable e) {
            log.info("An exception occurred in productGetProductList() method");
        }
        return new ArrayList<>();
    }

}
