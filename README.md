# Rastreador Espacial

O **Rastreador Espacial** é uma aplicação em Java para rastrear e avaliar o risco de objetos espaciais e visualizar fenômenos astronômicos através do consumo de APIs públicas da NASA (NeoWs e APOD) e Celestrak com suporte a cache local.

---

## 🔑 Obtenção da Chave de API da NASA

Para utilizar as APIs da NASA sem limites restritivos de requisição, você pode obter uma chave gratuita da seguinte forma:

1. Acesse o portal [NASA Open APIs](https://api.nasa.gov/).
2. Preencha o formulário de cadastro gratuito ("Generate API Key") com seu nome e e-mail.
3. Copie a chave de API fornecida.
4. Crie um arquivo `.env` na raiz do projeto (ou copie a partir de `.env.example`) e configure sua chave:
   ```env
   NASA_API_KEY=sua_chave_aqui
   ```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 25 ou superior
- Apache Maven 3.8+

### Compilar o projeto
```bash
mvn clean compile
```

### Executar os testes
```bash
mvn test
```

### Executar a aplicação
```bash
mvn compile exec:java
```

No menu principal, escolha `4` e depois `4` para abrir a página oficial da foto astronômica do dia no navegador padrão. A imagem não é exibida dentro do terminal.

---

## Alterações de arquitetura e comportamento

### Padrões criacionais

- **Factory (`SpaceClientFactory`)**: concentra a criação dos clientes NeoWs, APOD, Celestrak e SBDB. Isso evita espalhar a instanciação das implementações concretas e mantém a criação dos clientes em um único ponto.
- **Builder (`MenuContext.Builder`)**: permite montar o contexto da aplicação com valores padrão ou substituir dependências individualmente. Isso deixa a inicialização mais legível e facilita configurar clientes, serviço de dados e saída em testes sem multiplicar construtores.

### Padrões estruturais

- **Facade (`SpaceTrackingFacade`)**: centraliza o acesso às fontes de dados, ao serviço de cache e ao registro de objetos. Isso oferece uma entrada única para operações comuns de carregamento e criação de snapshots, reduzindo o acoplamento entre quem usa os dados e os detalhes de cada API.
- **Adapter (`SpaceObjectDisplayAdapter`)**: adapta asteroides, cometas e satélites para formatos textuais consistentes. A formatação específica por tipo fica isolada do menu, facilitando a manutenção e a inclusão de novos tipos de objeto.

### Padrões comportamentais

- **Observer (`MenuContext`, `FeedObserver` e `ConsoleFeedObserver`)**: o contexto notifica os observadores quando um feed é carregado ou falha. O observador de console apresenta o resultado sem acoplar a lógica de atualização à saída.
- **Visitor (`SpaceObjectVisitor` e `TipoObjetoVisitor`)**: permite executar operações sobre asteroides, cometas e satélites por meio de visitantes, mantendo a lógica dessas operações fora das classes de domínio.
- **Template Method (`FonteDeDados.carregar`)**: define a sequência comum para carregar um feed — buscar dados, interpretar, registrar objetos e gerar o resultado — enquanto cada fonte fornece sua interpretação específica.
- **State (`MenuNavigator` e `Screen`)**: cada tela representa o estado atual do menu e decide a próxima navegação. O navegador delega a exibição e o tratamento da escolha à tela ativa e mantém uma pilha para permitir voltar.
