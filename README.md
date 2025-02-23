# Query Builder

## Descrição

O Query Builder é uma ferramenta poderosa para construir consultas SQL de forma programática em Java. Ele permite criar consultas complexas de maneira simples e intuitiva, evitando erros comuns de sintaxe e facilitando a manutenção do código.

## Funcionalidades

- Construção de consultas SQL SELECT, INSERT, UPDATE e DELETE.
- Suporte a cláusulas WHERE, JOIN, ORDER BY, GROUP BY, HAVING, etc.
- Integração com diferentes bancos de dados.
- API fluente para construção de consultas.

## Requisitos

- Java 8 ou superior.
- Biblioteca JDBC para o banco de dados que você deseja utilizar.

## Instalação

1. Clone o repositório para o seu ambiente local:
    ```bash
    git clone https://github.com/seu-usuario/query-builder.git
    ```
2. Adicione o projeto ao seu ambiente de desenvolvimento Java.

## Utilização

### Exemplo de Uso

Abaixo está um exemplo de como utilizar o Query Builder para construir uma consulta SQL em Java:

```java
import com.querybuilder.builder.QueryBuilder;
import com.querybuilder.Enum.Operator;

public class Main {
    public static void main(String[] args) {
        QueryBuilder qb = new QueryBuilder("users");

        String query = qb.select("name", "age")
                         .where("age", Operator.GREATER_THAN, 18)
                         .orderBy("name", true)
                         .build();

        System.out.println(query);
        // Saída: SELECT name, age FROM users WHERE age > 18 ORDER BY name ASC;
    }
}
```

### Conectando ao Banco de Dados

Para executar a consulta construída, você precisará se conectar ao banco de dados usando JDBC. Veja um exemplo de como fazer isso:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/seu_banco_de_dados";
        String user = "seu_usuario";
        String password = "sua_senha";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            QueryBuilder qb = new QueryBuilder("users");
            String query = qb.select("name", "age")
                             .where("age", Operator.GREATER_THAN, 18)
                             .orderBy("name", true)
                             .build();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") + ", Age: " + rs.getInt("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Inserindo Dados

Para inserir dados em uma tabela, você pode usar o método `insertInto` e `set`:

```java
import com.querybuilder.builder.QueryBuilder;

public class InsertExample {
    public static void main(String[] args) {
        QueryBuilder qb = new QueryBuilder("users");

        String query = qb.insertInto("users")
                         .set("name", "John Doe")
                         .set("age", 30)
                         .build();

        System.out.println(query);
        // Saída: INSERT INTO users (name, age) VALUES (?, ?);
    }
}
```

### Atualizando Dados

Para atualizar dados em uma tabela, você pode usar o método `update`:

```java
import com.querybuilder.builder.QueryBuilder;
import com.querybuilder.Enum.Operator;

public class UpdateExample {
    public static void main(String[] args) {
        QueryBuilder qb = new QueryBuilder("users");

        String query = qb.update("name", "John Doe")
                         .where("id", Operator.EQUALS, 1)
                         .build();

        System.out.println(query);
        // Saída: UPDATE users SET name = ? WHERE id = ?;
    }
}
```

### Deletando Dados

Para deletar dados de uma tabela, você pode usar o método `delete`:

```java
import com.querybuilder.builder.QueryBuilder;
import com.querybuilder.Enum.Operator;

public class DeleteExample {
    public static void main(String[] args) {
        QueryBuilder qb = new QueryBuilder("users");

        String query = qb.delete()
                         .where("id", Operator.EQUALS, 1)
                         .build();

        System.out.println(query);
        // Saída: DELETE FROM users WHERE id = ?;
    }
}
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