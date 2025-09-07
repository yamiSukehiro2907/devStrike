package com.example.backend.services.PlatformService;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class PlatformDetailsServiceImpl {

    private final WebClient webClient;

    public PlatformDetailsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public boolean hasUserMadeCommitToday(String githubUsername) {
        OffsetDateTime todayStart = LocalDate.now().atStartOfDay().atOffset(OffsetDateTime.now().getOffset());

        try {
            JsonNode[] events = webClient.get()
                    .uri("/users/{username}/events/public", githubUsername)
                    .retrieve()
                    .bodyToMono(JsonNode[].class)
                    .block();

            if (events == null) return false;

            for (JsonNode event : events) {
                if ("PushEvent".equals(event.get("type").asText())) {
                    String creeatedAtString = event.get("created_at").asText();

                    OffsetDateTime createdAt = OffsetDateTime.parse(creeatedAtString);

                    if (createdAt.isAfter(todayStart)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error checking Github activity for " + githubUsername + " : " + e.getMessage());
            return false;
        }
    }

}
