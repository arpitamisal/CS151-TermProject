package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    // Views
    @FXML private VBox homeBox;
    @FXML private VBox defineBox;
    @FXML private VBox listBox;

    // Define form
    @FXML private TextField nameField;
    @FXML private Label statusLabel;

    // Table
    @FXML private TableView<String> table;
    @FXML private TableColumn<String, String> nameCol;

    // Storage
    private final Path storePath =
            Paths.get(System.getProperty("user.home"), ".cs151_languages.txt");

    // Data
    private final ObservableList<String> languages = FXCollections.observableArrayList();
    private SortedList<String> sortedLanguages;

    @FXML
    private void initialize() {
        // Column shows the String itself, and sorts case-insensitively
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        nameCol.setComparator(String::compareToIgnoreCase);

        // Wrap data with SortedList and BIND comparators
        sortedLanguages = new SortedList<>(languages);
        table.setItems(sortedLanguages);
        sortedLanguages.comparatorProperty().bind(table.comparatorProperty());

        // Default sort A→Z on first show
        table.getSortOrder().setAll(nameCol);
        nameCol.setSortType(TableColumn.SortType.ASCENDING);

        // Load from disk (will populate `languages`)
        loadLanguages();
    }


    // Navigation
    @FXML
    protected void showDefine() {
        showOnly(defineBox);
        if (statusLabel != null) statusLabel.setText("Examples: Java, Python");
        if (nameField != null) { nameField.clear(); nameField.requestFocus(); }
    }

    @FXML
    protected void showHome() { showOnly(homeBox); }

    @FXML
    protected void showList() {
        showOnly(listBox);
        if (table != null) table.sort(); // keep it A→Z
    }

    private void showOnly(VBox target) {
        for (VBox box : List.of(homeBox, defineBox, listBox)) {
            if (box == null) continue;
            boolean show = box == target;
            box.setVisible(show);
            box.setManaged(show);
        }
    }

    // Actions
    @FXML
    protected void saveDefine() {
        String name = nameField == null ? "" : nameField.getText().trim();
        if (name.isEmpty()) {
            if (statusLabel != null) statusLabel.setText("Name is required.");
            if (nameField != null) nameField.requestFocus();
            return;
        }

        // Avoid duplicates (case-insensitive)
        boolean exists = languages.stream()
                .anyMatch(s -> s.equalsIgnoreCase(name));
        if (exists) {
            if (statusLabel != null) statusLabel.setText("Already exists: " + name);
            return;
        }

        languages.add(name);
        persistLanguages();
        if (statusLabel != null) statusLabel.setText("Saved: " + name);
        if (nameField != null) { nameField.clear(); nameField.requestFocus(); }
    }

    private void loadLanguages() {
        languages.clear();
        if (Files.exists(storePath)) {
            try {
                List<String> lines = Files.readAllLines(storePath, StandardCharsets.UTF_8).stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());
                languages.addAll(lines);
            } catch (IOException e) {
            }
        }
    }

    private void persistLanguages() {
        try {
            List<String> sorted = languages.stream()
                    .distinct()
                    .sorted(String::compareToIgnoreCase)
                    .collect(Collectors.toList());
            Files.write(storePath, sorted, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            if (statusLabel != null) statusLabel.setText("Error saving data.");
        }
    }
}
