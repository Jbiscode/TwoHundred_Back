package org.duckdns.bidbuy.app.chat.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.chat.domain.ChatRoom;
import org.duckdns.bidbuy.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Boolean existsByArticleIdAndUserId(Article articleId, User userId);
    ChatRoom findByArticleIdAndUserId(Article articleId, User userId);
}
