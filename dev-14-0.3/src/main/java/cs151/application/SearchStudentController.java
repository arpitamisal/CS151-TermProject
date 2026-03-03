package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Objects;
import java.util.stream.Collectors;

public class SearchStudentController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameCol, statusCol, langsCol, dbsCol, roleCol;
    @FXML private TableColumn<Student, Void> actionCol;

    @FXML private TextField nameField, langField, dbField;
    @FXML private ComboBox<String> statusBox, roleBox;

    private ObservableList<Student> master;
    private FilteredList<Student> filtered;

    @FXML
    public void initialize() {
        // Basic property mappings
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getAcademicStatus() == null
                                ? ""
                                : c.getValue().getAcademicStatus().getDisplayName()
                )
        );

        langsCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getProgrammingLanguages() == null
                                ? ""
                                : c.getValue().getProgrammingLanguages().stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "))
                )
        );

        dbsCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDatabases() == null
                                ? ""
                                : c.getValue().getDatabases().stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "))
                )
        );

        roleCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPreferredRole() == null
                                ? ""
                                : c.getValue().getPreferredRole().getDisplayName()
                )
        );

        // Load data (replace with your persistence later)
        master = FXCollections.observableArrayList(StudentData.loadAll());
        filtered = new FilteredList<>(master, s -> true);
        studentTable.setItems(filtered);

        // Combo options
        statusBox.setItems(FXCollections.observableArrayList(
                "Any", "Freshman", "Sophomore", "Junior", "Senior", "Graduate"
        ));
        roleBox.setItems(FXCollections.observableArrayList(
                "Any", "Front-End", "Back-End", "Full-Stack", "Data", "Other"
        ));
        statusBox.getSelectionModel().selectFirst();
        roleBox.getSelectionModel().selectFirst();

        // Filter listeners
        nameField.textProperty().addListener((obs, o, n) -> applyFilters());
        langField.textProperty().addListener((obs, o, n) -> applyFilters());
        dbField.textProperty().addListener((obs, o, n) -> applyFilters());
        statusBox.valueProperty().addListener((obs, o, n) -> applyFilters());
        roleBox.valueProperty().addListener((obs, o, n) -> applyFilters());

        // Delete button
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(e -> {
                    Student s = getTableView().getItems().get(getIndex());
                    StudentData.delete(s);      // persist/remove in your store
                    master.remove(s);           // update table
                });
                btn.setMinWidth(48);
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        applyFilters(); // initial pass
    }

    @FXML
    private void onClearFilters() {
        nameField.clear();
        langField.clear();
        dbField.clear();
        statusBox.getSelectionModel().selectFirst();
        roleBox.getSelectionModel().selectFirst();
    }

    private void applyFilters() {
        final String qName   = safe(nameField.getText());
        final String qLang   = safe(langField.getText());
        final String qDb     = safe(dbField.getText());
        final String qStatus = statusBox.getValue();
        final String qRole   = roleBox.getValue();

        filtered.setPredicate(s ->
                s != null
                        && s.nameContains(qName)
                        && s.statusEqualsIgnoreCase(isAny(qStatus) ? "" : qStatus)
                        && s.hasLanguageContaining(qLang)
                        && s.hasDatabaseContaining(qDb)
                        && s.roleEqualsIgnoreCase(isAny(qRole) ? "" : qRole)
        );
    }

    private static String safe(String v) { return v == null ? "" : v.trim(); }
    private static boolean isAny(String v) { return v == null || v.equalsIgnoreCase("Any"); }
}
