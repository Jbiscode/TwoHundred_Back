package org.duckdns.bidbuy.app.offer.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OfferResponse {

    private Long id;
    private Long articleId;
    private String offererUsername;
    private String offererAddr1;
    private String offererAddr2;
    private LocalDateTime createdDate;
    private Integer offerPrice;
    private boolean isSelected;

}
