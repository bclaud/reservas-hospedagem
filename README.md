# Sistema de reservas de hospedagens
Desenvolvido para fins de aprendizado

CRUD com os domínios abaixo:

- Endereco
  - id;
  - cep;
  - logradouro;
  - numero;
  - complemento;
  - bairro;
  - cidade;
  - estado;
- Usuario
  - id;
  - avatarURL
  - nome;
  - email;
  - senha;
  - cpf;
  - dataNascimento;
  - Endereco endereco;
- CaracteristicaImovel
  - id;
  - descricao;
- Imovel
  - id;
  - identificacao;
  - TipoImovel tipoImovel
    - Apartamento
    - Casa
    - Hotel
    - Pousada
  - Endereco endereco;
  - Usuario proprietario;
  - List <CaracteristicaImovel> caracteristicas;
- FormaPagamento
  - Cartão de Crédito
  - Cartão de Débito
  - Pix
  - Dinheiro
- Anuncio
  - id;
  - TipoAnuncio tipoAnuncio;
    - Completo
    - Quarto
  - Imovel imovel;
  - Usuario anunciante;
  - valorDiaria;
  - List<FormaPagamento> formasAceitas;
  - descricao;
- Periodo
  - dataHoraInicial;
  - dataHoraFinal;
- Reserva
  - id;
  - Usuario solicitante;
  - Anuncio anuncio;
  - Periodo periodo;
  - quantidadePessoas;
  - dataHoraReserva;
  - Pagamento pagamento;
- Pagamento
  - valorTotal;
  - FormaPagamento formaEscolhida;
  - StatusPagamento statusPagamento;
    - Pendente
    - Pago
    - Estornado
    - Cancelado

## Requisitos 

- jdk-8
- plugin Lombok em sua respectiva IDE
- git
- maven

ou

- Docker devidamente instalado se for rodar pela imagem disponibilizada

## Como Executar

Clone este repositório, abra o terminal na pasta do projeto e execute o comando abaixo

```bash
mvn spring-boot:run
```

ou utilize a imagem disponível no docker hub 🐳

```bash
docker run -p 8080:8080 -d --name reservas baclaud/tcc-bclaud:latest
```

***Endpoints***

Collection postman  https://www.postman.com/collections/ea743a6f9b05cf404b3e

Com a aplicação rodando, acesse http://localhost:8080/swagger-ui/ para conferir as especificações completas 

### A Desenvolver

- Retorno em Page para o endpoint **{{baseUrl}}/reservas/solicitantes/:idSolicitante?dataHoraFinal&dataHoraInicial** (Concluido)
- Testes unitários para toda camada service (Cerca de 80%)
- Criar interface para calculo de impostos e taxas ao calcular valor da reserva (Opcional)

