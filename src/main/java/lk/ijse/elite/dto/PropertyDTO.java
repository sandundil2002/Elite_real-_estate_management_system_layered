package lk.ijse.elite.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class PropertyDTO {
    private String propertyId;
    private String agentId;
    private String price;
    private String address;
    private String type;
    private String status;
    private String perches;
}
