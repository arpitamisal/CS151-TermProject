package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfileListController {

    @FXML private TableView<ProfileBean> studentTable;
    @FXML private TableColumn<ProfileBean, String> nameColumn;
    @FXML private TableColumn<ProfileBean, String> statusColumn;
    @FXML private TableColumn<ProfileBean, String> roleColumn;
    @FXML private Label statusLabel;

    private Main main;
    private DataProfile dataProfile; // model layer
    private ObservableList<ProfileBean> profileList = FXCollections.observableArrayList();

    // Called automatically after FXML loads
    @FXML
    private void initialize() {
        // Setup columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));


        // Attach data
        studentTable.setItems(profileList);
    }

    // Called by Main after loading
    public void setMain(Main mainApp) {
        this.main = mainApp;
    }

    public void setDataProfile(DataProfile dataProfile) {
        this.dataProfile = dataProfile;
        // Populate from model
        profileList.setAll(dataProfile.getAllProfiles());
    }

    // --- BUTTON HANDLERS ---

    @FXML
    private void editStudent() {
        ProfileBean selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("No profile selected for editing.");
            return;
        }

        try {
            main.showProfileWithData(selected, false); 
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error editing profile: " + e.getMessage());
        }
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
            profileList.remove(selected);
            statusLabel.setText("Deleted profile: " + selected.getFullName());
        } else {
            statusLabel.setText("Failed to delete profile.");
        }
    }

    @FXML
    private void showHome() {
        try {
            main.showHome();
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error returning home: " + e.getMessage());
        }
    }
}
