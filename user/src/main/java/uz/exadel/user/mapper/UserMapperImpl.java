package uz.exadel.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;


@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-03-11T19:21:44+0100",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
        )
@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper{

    @Override
    public User dtoToUser(UserDto userDto) {
        if (userDto == null){
            return null;
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .address(userDto.getAddress())
                .password(userDto.getPassword())
                .createdAt(LocalDateTime.now())
                .phoneNumber(userDto.getPhoneNumber())
                .birthDate(userDto.getBirthDate())
                .build();

        return user;
    }


}
