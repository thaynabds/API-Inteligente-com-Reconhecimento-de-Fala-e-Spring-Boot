package br.com.thaynarbd.budgeting.infrastructure.web;

import br.com.thaynarbd.budgeting.infrastructure.ai.AssistantResponse;
import br.com.thaynarbd.budgeting.infrastructure.ai.FinancialAssistantService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
@ConditionalOnProperty(prefix = "app.ai", name = "enabled", havingValue = "true")
public class AssistantController {
    private final FinancialAssistantService assistant;
    public AssistantController(FinancialAssistantService assistant) { this.assistant = assistant; }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest request) {
        return Map.of("answer", assistant.chat(request.text()));
    }

    @PostMapping("/transcription")
    public Map<String, String> transcription(@RequestParam("audio") MultipartFile audio) throws IOException {
        return Map.of("transcription", assistant.transcribe(audio));
    }

    @PostMapping("/voice")
    public AssistantResponse voice(@RequestParam("audio") MultipartFile audio) throws IOException {
        return assistant.processVoice(audio);
    }

    @PostMapping(value = "/speech", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> speech(@RequestBody ChatRequest request) {
        byte[] audio = assistant.synthesize(request.text());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header("Content-Disposition", "inline; filename=budgeting-response.mp3")
                .body(audio);
    }

    public record ChatRequest(String text) {}
}
