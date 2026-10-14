# 💜 Budgeting AI — API Inteligente de Orçamento com Spring AI

API REST inteligente para gerenciamento de transações financeiras, construída em Java com Spring Boot e Spring AI. O projeto transforma comandos de voz em texto, permite que a IA interprete a intenção, execute funções reais da aplicação por Tool Calling e produza uma resposta em áudio.

> Projeto desenvolvido para o **Bootcamp DIO: Java AI Copilot — 10 de setembro de 2026**.

## Rodar as transações sem chave de IA

Requisito: Java 21.

```bash
./gradlew test
./gradlew bootRun --args='--spring.profiles.active=local'
```

A API sobe em `http://localhost:8080` com banco H2, arquivo local em `data/`. Os endpoints de transação funcionam. Os endpoints `/api/assistant` ficam desligados nesse modo.

Exemplo:

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{"description":"Supermercado","amount":185.90,"type":"EXPENSE","category":"alimentação","date":"2026-09-10"}'
```

Para ligar voz e texto com a OpenAI, suba o MySQL e use o perfil `ai`, com `OPENAI_API_KEY` no ambiente:

```bash
docker compose up -d mysql
./gradlew bootRun --args='--spring.profiles.active=ai'
```

## ✨ Destaques

- 🎙️ **Speech-to-Text:** recebe áudio e transforma fala em texto.
- 🧠 **ChatClient + LLM:** interpreta solicitações em linguagem natural.
- 🛠️ **Tool Calling:** a IA acessa funções reais para criar transações, consultar histórico e calcular saldo.
- 💾 **Persistência:** transações salvas em MySQL via Spring Data JPA.
- 🔊 **Text-to-Speech:** converte a resposta final em MP3.
- 🌐 **REST API:** endpoints independentes para transações, chat, transcrição e síntese.
- ✅ **Validação e testes:** regras de domínio, testes unitários e teste de contexto Spring.
- 🐳 **Docker Compose:** banco MySQL pronto para desenvolvimento local.
- 🔁 **CI no GitHub Actions:** execução automática dos testes.

## 🧩 Arquitetura

A aplicação mantém uma separação simples entre regra de negócio, casos de uso e adaptadores:

```text
src/main/java/br/com/thaynarbd/budgeting/
├── domain/
│   ├── model/             # Entidades e conceitos de negócio
│   └── repository/        # Contratos de persistência
│
├── application/
│   ├── dto/               # Objetos de entrada e saída
│   └── usecase/            # Casos de uso da aplicação
│
└── infrastructure/
    ├── ai/                # ChatClient, Tools, transcrição e voz
    ├── persistence/       # JPA + MySQL
    └── web/               # Controllers e tratamento HTTP
```

Essa organização segue a ideia de manter o domínio independente de detalhes como banco, HTTP e provedores externos.

## 🔄 Fluxo principal por voz

```text
Pessoa usuária
      │
      │ arquivo de áudio
      ▼
POST /api/assistant/voice
      │
      ▼
TranscriptionModel
      │
      │ texto
      ▼
ChatClient + FinancialTools
      │
      ├── create_transaction ───► caso de uso ───► MySQL
      │
      ├── list_transactions ────► caso de uso ───► MySQL
      │
      └── get_balance ──────────► caso de uso ───► MySQL
      │
      ▼
Resposta textual da IA
      │
      ▼
TextToSpeechModel
      │
      ▼
MP3 + resposta
```

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem e runtime |
| Spring Boot 4.1.1 | Framework da API |
| Spring AI 2.0.1 | IA generativa e áudio |
| OpenAI | Chat, transcrição e síntese de voz |
| Spring Data JPA | Persistência |
| MySQL 8.4 | Banco de dados |
| Gradle | Build e testes |
| Docker Compose | Ambiente do banco |
| JUnit 5 + Mockito | Testes |
| GitHub Actions | Integração contínua |

As versões adotadas foram atualizadas em relação ao projeto-base da trilha: o repositório público da DIO atualmente descreve o módulo 05 com voz → texto → tool calling → persistência → texto → áudio, enquanto sua configuração ainda referencia versões anteriores/pré-release do ecossistema. Esta implementação usa versões estáveis verificadas em setembro de 2026.

## 🚀 Pré-requisitos

- JDK 21 instalado.
- Gradle 9+ disponível no PATH.
- Docker e Docker Compose.
- Uma chave de API da OpenAI.

## ⚙️ Configuração

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

Edite `.env` e informe sua chave:

```dotenv
OPENAI_API_KEY=sua_chave_aqui
```

**Nunca publique sua chave da OpenAI no GitHub.** O arquivo `.env` já está no `.gitignore`.

## 🐳 Subir o MySQL

```bash
docker compose up -d mysql
```

Verifique:

```bash
docker compose ps
```

## ▶️ Executar a aplicação

Com o MySQL em execução:

```bash
gradle bootRun
```

A API ficará disponível em:

```text
http://localhost:8080
```

Health check:

```text
GET http://localhost:8080/actuator/health
```

## 🧪 Executar testes

```bash
gradle test
```

Para gerar o JAR:

```bash
gradle clean build
```

## 📡 Endpoints

### Criar transação

```http
POST /api/transactions
Content-Type: application/json
```

Exemplo:

```json
{
  "description": "Supermercado",
  "amount": 185.90,
  "type": "EXPENSE",
  "category": "alimentação",
  "date": "2026-09-10"
}
```

### Listar transações

```http
GET /api/transactions?from=2026-09-01&to=2026-09-30
```

### Consultar uma transação

```http
GET /api/transactions/{id}
```

### Consultar saldo

```http
GET /api/transactions/balance?from=2026-09-01&to=2026-09-30
```

Exemplo de resposta:

```json
{
  "from": "2026-09-01",
  "to": "2026-09-30",
  "totalIncome": 5000.00,
  "totalExpense": 2150.00,
  "balance": 2850.00,
  "transactionCount": 3
}
```

### Conversar com a IA por texto

```http
POST /api/assistant/chat
Content-Type: application/json
```

```json
{
  "text": "Quanto eu gastei neste mês?"
}
```

A IA pode chamar a ferramenta `get_balance` e construir a resposta a partir dos dados reais da aplicação.

### Transcrever áudio

```http
POST /api/assistant/transcription
Content-Type: multipart/form-data
```

Campo:

```text
audio = arquivo.mp3
```

### Fluxo completo por voz

```http
POST /api/assistant/voice
Content-Type: multipart/form-data
```

Campo:

```text
audio = arquivo.mp3
```

Exemplo com cURL:

```bash
curl -X POST http://localhost:8080/api/assistant/voice \
  -F "audio=@exemplos/registrar-mercado.wav"
```

A resposta contém:

```json
{
  "transcription": "Gastei 80 reais no mercado hoje",
  "answer": "Registrei a despesa de R$ 80,00 na categoria alimentação.",
  "audioBase64": "..."
}
```

O áudio retornado em `audioBase64` está em formato MP3.

### Gerar áudio a partir de texto

```http
POST /api/assistant/speech
Content-Type: application/json
```

```json
{
  "text": "Seu saldo atual é de dois mil reais."
}
```

O endpoint retorna `audio/mpeg`.

## 🧠 Ferramentas expostas à IA

### `create_transaction`
Registra uma receita ou despesa no banco através do caso de uso da aplicação.

### `list_transactions`
Consulta transações reais em um intervalo de datas.

### `get_balance`
Calcula receitas, despesas, saldo e quantidade de transações no período.

A aplicação é responsável por executar as ferramentas; o modelo somente solicita o uso delas. Isso mantém a lógica de negócio dentro dos casos de uso e reduz o acoplamento entre LLM e infraestrutura.

## 🎯 Melhoria implementada

Além do fluxo mínimo de voz, esta versão amplia o projeto com três capacidades de Tool Calling:

1. Registro de novas transações.
2. Consulta do histórico financeiro.
3. Cálculo de saldo por período.

Também foram adicionados:

- validações de entrada;
- resposta padronizada para erros HTTP;
- Actuator para health check;
- testes unitários;
- teste de carregamento do contexto Spring;
- pipeline de CI no GitHub Actions;
- documentação de execução e testes.

## 🔐 Segurança e boas práticas

- Segredos ficam fora do código-fonte.
- `.env` é ignorado pelo Git.
- A IA não acessa diretamente o banco: usa ferramentas delimitadas pela aplicação.
- O domínio valida invariantes antes da persistência.
- Limite de upload de áudio configurado em 20 MB.

## 📚 Relação com o projeto da DIO

O ponto de partida conceitual é o módulo **05-spring-ai** da trilha Spring Boot da Digital Innovation One, que documenta o fluxo de processamento de comandos de voz, uso de `TranscriptionModel`, `ChatClient`, `@Tool` e `TextToSpeechModel`. A arquitetura desta entrega foi organizada como um repositório próprio, com melhorias e decisões de implementação adicionais.

Repositório-base da trilha:
https://github.com/digitalinnovationone/dio-spring-boot-learning-track

Material do projeto final:
https://github.com/digitalinnovationone/dio-spring-boot-learning-track/tree/main/05-spring-ai

## 👩‍💻 Autora

### Thayná Batista da Silva

📧 thaynabdstec@gmail.com · 📱 +55 (81) 97912-6121  
Estudante de **Análise e Desenvolvimento de Sistemas** — Faculdade Senac de Pernambuco — Recife/PE, Brasil  
Formação em andamento: **2025–2027**

---

Feito com 💜 por **Thayná Batista da Silva** para o **Bootcamp DIO: Java AI Copilot, 10 de setembro de 2026.**

**Copyright © 2026 — ThaynaBDSTec — Todos os direitos reservados.**
