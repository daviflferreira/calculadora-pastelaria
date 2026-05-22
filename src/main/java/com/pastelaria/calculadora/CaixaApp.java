package com.pastelaria.calculadora;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class CaixaApp extends Application {

    private final GerenciadorCardapio gerenciador = new GerenciadorCardapio("produtos.json");
    private final CalculadoraPedido calculadoraPedido = new CalculadoraPedido();

    private ListView<Produto> cardapioList;
    private Button removerBtn;
    private TextField nomeField;
    private TextField precoField;

    private TableView<ItemPedido> pedidoTable;
    private Label totalLabel;
    private TextField descontoField;
    private TextField valorPagoField;
    private Label trocoLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Caixa - Cantinho do Pastel");

        SplitPane splitPane = new SplitPane(criarPainelCardapio(), criarPainelPedido());
        splitPane.setDividerPositions(0.45);

        stage.setScene(new Scene(splitPane, 900, 600));
        stage.show();
    }

    // ── Painel esquerdo ──────────────────────────────────────────────────────

    private VBox criarPainelCardapio() {
        Label titulo = new Label("CARDÁPIO");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        cardapioList = new ListView<>();
        atualizarListaCardapio();
        VBox.setVgrow(cardapioList, Priority.ALWAYS);

        cardapioList.setOnMouseClicked(e -> {
            Produto selecionado = cardapioList.getSelectionModel().getSelectedItem();
            if (selecionado != null) adicionarAoPedido(selecionado);
        });

        cardapioList.getSelectionModel().selectedItemProperty().addListener(
                (obs, antigo, novo) -> removerBtn.setDisable(novo == null));

        nomeField = new TextField();
        nomeField.setPromptText("Nome do produto");

        precoField = new TextField();
        precoField.setPromptText("Preço (ex: 8.50)");

        Button adicionarBtn = new Button("Adicionar");
        adicionarBtn.setMaxWidth(Double.MAX_VALUE);
        adicionarBtn.setOnAction(e -> adicionarProduto());

        removerBtn = new Button("Remover");
        removerBtn.setMaxWidth(Double.MAX_VALUE);
        removerBtn.setDisable(true);
        removerBtn.setOnAction(e -> removerProduto());

        HBox botoes = new HBox(8, adicionarBtn, removerBtn);
        HBox.setHgrow(adicionarBtn, Priority.ALWAYS);
        HBox.setHgrow(removerBtn, Priority.ALWAYS);

        VBox painel = new VBox(8, titulo, cardapioList, nomeField, precoField, botoes);
        painel.setPadding(new Insets(12));
        return painel;
    }

    // ── Painel direito ───────────────────────────────────────────────────────

    private VBox criarPainelPedido() {
        Label titulo = new Label("PEDIDO ATUAL");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TableColumn<ItemPedido, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getNome()));
        colProduto.setPrefWidth(200);

        TableColumn<ItemPedido, String> colQtd = new TableColumn<>("Qtd");
        colQtd.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getQuantidade())));
        colQtd.setPrefWidth(50);

        TableColumn<ItemPedido, String> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(cd ->
                new SimpleStringProperty(String.format("R$ %.2f",
                        cd.getValue().getPreco() * cd.getValue().getQuantidade())));
        colSubtotal.setPrefWidth(100);

        pedidoTable = new TableView<>();
        pedidoTable.setItems(FXCollections.observableArrayList());
        pedidoTable.getColumns().add(colProduto);
        pedidoTable.getColumns().add(colQtd);
        pedidoTable.getColumns().add(colSubtotal);
        pedidoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        VBox.setVgrow(pedidoTable, Priority.ALWAYS);

        totalLabel = new Label("Total: R$ 0,00");
        totalLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");

        descontoField = new TextField();
        descontoField.setPromptText("Desconto (%)");

        valorPagoField = new TextField();
        valorPagoField.setPromptText("Valor pago (R$)");

        Button novoBtn = new Button("Novo Pedido");
        novoBtn.setOnAction(e -> novoPedido());

        Button finalizarBtn = new Button("Finalizar");
        finalizarBtn.setOnAction(e -> finalizarPedido());

        HBox botoes = new HBox(8, novoBtn, finalizarBtn);

        trocoLabel = new Label("Troco: R$ ---");
        trocoLabel.setStyle("-fx-font-size: 13;");

        VBox painel = new VBox(8,
                titulo, pedidoTable, totalLabel,
                new Label("Desconto (%):"), descontoField,
                new Label("Valor pago:"), valorPagoField,
                botoes, trocoLabel);
        painel.setPadding(new Insets(12));
        return painel;
    }

    // ── Ações ────────────────────────────────────────────────────────────────

    private void adicionarAoPedido(Produto produto) {
        Optional<ItemPedido> existente = pedidoTable.getItems().stream()
                .filter(i -> i.getId() == produto.getId())
                .findFirst();

        if (existente.isPresent()) {
            existente.get().incrementar();
            pedidoTable.refresh();
        } else {
            pedidoTable.getItems().add(new ItemPedido(produto));
        }
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = pedidoTable.getItems().stream()
                .mapToDouble(i -> i.getPreco() * i.getQuantidade())
                .sum();
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    private void adicionarProduto() {
        String nome = nomeField.getText().trim();
        String precoStr = precoField.getText().trim().replace(",", ".");

        if (nome.isBlank()) {
            mostrarErro("Nome não pode ser vazio.");
            return;
        }
        try {
            double preco = Double.parseDouble(precoStr);
            gerenciador.adicionarProduto(nome, preco);
            atualizarListaCardapio();
            nomeField.clear();
            precoField.clear();
        } catch (NumberFormatException e) {
            mostrarErro("Preço inválido. Use ponto ou vírgula como separador decimal.");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void removerProduto() {
        Produto selecionado = cardapioList.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;
        gerenciador.removerProduto(selecionado.getId());
        pedidoTable.getItems().removeIf(i -> i.getId() == selecionado.getId());
        atualizarListaCardapio();
        atualizarTotal();
    }

    private void novoPedido() {
        pedidoTable.getItems().clear();
        descontoField.clear();
        valorPagoField.clear();
        totalLabel.setText("Total: R$ 0,00");
        trocoLabel.setText("Troco: R$ ---");
    }

    private void finalizarPedido() {
        if (pedidoTable.getItems().isEmpty()) {
            mostrarErro("Adicione pelo menos um item ao pedido.");
            return;
        }

        double total = pedidoTable.getItems().stream()
                .mapToDouble(i -> i.getPreco() * i.getQuantidade())
                .sum();

        String descontoStr = descontoField.getText().trim().replace(",", ".");
        if (!descontoStr.isBlank()) {
            try {
                double desconto = Double.parseDouble(descontoStr);
                total = calculadoraPedido.aplicarDesconto(total, desconto);
            } catch (NumberFormatException e) {
                mostrarErro("Desconto inválido.");
                return;
            } catch (IllegalArgumentException e) {
                mostrarErro(e.getMessage());
                return;
            }
        }

        String valorPagoStr = valorPagoField.getText().trim().replace(",", ".");
        if (valorPagoStr.isBlank()) {
            mostrarErro("Informe o valor pago.");
            return;
        }
        try {
            double valorPago = Double.parseDouble(valorPagoStr);
            double troco = calculadoraPedido.calcularTroco(total, valorPago);
            trocoLabel.setText(String.format("Troco: R$ %.2f", troco));
        } catch (NumberFormatException e) {
            mostrarErro("Valor pago inválido.");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizarListaCardapio() {
        cardapioList.setItems(FXCollections.observableArrayList(gerenciador.listarTodos()));
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // ── Modelo do item do pedido ─────────────────────────────────────────────

    public static class ItemPedido {
        private final int id;
        private final String nome;
        private final double preco;
        private int quantidade;

        public ItemPedido(Produto produto) {
            this.id = produto.getId();
            this.nome = produto.getNome();
            this.preco = produto.getPreco();
            this.quantidade = 1;
        }

        public void incrementar()  { this.quantidade++; }

        public int getId()         { return id; }
        public String getNome()    { return nome; }
        public double getPreco()   { return preco; }
        public int getQuantidade() { return quantidade; }
    }
}
