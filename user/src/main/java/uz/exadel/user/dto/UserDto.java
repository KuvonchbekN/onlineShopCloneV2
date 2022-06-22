package uz.exadel.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String password;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;

    @JsonProperty(value = "birth_date")
    @Past(message = "Date should be before current date!")
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDate birthDate;

    private LocalDateTime created_at = LocalDateTime.now();

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
}
