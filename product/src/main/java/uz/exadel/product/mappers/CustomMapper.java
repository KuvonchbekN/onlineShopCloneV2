package uz.exadel.product.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomMapper <T, R>{

    // T - PROD  R- DTO
    T dtoToObject(R r);

    R objectToDto(T t);

    T dtoToObject(R r, T t);
}
