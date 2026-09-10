package br.com.thaynarbd.budgeting.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class FinancialAssistantService {
    private static final String SYSTEM_PROMPT = """
            Você é o assistente financeiro do projeto Budgeting AI.
            Responda em português do Brasil, com clareza e sem inventar valores.
            Use as ferramentas disponíveis sempre que a solicitação envolver dados financeiros reais.
            Para registrar uma transação, confirme implicitamente o tipo correto (receita ou despesa), valor, descrição e categoria.
            Quando faltarem dados indispensáveis, peça apenas o dado que realmente falta.
            Ao responder consultas, apresente valores em reais e datas de forma amigável.
            Não forneça aconselhamento financeiro profissional; apenas organize e interprete os dados registrados.
            """;

    private final ChatClient chatClient;
    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;
    private final FinancialTools financialTools;

    public FinancialAssistantService(ChatClient.Builder chatClientBuilder,
                                     TranscriptionModel transcriptionModel,
                                     TextToSpeechModel textToSpeechModel,
                                     FinancialTools financialTools) {
        this.chatClient = chatClientBuilder.build();
        this.transcriptionModel = transcriptionModel;
        this.textToSpeechModel = textToSpeechModel;
        this.financialTools = financialTools;
    }

    public String chat(String text) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("text é obrigatório");
        String response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(text)
                .tools(financialTools)
                .call()
                .content();
        if (response == null || response.isBlank()) throw new IllegalStateException("A IA não retornou uma resposta");
        return response.trim();
    }

    public String transcribe(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("O arquivo de áudio é obrigatório");
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override public String getFilename() { return file.getOriginalFilename() == null ? "audio.wav" : file.getOriginalFilename(); }
        };
        String transcription = transcriptionModel.transcribe(resource);
        if (transcription == null || transcription.isBlank()) throw new IllegalStateException("Não foi possível transcrever o áudio");
        return transcription.trim();
    }

    public AssistantResponse processVoice(MultipartFile file) throws IOException {
        String transcription = transcribe(file);
        String answer = chat(transcription);
        byte[] audio = textToSpeechModel.call(answer);
        return new AssistantResponse(transcription, answer, Base64.getEncoder().encodeToString(audio));
    }

    public byte[] synthesize(String text) {
        return textToSpeechModel.call(text);
    }
}
