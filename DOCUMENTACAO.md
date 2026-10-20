# Documentacao das Requisicoes da API

Este documento registra cada requisicao realizada nos endpoints da API, com o tipo da requisicao, o corpo enviado e uma breve explicacao do resultado obtido. As capturas de tela dos resultados devem ser anexadas abaixo de cada item.

> Observacao: os IDs retornados podem variar conforme a ordem de execucao. Os exemplos assumem que as entidades foram criadas na ordem apresentada.

---

## 1. Clientes

### 1.1 POST /clientes - Criar cliente
- **Tipo:** POST
- **Corpo:**
```json
{
  "nome": "Joao da Silva",
  "celular": "48999999999",
  "email": "joao@email.com",
  "dataCadastro": "2026-10-20"
}
```
- **Resultado esperado:** HTTP 201 Created, retornando o cliente com o `id` gerado.


### 1.2 GET /clientes/{id} - Consultar cliente por ID
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK com os dados do cliente. HTTP 404 se nao existir.


### 1.3 GET /clientes - Listar clientes
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK com a lista de clientes.


### 1.4 PUT /clientes/{id} - Atualizar cliente
- **Tipo:** PUT
- **Corpo:**
```json
{
  "nome": "Joao da Silva Atualizado",
  "celular": "48988888888",
  "email": "joao.novo@email.com",
  "dataCadastro": "2026-10-20"
}
```
- **Resultado esperado:** HTTP 200 OK com o cliente atualizado.

### 1.5 DELETE /clientes/{id} - Remover cliente
- **Tipo:** DELETE
- **Resultado esperado:** HTTP 204 No Content.


---

## 2. Marcas

### 2.1 POST /marcas - Criar marca
- **Tipo:** POST
- **Corpo:**
```json
{ "nome": "Volkswagen" }
```
- **Resultado esperado:** HTTP 201 Created com a marca criada.

### 2.2 GET /marcas/{id} - Consultar marca por ID
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK.


### 2.3 GET /marcas - Listar marcas
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK com a lista de marcas.


### 2.4 PUT /marcas/{id} - Atualizar marca
- **Tipo:** PUT
- **Corpo:**
```json
{ "nome": "Volkswagen do Brasil" }
```
- **Resultado esperado:** HTTP 200 OK com a marca atualizada.


### 2.5 DELETE /marcas/{id} - Remover marca
- **Tipo:** DELETE
- **Resultado esperado:** HTTP 204 No Content.


---

## 3. Modelos

### 3.1 POST /modelos - Criar modelo
- **Tipo:** POST
- **Corpo:**
```json
{
  "descricao": "Gol",
  "categoria": "PADRAO",
  "marca": { "id": 1 }
}
```
- **Resultado esperado:** HTTP 201 Created com o modelo criado e associado a marca.


### 3.2 GET /modelos/{id} - Consultar modelo por ID
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK.


### 3.3 GET /modelos - Listar modelos
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK com a lista de modelos.


### 3.4 PUT /modelos/{id} - Atualizar modelo
- **Tipo:** PUT
- **Corpo:**
```json
{
  "descricao": "Gol G6",
  "categoria": "MEDIO",
  "marca": { "id": 1 }
}
```
- **Resultado esperado:** HTTP 200 OK com o modelo atualizado.

### 3.5 DELETE /modelos/{id} - Remover modelo
- **Tipo:** DELETE
- **Resultado esperado:** HTTP 204 No Content.


---

## 4. Veiculos

### 4.1 POST /veiculos - Criar veiculo
- **Tipo:** POST
- **Corpo:**
```json
{
  "placa": "ABC1D23",
  "observacoes": "Sem observacoes",
  "cor": { "id": 1 },
  "modelo": { "id": 1 },
  "proprietario": { "id": 1 }
}
```
- **Resultado esperado:** HTTP 201 Created com o veiculo criado e associacoes resolvidas.


### 4.2 GET /veiculos/{id} - Consultar veiculo por ID
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK.


### 4.3 GET /veiculos - Listar veiculos
- **Tipo:** GET
- **Resultado esperado:** HTTP 200 OK com a lista de veiculos.


### 4.4 PUT /veiculos/{id} - Atualizar veiculo
- **Tipo:** PUT
- **Corpo:**
```json
{
  "placa": "XYZ9A87",
  "observacoes": "Atualizado",
  "cor": { "id": 1 },
  "modelo": { "id": 1 },
  "proprietario": { "id": 1 }
}
```
- **Resultado esperado:** HTTP 200 OK com o veiculo atualizado.


### 4.5 DELETE /veiculos/{id} - Remover veiculo
- **Tipo:** DELETE
- **Resultado esperado:** HTTP 204 No Content.


---

## 5. Cores e Motores

Os endpoints `/cores` e `/motores` seguem o mesmo padrao CRUD. Exemplos:

### POST /cores
```json
{ "nome": "Prata" }
```

### POST /motores
```json
{ "potencia": 120, "tipoCombustivel": "FLEX" }
```

- **Resultado esperado:** HTTP 201 Created em ambos os casos.

