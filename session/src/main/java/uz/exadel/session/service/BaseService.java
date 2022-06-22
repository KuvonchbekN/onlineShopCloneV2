package uz.exadel.session.service;


import java.util.List;


public interface BaseService<T, R> {
    String create(T t);
    List<R> getList();
    R getById(String id);
    void update(T t, String id);
    void delete(String id);
}
