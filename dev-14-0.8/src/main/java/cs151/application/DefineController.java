package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

public class DefineController {

    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private TableView<String> progLangTable;
    @FXML private TableColumn<String, String> nameCol;

    // Shared data model
    private DataLang data;
    private Main main; // reference to the main app for scene switching

    /**
     * This method can be called from MainApp to give this controller
     * access to the main application for navigation.
     */
    public void setMain(Main mainApp) {
        this.main = mainApp;
    }

    /**
     * This will be called by your main application to inject the shared Data instance.
     */
    public void setData(DataLang data) {
        this.data = data;
        if (data != null) {
            progLangTable.setItems(data.getLanguages());
        }
    }
    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()));
    }

    

    @FXML
    private void saveDefine() {
        if (data == null) {
            System.err.println("Data not initialized — call setData() before using this controller.");
            return;
        }

        String name = nameField == null ? "" : nameField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Name is required.");
            nameField.requestFocus();
            return;
        }

        boolean exists = data.getLanguages().stream()
                .anyMatch(s -> s.equalsIgnoreCase(name));

        if (exists) {
            statusLabel.setText("Already exists: " + name);
            return;
        }

        data.addLanguage(name);
        statusLabel.setText("Saved: " + name);
        nameField.clear();
        nameField.requestFocus();
    }

    @FXML
    private void showHome() {
        System.out.println("Navigating to Home view...");
        try {
            main.showHome();
        } catch (Exception e) {
            System.out.println("⚠️ Error navigating to Home view: " + e.getMessage());
        }
    }
}
