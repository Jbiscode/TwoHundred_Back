package org.duckdns.bidbuy.app.offer.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OfferAcceptResponse {
    private Long offerId;
    private Integer price;
    private boolean isSelected;
}
