# FinTrack — Sistema de Controle Financeiro

Aplicação full-stack para controle de finanças pessoais. Permite registrar entradas e saídas, visualizar o extrato completo e acompanhar o saldo em tempo real.

**Demo:** https://sistema-financeiro-rafa15.vercel.app

## Funcionalidades

• Cadastro de transações (entrada ou saída)
• Visualização do extrato completo
• Cálculo automático do saldo total
• Exclusão de transações

## Tecnologias

**Backend:** Java 17, Spring Boot, Spring Data JPA, MySQL
**Frontend:** React, Vite
**Deploy:** Railway (backend + banco de dados), Vercel (frontend)

## Arquitetura

O backend segue uma arquitetura em camadas tradicional do Spring Boot:

controller → recebe as requisições HTTP
service → contém as regras de negócio
model → entidades JPA mapeadas para o banco
config → configurações da aplicação (CORS, etc.)

## Estrutura de pastas

```
Sistema-Financeiro/
├── frontend/
│   └── src/
├── src/main/java/com/sistemafinanceiro/api/
│   ├── config/
│   ├── controller/
│   ├── model/
│   └── service/
└── pom.xml
```

## Como executar localmente

Pré-requisitos: Java 17+, Node.js 18+ e MySQL em execução.

### Backend

```bash
git clone https://github.com/RafaTorresDev/Sistema-Financeiro.git
cd Sistema-Financeiro
./mvnw spring-boot:run
```

Configure as variáveis de ambiente `DB_URL`, `DB_USER` e `DB_PASSWORD` de acordo com o seu banco MySQL local.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Roadmap / Melhorias futuras

• Autenticação de usuários (Spring Security + JWT)
• Categorização de transações por tipo
• Gráficos de gastos por período
• Testes automatizados (JUnit + Mockito)
• Paginação no extrato de transações

## Licença

Este projeto está sob a licença MIT — veja o arquivo [LICENSE](LICENSE) para mais detalhes.
