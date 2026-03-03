package cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {

    private Stage primaryStage;  // keep a reference so we can change scenes later
    private DataLang progLangData;
    private DataProfile profileData;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        progLangData = new DataLang();
        profileData = new DataProfile();
        this.primaryStage = primaryStage;
        showHome();  // start with the home screen

        primaryStage.setWidth(700);
        primaryStage.setHeight(620);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Students' Knowledgebase");
        primaryStage.show();
    }

    public void showHome() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
        Parent root = loader.load();
        HomeController controller = loader.getController();

        controller.setMain(this);

        primaryStage.setScene(new Scene(root));
        
    }

    public void showDefine() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("define-page.fxml"));
        Parent root = loader.load();
        DefineController controller = loader.getController();

        controller.setMain(this);
        controller.setData(progLangData);

        primaryStage.setScene(new Scene(root));
    }

    public void showProfile() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new-profile-page.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();

        controller.setMain(this);
        controller.setProgLangData(progLangData);
        controller.setProfileData(profileData);
        controller.setEditMode(false);
        controller.setFromSearch(false);

        primaryStage.setScene(new Scene(root));
    }

    public void showProfileWithData(ProfileBean student, boolean fromSearch) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new-profile-page.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();

        controller.setMain(this);
        controller.setProgLangData(progLangData);
        controller.setProfileData(profileData);

        controller.populateFieldsForEdit(student);
        controller.setEditMode(true);
        controller.setFromSearch(fromSearch);

        primaryStage.setScene(new Scene(root));
    }

    public void showStudentList() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("student-list-page.fxml"));
        Parent root = loader.load();
        ProfileListController controller = loader.getController();

        controller.setMain(this);
        controller.setDataProfile(profileData);

        primaryStage.setScene(new Scene(root));
    }

    public void showSearch() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-page.fxml"));
        Parent root = loader.load();
        SearchController controller = loader.getController();

        controller.setMain(this);
        controller.setProfileData(profileData);

        primaryStage.setScene(new Scene(root));
    }




}
