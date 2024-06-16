package org.duckdns.bidbuy.app.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.chat.dto.ChatEnterRequest;
import org.duckdns.bidbuy.app.chat.dto.ChatRoomResponse;
import org.duckdns.bidbuy.app.chat.service.ChatRoomService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/chatroom", produces = "application/json")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/enter")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> enter(@RequestBody ChatEnterRequest request) {
        log.info("Entering chat room: {},{}", request.getArticleId(), request.getUserId());
        ChatRoomResponse chatRoomId = chatRoomService.enter(request);
        ApiResponse<ChatRoomResponse> response =new ApiResponse<>("200", "채팅방 PK", chatRoomId);

        return ResponseEntity.ok(response);
    }

//    @PostMapping("/create")
//    public ResponseEntity<ChatRoomResponse> create(@RequestBody ChatCreateRequest request) {
//        log.info("Creating chat room: {}", request);
//        ChatRoomResponse response = chatRoomService.create(request);
//        return ResponseEntity.ok(response);
//    }

}
