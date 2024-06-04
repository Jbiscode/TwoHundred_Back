package org.duckdns.bidbuy.app.user.controller;

import org.duckdns.bidbuy.app.review.domain.Review;

import java.util.List;

public record MyProfileResponse(
    String username,
    String profileImageUrl,
    Integer offerLevel,
    List<Review> reviewsReceived,
    List<Review> reviewsGiven

) {
}
