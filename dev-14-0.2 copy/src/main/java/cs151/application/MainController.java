package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML private VBox homeBox;

    // Define page controls
    @FXML private VBox defineBox;
    @FXML private TextField nameField;
    @FXML private Label statusLabel;

    @FXML
    protected void showDefine() {
        homeBox.setVisible(false);
        homeBox.setManaged(false);
        defineBox.setVisible(true);
        defineBox.setManaged(true);
        if (statusLabel != null) statusLabel.setText("Examples: Java, Python");
        if (nameField != null) { nameField.clear(); nameField.requestFocus(); }
    }

    @FXML
    protected void showHome() {
        defineBox.setVisible(false);
        defineBox.setManaged(false);
        homeBox.setVisible(true);
        homeBox.setManaged(true);
    }

    @FXML
    protected void saveDefine() {
        String name = nameField == null ? "" : nameField.getText().trim();
        if (name.isEmpty()) {
            if (statusLabel != null) statusLabel.setText("Name is required.");
            if (nameField != null) nameField.requestFocus();
            return;
        }
        if (statusLabel != null) statusLabel.setText("Saved: " + name);
    }
}