# 🛍️ Wishlist API

API desenvolvida com o objetivo de gerenciar a lista de desejos (Wishlist) de clientes de um marketplace de forma simples, eficiente e seguindo boas práticas de arquitetura.  
A aplicação permite **adicionar**, **remover**, **consultar produtos** e **verificar a existência** de itens dentro da wishlist do cliente.

---

# ✅ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.7**
- **MongoDB (NoSQL)**
- **Maven**
- **JUnit / Mockito**

---

# 🧱 Arquitetura da Aplicação

A aplicação segue os princípios da **Clean Architecture**.

## 📂 Organização das Camadas

### **Domain**
- Regras de negócio puras  
- Entidade `Wishlist`  
- Entidade `ProductItem`  
- Exceptions  

### **Application (Use Cases)**
- Casos de uso:
  - adicionar item  
  - remover item  
  - consultar itens  
  - verificar existência de item  

### **Infrastructure**
- Controllers REST  
- Repositório do MongoDB  
- Documentos persistidos  

## 🧠 Decisões Importantes

- O **customerId é o próprio `_id` do documento** no MongoDB.  
- O domínio impõe:
  - Limite de **20 produtos**
  - Produto não pode ser duplicado  
  - `productId` não pode ser nulo  
  - Atualização automática de timestamps  

---

# 🚀 Como Rodar o Projeto

## 1. Clonar o repositório

```bash
git clone https://github.com/lidiamac/customer-wishlist-service.git
cd customer-wishlist-service
```

## 2. Configurar o MongoDB

```
spring.data.mongodb.uri=mongodb://localhost:27017/wishlist
```

## 3. Executar a aplicação

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```
http://localhost:8080
```

---

# 🧪 Testando a API

## ✅ Coleção Postman

Importe:

```
src/main/resources/WishlistRequests.json
```

Contém todos os endpoints configurados.

---

# 🔌 Endpoints da API

## ➕ Adicionar produto à wishlist
**POST**
```
/wishlist/{customerId}/products
```

Body:
```json
{
  "productId": 815181898999
}
```

---

## ➖ Remover produto da wishlist
**DELETE**
```
/wishlist/{customerId}/products/{productId}
```
- Ao remover o último item, a wishlist é **excluída automaticamente**

---

## 🔍 Verificar se produto está na wishlist
**GET**
```
/wishlist/{customerId}/products/{productId}
```
Retorno:
```json
true
```
ou
```json
false
```

---

## 📋 Listar todos os produtos da wishlist
**GET**
```
/wishlist/{customerId}
```

Exemplo de resposta:
```json
{
  "customerId": "1a9f3c7e4b12d89a0f3e5680",
  "items": [
    { "productId": 815181899 },
    { "productId": 17578578 }
  ]
}
```

---

# ✅ Regras de Negócio

- Limite de **20 produtos**
- Produto **não pode ser duplicado**
- `productId` **não pode ser nulo**

---

# 📂 Estrutura de Pastas

```
src/main/java/com/ecommerce/customer/wishlist/
│
├── domain
│   ├── Wishlist.java
│   ├── ProductItem.java
│   └── exceptions/
│
├── application
│   ├── usecases/
│   ├── repository/
│   └── dto/
│
└── infrastructure
    ├── controller/
    ├── repository/
    └── document/
```

---

# 🔮 Melhorias Futuras
- Cache com Redis  
- Paginação nos itens 
- Logs
- Excluir wishlist quando a listagem de itens está vazia

---

# 👩‍💻 Autora

**Lidia Maciel**  
Desenvolvedora Backend Java  

