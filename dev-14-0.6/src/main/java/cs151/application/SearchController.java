package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchFilter;
    @FXML private TableView<ProfileBean> studentTable;
    @FXML private TableColumn<ProfileBean, String> nameColumn;
    @FXML private TableColumn<ProfileBean, String> statusColumn;
    @FXML private TableColumn<ProfileBean, String> roleColumn;
    @FXML private Label statusLabel;
    ObservableList<ProfileBean> results = FXCollections.observableArrayList();

    private Main main; // Reference to main app for navigation
    private DataProfile dataProfile; // Shared data source

    public void setMain(Main mainApp) {
        this.main = mainApp;
    }

    public void setProfileData(DataProfile data) {
        this.dataProfile = data;
        results.setAll(dataProfile.getAllProfiles());
    }

    /**
     * Called automatically after FXML is loaded.
     */
    @FXML
    private void initialize() {
        // Set up columns to bind to ProfileBean properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));

        // Example search filters
        searchFilter.setItems(FXCollections.observableArrayList("Name", "Academic Status", "Programming Language", "Database Skill", "Preferred Role"));
        searchFilter.setValue("Name");

        studentTable.setItems(results);
    }

    @FXML
    private void performSearch() {
        String query = searchField.getText().toLowerCase();
        String filter = searchFilter.getValue();

        ObservableList<ProfileBean> results = FXCollections.observableArrayList();

        for (ProfileBean profile : dataProfile.getAllProfiles()) {
            boolean matches = false;
            switch (filter) {
                case "Name":
                    matches = profile.getFullName().toLowerCase().contains(query);
                    break;
                case "Academic Status":
                    matches = profile.getAcademicStatus().toLowerCase().contains(query);
                    break;
                case "Preferred Role":
                    matches = profile.getPreferredRole().toLowerCase().contains(query);
                    break;
                case "Programming Language":
                    matches = containsIgnoreCase(profile.getProgrammingLanguages(), query);
                    break;
                case "Database Skill":
                    matches = containsIgnoreCase(profile.getDatabases(), query);
                    break;
            }
            if (matches) {
                results.add(profile);
            }
        }
        this.results = results;
        studentTable.setItems(results);

        statusLabel.setText("Found " + results.size() + " matching profiles.");
    }

    private boolean containsIgnoreCase(List<String> list, String value) {
        if (list == null || value == null) {
            return false; // Null safety
        }

        for (String item : list) {
            if (item != null && item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }




    @FXML
    private void deleteStudent() {
        ProfileBean selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("No profile selected for deletion.");
            return;
        }

        boolean removed = dataProfile.deleteProfile(selected.getId());
        if (removed) {
            results.remove(selected);
            statusLabel.setText("Deleted profile: " + selected.getFullName());
        } else {
            statusLabel.setText("Failed to delete profile.");
        }
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

    @FXML
    private void editStudent() {
        ProfileBean selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("No profile selected for editing.");
            return;
        }

        try {
            main.showProfileWithData(selected, true); 
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error editing profile: " + e.getMessage());
        }
    }

}
