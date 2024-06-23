package org.duckdns.bidbuy.app.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.article.repository.ProductImageRepository;
import org.duckdns.bidbuy.app.chat.domain.ChatRoom;
import org.duckdns.bidbuy.app.chat.dto.ChatEnterRequest;
import org.duckdns.bidbuy.app.chat.dto.ChatPostDetailResponse;
import org.duckdns.bidbuy.app.chat.dto.ChatRoomResponse;
import org.duckdns.bidbuy.app.chat.repository.ChatRoomRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;

    public ChatRoomResponse enter(ChatEnterRequest request) {
        Article article = articleRepository.findById(request.getArticleId()).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Long existChatRoomId = null;

        Boolean isExist = chatRoomRepository.existsByArticleIdAndUserId(article, user);

        if (isExist) {
            existChatRoomId = chatRoomRepository.findByArticleIdAndUserId(article,user).getId();
            return ChatRoomResponse.builder().ChatRoomId(existChatRoomId).build();
        }
        Long newChatRoom = chatRoomRepository.save(ChatRoom.builder()
                        .createdDate(LocalDateTime.now())
                        .modifiedDate(LocalDateTime.now())
                        .articleId(article)
                        .userId(user)
                        .build()).getId();

        return ChatRoomResponse.builder().ChatRoomId(newChatRoom).build();
    }

    public ChatPostDetailResponse getChatPostDetail(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다."));
        Article article = articleRepository.findById(chatRoom.getArticleId().getId()).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        List<ProductImage> productImage = productImageRepository.findByArticle(article).stream().filter(a->a.getThumbnailUrl()!=null).toList();

        return ChatPostDetailResponse.builder()
                .articleId(chatRoom.getArticleId().getId())
                .title(article.getTitle())
                .content(article.getContent())
                .ProductImageUrl(productImage.get(0).getThumbnailUrl())
                .tradeStatus(article.getTradeStatus())
                .writerId(article.getWriter().getId())
                .writerName(article.getWriter().getUsername())
                .writerProfileImageUrl(article.getWriter().getProfileImageUrl())
                .userId(chatRoom.getUserId().getId())
                .offerProfileImageUrl(chatRoom.getUserId().getProfileImageUrl())
                .build();
    }
}
