package cs151.application;

import java.io.IOException;
import java.nio.file.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 460);
        stage.setTitle("Students' Knowledgebase");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    // Sets up the application
    // Creates ProgLang.txt to log saved programming languages
    public void init() throws IOException {

        try {
            Path projectRoot = Paths.get(".");
            Path targetFile = projectRoot.resolve("ProgLang.txt");
            if (Files.notExists(targetFile)) {
                Files.createFile(targetFile);
                // String header = "Testing \n";
                // Files.write(targetFile, header.getBytes(), StandardOpenOption.APPEND);
                System.out.println("Created file: " + targetFile.toString());
            } else {
                System.out.println("File already exists: " + targetFile.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to ensure ProgLang.txt: " + e.getMessage());
        }
    }

    public static void main(String[] args) { launch(); }
}
