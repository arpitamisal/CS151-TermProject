package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private Label statusLabel; // optional (if you later want to show messages)

    private Main main; // reference to the main app for scene switching

    /**
     * This method can be called from MainApp to give this controller
     * access to the main application for navigation.
     */
    public void setMain(Main mainApp) {
        this.main = mainApp;
    }


    @FXML
    private void initialize() {
        // Runs automatically after FXML loads
        System.out.println("HomeController initialized.");
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
    private void showDefine() {
        System.out.println("Navigating to Define Programming Language view...");
        try {
            main.showDefine();
        } catch (Exception e) {
            System.out.println("⚠️ Error navigating to Define view: " + e.getMessage());
        }
    }

    @FXML
    private void showProfile() {
        System.out.println("Navigating to Student Profile view...");
        try {
            main.showProfile();
        } catch (Exception e) {
            System.out.println("⚠️ Error navigating to Profile view: " + e.getMessage());
        }
    }

    @FXML
    private void showStudentList() {
        System.out.println("Navigating to Student Profile List view...");
        try {
            main.showStudentList();
        } catch (Exception e) {
            System.out.println("⚠️ Error navigating to Student List view: " + e.getMessage());
        }
    }

    @FXML
    private void showSearch() {
        System.out.println("Navigating to Search view...");
        try {
            main.showSearch();
        } catch (Exception e) {
            System.out.println("⚠️ Error navigating to Search view: " + e.getMessage());
        }
    }
}
