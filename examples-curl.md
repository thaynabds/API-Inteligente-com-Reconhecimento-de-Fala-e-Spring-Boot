# Exemplos rápidos com cURL

## Criar receita

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{
    "description": "Salário",
    "amount": 5000.00,
    "type": "INCOME",
    "category": "salário",
    "date": "2026-09-05"
  }'
```

## Criar despesa

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{
    "description": "Mercado",
    "amount": 180.50,
    "type": "EXPENSE",
    "category": "alimentação",
    "date": "2026-09-10"
  }'
```

## Perguntar à IA

```bash
curl -X POST http://localhost:8080/api/assistant/chat \
  -H 'Content-Type: application/json' \
  -d '{"text":"Qual meu saldo neste mês?"}'
```
