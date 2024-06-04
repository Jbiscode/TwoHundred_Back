package org.duckdns.bidbuy.app.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleRequest {

    @NotBlank
    private String title;

    private String content;
    private Integer price;
    private Integer quantity;

    private Category category;

    private TradeMethod tradeMethod;

    private TradeStatus tradeStatus;

    private String addr1;
    private String addr2;

//    private User writer;

    // 업로드 처리가 필요한 파일의 이름을 저장하는 리스트
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 이미 업로드 된 파일의 이름을 저장하는 리스트
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();


}
