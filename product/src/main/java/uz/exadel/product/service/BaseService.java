package uz.exadel.product.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<R, T> {
    List<T> getAll();

    T getById(String id);

    String create(R r);

    String update(R r, String id);

    void delete(String id);

    void checkById(String id);
}
