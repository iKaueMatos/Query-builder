# Query Builder

![GitHub repo size](https://img.shields.io/github/repo-size/seu-usuario/query-builder)
![GitHub contributors](https://img.shields.io/github/contributors/seu-usuario/query-builder)
![GitHub stars](https://img.shields.io/github/stars/seu-usuario/query-builder?style=social)
![GitHub forks](https://img.shields.io/github/forks/seu-usuario/query-builder?style=social)
![GitHub issues](https://img.shields.io/github/issues/seu-usuario/query-builder)
![GitHub license](https://img.shields.io/github/license/seu-usuario/query-builder)

## Descrição

O Query Builder é uma ferramenta poderosa para construir consultas SQL de forma programática. Ele permite criar consultas complexas de maneira simples e intuitiva, sem a necessidade de escrever SQL manualmente.

## Funcionalidades

- Construção de consultas SELECT, INSERT, UPDATE e DELETE.
- Suporte a cláusulas WHERE, JOIN, ORDER BY, GROUP BY e HAVING.
- Suporte a parâmetros nomeados para evitar SQL Injection.
- Fácil integração com diversos bancos de dados.

## Instalação

Para instalar o Query Builder, você pode clonar este repositório e instalar as dependências necessárias:

```bash
git clone https://github.com/seu-usuario/query-builder.git
cd query-builder
npm install
```

## Utilização

### Importação

Para utilizar o Query Builder, você deve importá-lo no seu projeto:

```javascript
const QueryBuilder = require('./caminho/para/query-builder');
```

### Exemplos de Uso

#### SELECT

Para construir uma consulta SELECT:

```javascript
const query = new QueryBuilder()
  .select(['id', 'nome', 'email'])
  .from('usuarios')
  .where('ativo = :ativo', { ativo: true })
  .orderBy('nome', 'ASC')
  .build();

console.log(query);
// Saída: SELECT id, nome, email FROM usuarios WHERE ativo = true ORDER BY nome ASC
```

#### INSERT

Para construir uma consulta INSERT:

```javascript
const query = new QueryBuilder()
  .insert('usuarios', { nome: 'João', email: 'joao@example.com', ativo: true })
  .build();

console.log(query);
// Saída: INSERT INTO usuarios (nome, email, ativo) VALUES ('João', 'joao@example.com', true)
```

#### UPDATE

Para construir uma consulta UPDATE:

```javascript
const query = new QueryBuilder()
  .update('usuarios')
  .set({ nome: 'João Silva', ativo: false })
  .where('id = :id', { id: 1 })
  .build();

console.log(query);
// Saída: UPDATE usuarios SET nome = 'João Silva', ativo = false WHERE id = 1
```

#### DELETE

Para construir uma consulta DELETE:

```javascript
const query = new QueryBuilder()
  .delete()
  .from('usuarios')
  .where('id = :id', { id: 1 })
  .build();

console.log(query);
// Saída: DELETE FROM usuarios WHERE id = 1
```

### Parâmetros Nomeados

O Query Builder suporta parâmetros nomeados para evitar SQL Injection. Você pode usar parâmetros nomeados nas cláusulas WHERE, SET, etc.:

```javascript
const query = new QueryBuilder()
  .select(['id', 'nome'])
  .from('usuarios')
  .where('email = :email', { email: 'joao@example.com' })
  .build();

console.log(query);
// Saída: SELECT id, nome FROM usuarios WHERE email = 'joao@example.com'
```

### JOIN

Para construir uma consulta com JOIN:

```javascript
const query = new QueryBuilder()
  .select(['u.id', 'u.nome', 'p.nome AS perfil'])
  .from('usuarios u')
  .join('perfis p', 'u.perfil_id = p.id')
  .where('u.ativo = :ativo', { ativo: true })
  .build();

console.log(query);
// Saída: SELECT u.id, u.nome, p.nome AS perfil FROM usuarios u JOIN perfis p ON u.perfil_id = p.id WHERE u.ativo = true
```

## Contribuição

Se você deseja contribuir com o projeto, por favor, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Faça push para a branch (`git push origin feature/nova-feature`).
5. Crie um Pull Request.

## Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
