# LuggyCar

# Sumário

- [Sobre o Projeto](#Sobre-o-Projeto)
- [Endpoints](#Endpoints)
- [Configuração do Projeto](#Configuração-do-Projeto)
- [Contribuição](#Contribuição)


## Sobre o Projeto

**LuggyCar** é um sistema de back-end para gestão de aluguel de carros, desenvolvido pela turma de Programação Web II - 2024.2 do Centro Universitário UNIME de Lauro de Freitas. Com o LuggyCar, é possível realizar a administração completa de clientes, usuários, veículos e locações, oferecendo um sistema robusto para o mercado de locação de automóveis.

O sistema permite gerenciar:

- **Clientes** e **usuários** do sistema com segurança.
- **Carros**, incluindo suas categorias, opcionais, acessórios e registro de sinistros.
- **Locações**, proporcionando um controle detalhado de todos os processos de aluguel.

Este projeto foi orientado pelo professor Paulo Reis e detalhado em um levantamento de [requisitos](https://dear-creature-6bf.notion.site/Projeto-Web-2-2024-2-0c6c1c28636549c0b5287e058371c714) no Notion.


### Tecnologias Utilizadas
- Java

- Spring framework
- Maven
- MySQL

## Configuração do Projeto

### Pré-requisitos
* JDK 17 ou superior
* Apache Maven
* MySQL

# Endpoints

## Usuario
Existem dois tipos de usuarios no sistema: 
- ADMIN

Tem autorização de acessar todas as rotas com todos os metodos HTTP.
- USER

Tem autorização de acessar todas as rotas mas somente com os metodos HTTP GET.

1. **Criar um novo usuario**
   - **Método HTTP:** `POST`
   - **Rota:** `/auth/register`
   - **Descrição:** Cria um novo usuario do sistema. 
   É necessário enviar o json a seguir:

```json
{
    "login":"admin",
    "password":"123456789",
    "role":"ADMIN"
}
```

2. **Logar no sistema**
   - **Método HTTP:** `POST`
   - **Rota:** `/auth/login`
   - **Descrição:** recebe um token de acesso com duração de 2 horas
   
```json
{
    "login":"admin",
    "password":"123456789"
}
```
**OBS**: Ao logar no sistema, recebe um token para fazer requisições com validade de 2 horas.

## Veiculos


### Rotas para Veiculos
1. **Listar todos os veiculos**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/vehicle`
   - **Descrição:** Recupera uma lista de registro de todos os veiculos.

2. **Criar um novo veiculos**
   - **Método HTTP:** `POST`
   - **Rota:** `/api/vehicle`
   - **Descrição:** Cria um novo registro de veiculo. 
   É necessário enviar os dados do veiculos no corpo da requisição (modelo json mais a baixo).

3. **Mostrar uma veiculo específico por id**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/vehicle/{id}`
   - **Descrição:** Recupera as informações de um veiculo baseado no `id`.

4. **Atualizar um veiculo em específico**
   - **Método HTTP:** `PUT`
   - **Rota:** `/api/vehicle/{id}`
   - **Descrição:** Atualiza um veiculo específico baseado no `id`. Os dados atualizados devem ser enviados no corpo da requisição. (modelo json mais a baixo).

5. **Excluir um veiculo específico**
   - **Método HTTP:** `DELETE`
   - **Rota:** `/api/vehicle/{id}`
   - **Descrição:** Remove um veiculo específico baseado no `id`.


### Estrutura Json do veiculo
Para criar ou atualizar um veiculo, utilize o seguinte modelo JSON:

```json
{
  "name": "Creta",
  "manufacturer": "HONDA",
  "version": "flex",
  "categoryName": "Cabriolet",
  "urlFipe": "https://www.fipe.org.br/mustang-gt",
  "plate": "CBC1234",
  "color": "SILVER",
  "transmission": "MANUAL",
  "currentKm": "15000",
  "passangerCapacity": "4",
  "trunkCapacity": "450",
  "accessories": ["AIRBAGS", "GPS"],
  "dailyRate": 500.00
}
```

## Clientes
Para diferenciar os clientes juridicos das pessoas fisicas, utilizamos o "personType" como "PF" ou "PJ".
No caso de PF os campos específicos para pessoa jurídica (cnpj, companyName) devem ser nulos, e vice-versa para pessoa jurídica. Essa estrutura permite flexibilidade, adaptando-se às necessidades de identificação e armazenamento de dados para ambos os tipos de pessoa.
O CEP é validado pela API dos correios.

### Rotas para Clientes
1. **Listar todos os clientes**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/client`
   - **Descrição:** Recupera uma lista de registro de todos os clientes.

2. **Criar um novo cliente**
   - **Método HTTP:** `POST`
   - **Rota:** `/api/client`
   - **Descrição:** Cria um novo registro de cliente. 
   É necessário enviar os dados do cliente no corpo da requisição (modelo json mais a baixo).

3. **Mostrar um cliente específico por id**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/client/{id}`
   - **Descrição:** Recupera as informações de um cliente baseado no `id`.

4. **Mostrar um cliente específico por documento**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/client/{doc}`
   - **Descrição:** Recupera as informações de um cliente baseado no CNPJ ou CPF.

5. **Atualizar um cliente em específico**
   - **Método HTTP:** `PUT`
   - **Rota:** `/api/client/{id}`
   - **Descrição:** Atualiza um cliente específico baseado no `id`. Os dados atualizados devem ser enviados no corpo da requisição. (modelo json mais a baixo).

6. **Excluir um cliente específico**
   - **Método HTTP:** `DELETE`
   - **Rota:** `/api/client/{id}`
   - **Descrição:** Remove um cliente específico baseado no `id`.


### Estrutura Json do cliente
Para criar ou atualizar uma cliente, utilize o seguinte modelo JSON:

```json
    
   {
   "personType": "PF",
   "naturalPersonName": "João Silva",
   "cpf": "123.456.455-10",
   "cnpj": null,
   "companyName": null,
   "email": "joao.silva@example.com",
   "gender": "MASCULINO",
   "dateBirth": "1990-05-15",
   "cep": "40000-000",
   "endereco": "Rua das Flores, 123, Bairro Centro"
   } 
    
```


## Rotas para Locações

1. **Listar todoa as locações**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/rent`
   - **Descrição:** Recupera uma lista de registro de todos os locações.

2. **Criar uma nova locação**
   - **Método HTTP:** `POST`
   - **Rota:** `/api/rent`
   - **Descrição:** Cria um novo registro de locação. 
   É necessário enviar os dados da locação no corpo da requisição (modelo json mais a baixo).

3. **Mostrar uma locação específico por id**
   - **Método HTTP:** `GET`
   - **Rota:** `/api/rent/{id}`
   - **Descrição:** Recupera as informações de uma locação baseado no `id`.

4. **Atualizar uma locação em específico**
   - **Método HTTP:** `PUT`
   - **Rota:** `/api/rent/{id}`
   - **Descrição:** Atualiza uma locação específico baseado no `id`. Os dados atualizados devem ser enviados no corpo da requisição. (modelo json mais a baixo).

5. **Excluir uma locação específica**
   - **Método HTTP:** `DELETE`
   - **Rota:** `/api/rent/{id}`
   - **Descrição:** Remove uma locação em específico baseado no `id`.


### Estrutura Json da locação
Para criar ou atualizar uma cliente, utilize o seguinte modelo JSON:

```json
{
	"dailyRate": 100.00,
	"totalDays": 1,
	"deposit": 100.00,
	"kmInitial": 15.000,
	"kmFinal": 15.800,
	"client": {
		"id": 1
	},
	"vehicle":{
		"id": 1
	}
} 
```


### Passo a passo para executar o projeto

#### Recomendamos 
* O uso de IntelliJ IDEA 

#### 1. **Clonar o repositório**
   - Para clonar o projeto, use o comando `git clone` com o URL do repositório.
     ```bash
     git clone https://github.com/MichelNsouza/LuggyCar.git
     ```
   - Navegue para o diretório do projeto:
     ```bash
     cd LuggyCar
     ```

#### 2. **Compilar o projeto**
   - Antes de executar o projeto, você deve garantir que ele está compilado corretamente. No IntelliJ procure pelo icone do maven ou no diretório do projeto, execute o comando Maven para compilar:
     ```bash
     mvn clean install
     ```
   - Isso irá baixar todas as dependências e compilar o código.

#### 4. **Executar o projeto**
   - Para executar o projeto Spring Boot, no IntelliJ você pode clicar no icone verde de play (►) ou pode usar o seguinte comando:
     ```bash
     mvn spring-boot:run
     ```
   - Isso iniciará o servidor embutido (geralmente Tomcat) e a aplicação ficará disponível na porta padrão (geralmente `http://localhost:8080`).

#### 5. **Conclusão**
   - Acesse a aplicação pelo navegador ou use ferramentas como `curl` ou Postman para fazer requisições à API (exemplo: `http://localhost:8080/`).


## Contribuição

* [Elder Borges](https://github.com/eldersb)
* [Isabela Guimarães](https://github.com/guimaraesisabela)
* [Michel Souza](https://github.dev/MichelNsouza)
* [Railan Ibraim](https://github.com/Ibraim999)
* [Thales Souza](https://github.com/Thaleszx7)
* [Thiago Sales](https://github.com/txiami)

Orientação
* [Paulo Reis](https://github.com/PHPauloReis)