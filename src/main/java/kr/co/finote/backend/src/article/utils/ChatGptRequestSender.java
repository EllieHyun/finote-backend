package kr.co.finote.backend.src.article.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.co.finote.backend.src.article.dto.request.ChatGptRequest;
import kr.co.finote.backend.src.article.dto.response.ChatgptResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class ChatGptRequestSender {

    @Value("${chatgpt.api-key}")
    private String API_KEY;

    @Value("${chatgpt.endpoint}")
    private String END_POINT;

    @Value("${chatgpt.max-token}")
    private Integer MAX_TOKENS;

    @Value("${chatgpt.temperature}")
    private Float TEMPERATURE;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ChatgptResponse sendMessage(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        List<ChatGptRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatGptRequest.Message("user", prompt));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", TEMPERATURE);
        requestBody.put("max_tokens", MAX_TOKENS);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response =
                restTemplate.postForEntity(END_POINT, requestEntity, String.class);

        try {
            return objectMapper.readValue(response.getBody(), ChatgptResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
