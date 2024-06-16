package org.duckdns.bidbuy.app.chat.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.chat.domain.ChatRoom;
import org.duckdns.bidbuy.app.chat.dto.ChatEnterRequest;
import org.duckdns.bidbuy.app.chat.dto.ChatRoomResponse;
import org.duckdns.bidbuy.app.chat.repository.ChatRoomRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

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
}
