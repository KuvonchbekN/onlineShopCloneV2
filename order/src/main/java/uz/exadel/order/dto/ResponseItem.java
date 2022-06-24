package uz.exadel.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseItem {
    private String message;
    private Object object;

    public ResponseItem(String message) {
        this.message = message;
    }
}
