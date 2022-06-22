package uz.exadel.user.mapper;

import org.mapstruct.Mapper;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto dto);

}
