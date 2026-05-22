# Calculadora Pastelaria

Sistema de caixa para pastelaria com interface de terminal e interface gráfica (JavaFX).

## Funcionalidades

- Gerenciamento de cardápio (adicionar e remover produtos)
- Registro de pedidos com múltiplos itens
- Aplicação de desconto percentual
- Cálculo de troco
- Persistência do cardápio em arquivo JSON (`produtos.json`)

## Tecnologias

- Java 17
- JavaFX 21
- Maven
- JUnit 5

## Como rodar

### Pelo terminal (Maven)

**Interface gráfica:**
```
mvn javafx:run
```

**Build do JAR (modo terminal):**
```
mvn package
java -jar target/calculadora-pastelaria-1.0.0.jar
```

### Pelo IntelliJ IDEA

**Interface gráfica:**
1. Abra o projeto pelo IntelliJ (`File > Open` e selecione a pasta do projeto)
2. Aguarde o Maven baixar as dependências
3. No painel Maven (lado direito), expanda `Plugins > javafx`
4. Clique duas vezes em `javafx:run`

**Modo terminal:**
1. Abra a classe `Main.java` (`src/main/java/com/pastelaria/calculadora/Main.java`)
2. Clique no botão ▶ ao lado do método `main`
3. Digite `1` no console que aparecer na parte inferior

## Testes

```
mvn test
```

Ou no IntelliJ: clique com o botão direito na pasta `src/test` e selecione **Run All Tests**.
