package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ProfileController {

    @FXML
    private TextField fullNameField;
    @FXML
    private ComboBox<String> academicStatusCombo;
    @FXML
    private RadioButton employedRadio;
    @FXML
    private RadioButton notEmployedRadio;
    @FXML
    private TextField jobDetailsField;
    @FXML
    private ListView<String> programmingLanguagesList;
    @FXML
    private ListView<String> databasesList;
    @FXML
    private ComboBox<String> preferredRoleCombo;
    @FXML
    private TextArea commentsArea;
    @FXML
    private CheckBox whitelistCheck;
    @FXML
    private CheckBox blacklistCheck;
    @FXML
    private Label statusLabel;

    private boolean isEditMode = false;
    private ProfileBean editingProfile = null;
    private boolean fromSearch = false;

    // Shared data model
    private DataLang progLangData;
    private DataProfile profileData;
    private Main main; // reference to the main app for scene switching

    /**
     * This method can be called from MainApp to give this controller
     * access to the main application for navigation.
     */
    public void setMain(Main mainApp) {
        this.main = mainApp;
    }

    public void setProfileData(DataProfile data) {
        this.profileData = data;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    /**
     * This will be called by your main application to inject the shared Data
     * instance.
     */
    public void setProgLangData(DataLang data) {
        this.progLangData = data;
        programmingLanguagesList.setItems(data.getLanguages());
    }

    public void setEditMode(boolean isEdit) {
        this.isEditMode = isEdit;
    }

    @FXML
    private void initialize() {
        // Fill combos and lists with dummy data for testing
        academicStatusCombo.getItems().addAll("Undergraduate", "Graduate", "PhD");
        preferredRoleCombo.getItems().addAll("Front-End", "Back-End", "Full-Stack", "Data", "Other");
        databasesList.getItems().addAll("MySQL", "Postgres", "MongoDB");

        programmingLanguagesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        databasesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Setup toggle for employment
        ToggleGroup employmentGroup = new ToggleGroup();
        employedRadio.setToggleGroup(employmentGroup);
        notEmployedRadio.setToggleGroup(employmentGroup);
        notEmployedRadio.setSelected(true); // default
        // Enable/disable job details based on employment selection
        employedRadio.selectedProperty().addListener((obs, oldVal, newVal) -> jobDetailsField.setDisable(!newVal));
        // Clear job details if not employed
        notEmployedRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal)
                jobDetailsField.clear();
        });

        whitelistCheck.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                blacklistCheck.setSelected(false);
            }
        });

        blacklistCheck.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                whitelistCheck.setSelected(false);
            }
        });

    }

    public void populateFieldsForEdit(ProfileBean profile) {
        this.editingProfile = profile;

        fullNameField.setText(profile.getFullName());
        academicStatusCombo.setValue(profile.getAcademicStatus());

        // Employment
        if (profile.isEmployed()) {
            employedRadio.setSelected(true);
            jobDetailsField.setDisable(false);
            jobDetailsField.setText(profile.getJobDetails());
        } else {
            notEmployedRadio.setSelected(true);
            jobDetailsField.setDisable(true);
            jobDetailsField.clear();
        }

        // Preferred role
        preferredRoleCombo.setValue(profile.getPreferredRole());

        // Programming languages
        programmingLanguagesList.getSelectionModel().clearSelection();
        for (String lang : profile.getProgrammingLanguages()) {
            programmingLanguagesList.getSelectionModel().select(lang);
        }

        // Databases
        databasesList.getSelectionModel().clearSelection();
        for (String db : profile.getDatabases()) {
            databasesList.getSelectionModel().select(db);
        }


        commentsArea.clear();

        // Whitelist / Blacklist
        whitelistCheck.setSelected(profile.isWhitelisted());
        blacklistCheck.setSelected(profile.isBlacklisted());

        statusLabel.setText("Editing profile ID: " + profile.getId());

    }

    private void editProfile() {
        if (editingProfile == null) {
            statusLabel.setText("No profile selected for editing.");
            return;
        }

        // Filler: just print values
        System.out.println("Editing profile ID: " + editingProfile.getId());
        System.out.println("Name: " + fullNameField.getText());
        System.out.println("Academic Status: " + academicStatusCombo.getValue());
        System.out.println("Employed: " + employedRadio.isSelected());
        System.out.println("Job Details: " + jobDetailsField.getText());
        System.out.println("Preferred Role: " + preferredRoleCombo.getValue());
        System.out.println("Languages: " + programmingLanguagesList.getSelectionModel().getSelectedItems());
        System.out.println("Databases: " + databasesList.getSelectionModel().getSelectedItems());
        System.out.println("Whitelist: " + whitelistCheck.isSelected());
        System.out.println("Blacklist: " + blacklistCheck.isSelected());

        // Here you would update the profile in your data model
        // For now, just simulate success
        // call data model update methods here

        profileData.editProfile(editingProfile.getId(),
                fullNameField.getText().trim(),
                academicStatusCombo.getValue(),
                employedRadio.isSelected(),
                jobDetailsField.getText().trim(),
                preferredRoleCombo.getValue(),
                new java.util.ArrayList<>(programmingLanguagesList.getSelectionModel().getSelectedItems()),
                new java.util.ArrayList<>(databasesList.getSelectionModel().getSelectedItems()),
                whitelistCheck.isSelected(),
                blacklistCheck.isSelected());

        String commentText = commentsArea.getText().trim();
        if (!commentText.isEmpty()) {
            profileData.addComments(commentText, editingProfile.getId());
        }

        statusLabel.setText("Profile ID " + editingProfile.getId() + " updated (simulated)");
    }

    @FXML
    private void saveProfile() {
        if(isEditMode)
        {
            editProfile();
            return;
        }


        // Filler: just print values
        System.out.println("Saving profile...");
        System.out.println("Name: " + fullNameField.getText());
        System.out.println("Academic Status: " + academicStatusCombo.getValue());
        System.out.println("Employed: " + employedRadio.isSelected());
        System.out.println("Job Details: " + jobDetailsField.getText());
        System.out.println("Preferred Role: " + preferredRoleCombo.getValue());
        System.out.println("Languages: " + programmingLanguagesList.getSelectionModel().getSelectedItems());
        System.out.println("Databases: " + databasesList.getSelectionModel().getSelectedItems());
        System.out.println("Whitelist: " + whitelistCheck.isSelected());
        System.out.println("Blacklist: " + blacklistCheck.isSelected());

        String fullName = fullNameField.getText().trim();
        String academicStatus = academicStatusCombo.getValue();
        boolean isEmployed = employedRadio.isSelected();
        String jobDetails = jobDetailsField.getText().trim();
        String preferredRole = preferredRoleCombo.getValue();

        var selectedLanguages = programmingLanguagesList.getSelectionModel().getSelectedItems();
        var selectedDatabases = databasesList.getSelectionModel().getSelectedItems();
        String commentText = commentsArea.getText().trim();

        boolean isWhitelisted = whitelistCheck.isSelected();
        boolean isBlacklisted = blacklistCheck.isSelected();

        java.util.List<String> langs = new java.util.ArrayList<>(selectedLanguages);
        java.util.List<String> dbs = new java.util.ArrayList<>(selectedDatabases);

        if (fullName.isEmpty()) {
            statusLabel.setText("Please enter full name.");
            return;
        }

        if (academicStatus == null) {
            statusLabel.setText("Please select academic status.");
            return;
        }
        if (employedRadio.isSelected() && jobDetails.isEmpty()) {
            statusLabel.setText("Please enter job details.");
            return;
        }

        if (preferredRole == null) {
            statusLabel.setText("Please select preferred role.");
            return;
        }

        ProfileBean createdProfile = profileData.createProfile(
                fullName,
                academicStatus,
                isEmployed,
                jobDetails,
                preferredRole,
                langs,
                dbs,
                isWhitelisted,
                isBlacklisted,
                commentText);

        statusLabel.setText("Profile saved (simulated)");

        clearForm();

    }


    @FXML
    private void back() throws Exception {
        if (isEditMode) {
            if (fromSearch) {
                main.showSearch();
            } else {
                main.showStudentList();
            }
            clearForm();
            statusLabel.setText("");
        }
        else
        {
            main.showHome();
            clearForm();        
            statusLabel.setText("");
        }
    }


    private void clearForm() {
        fullNameField.clear();
        academicStatusCombo.setValue(null);
        employedRadio.setSelected(false);
        notEmployedRadio.setSelected(true);
        jobDetailsField.clear();
        jobDetailsField.setDisable(true);
        programmingLanguagesList.getSelectionModel().clearSelection();
        databasesList.getSelectionModel().clearSelection();
        preferredRoleCombo.setValue(null);
        commentsArea.clear();
        whitelistCheck.setSelected(false);
        blacklistCheck.setSelected(false);
    }
}
