package com.example.demo1;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import com.example.demo1.ResultEntry;



public class DepositCalculator extends Application {

    private TextField principalField, rateField, timeField;
    private Label resultLabel;
    private TableView<ResultEntry> resultTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Депозитный калькулятор");

        GridPane grid = createInputGrid();
        VBox vBox = createResultBox();

        Button calculateButton = new Button("Вычислить");
        calculateButton.setOnAction(e -> calculateAndDisplayResult());

        Button saveButton = new Button("Сохранить в файл");
        saveButton.setOnAction(e -> saveResultsToFile());

        VBox buttonsBox = new VBox(10, calculateButton, saveButton);
        buttonsBox.setAlignment(Pos.CENTER);

        HBox mainBox = new HBox(20, grid, vBox, buttonsBox);
        mainBox.setPadding(new Insets(20));
        mainBox.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(mainBox));
        primaryStage.show();
    }

    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Label principalLabel = new Label("Основная сумма:");
        principalField = new TextField();
        addTextLimiter(principalField, 10);

        Label rateLabel = new Label("Годовая ставка (%):");
        rateField = new TextField();
        addTextLimiter(rateField, 5);

        Label timeLabel = new Label("Срок в годах:");
        timeField = new TextField();
        addTextLimiter(timeField, 5);

        grid.addColumn(0, principalLabel, rateLabel, timeLabel);
        grid.addColumn(1, principalField, rateField, timeField);

        return grid;
    }

    private VBox createResultBox() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        resultLabel = new Label("");
        resultTable = new TableView<>();
        TableColumn<ResultEntry, String> principalCol = new TableColumn<>("Основная сумма");
        principalCol.setCellValueFactory(new PropertyValueFactory<>("principal"));

        TableColumn<ResultEntry, String> rateCol = new TableColumn<>("Ставка");
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<ResultEntry, String> timeCol = new TableColumn<>("Срок");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<ResultEntry, String> resultCol = new TableColumn<>("Результат");
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        resultTable.getColumns().addAll(principalCol, rateCol, timeCol, resultCol);
        vBox.getChildren().addAll(resultLabel, resultTable);

        return vBox;
    }

    private void calculateAndDisplayResult() {
        try {
            double principal = Double.parseDouble(principalField.getText());
            double rate = Double.parseDouble(rateField.getText());
            double time = Double.parseDouble(timeField.getText());

            if (principal < 0 || rate < 0 || time < 0) {
                showErrorAlert("Введены отрицательные значения.");
                return;
            }

            double result = principal * Math.pow(1 + rate / 100, time);

            ResultEntry entry = new ResultEntry (principal, rate, time, result);

            resultLabel.setText("Результат: " + result);
            resultTable.getItems().add(entry);

        } catch (NumberFormatException e) {
            showErrorAlert("Пожалуйста, введите корректные числовые значения.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addTextLimiter(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    private void saveResultsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("deposit_results.txt"))) {
            for (ResultEntry entry : resultTable.getItems()) {
                writer.println(
                        String.format(
                                "Основная сумма: %.2f\nСтавка: %.2f%%\nСрок: %.2f лет\nРезультат: %.2f",
                                entry.getPrincipal(),
                                entry.getRate() * 100,
                                entry.getTime(),
                                entry.getResult()
                        )
                );
            }

            if (writer.checkError()) {
                throw new IOException("Failed to write to file");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Результаты сохранены в файл deposit_results.txt");
                alert.showAndWait();
            }
        } catch (IOException e) {
            showErrorAlert("Ошибка при сохранении файла: " + e.getMessage());
        }
    }
}
