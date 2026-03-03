package cs151.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DataLang {
    private static final String LANG_FILE = "./src/main/java/cs151/application/data/ProgLang.txt";
    private final ObservableList<String> languages = FXCollections.observableArrayList();

    public DataLang() {
        setupFile(LANG_FILE);
        loadLanguages();
    }

    public void setupFile(String filename) {
        try {
            Path projectRoot = Paths.get(".");
            Path targetFile = projectRoot.resolve(filename);
            if (Files.notExists(targetFile)) {
                Files.createFile(targetFile);

                // String header = "Testing \n";
                // Files.write(targetFile, header.getBytes(), StandardOpenOption.APPEND);
                System.out.println("Created file: " + targetFile.toString());
            } else {
                System.out.println("File already exists: " + targetFile.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to ensure " + LANG_FILE + ": " + e.getMessage());
        }
    }

    public void loadLanguages() {
        try {
            Path projectRoot = Paths.get(".");
            Path targetFile = projectRoot.resolve(LANG_FILE);
            if (Files.exists(targetFile)) {
                ObservableList<String> loadedLanguages = FXCollections.observableArrayList(
                        Files.readAllLines(targetFile));
                languages.setAll(loadedLanguages);
                System.out.println("Loaded languages from " + LANG_FILE);
            } else {
                System.err.println(LANG_FILE + " does not exist for loading languages.");
            }
        } catch (IOException e) {
            System.err.println("Error loading languages: " + e.getMessage());
        }
    }

    public ObservableList<String> getLanguages() {

        System.out.println("Getting languages: " + languages);
        return languages;
    }

    public boolean addLanguage(String language) {
        if (!languages.contains(language)) {
            languages.add(language);
            System.out.println("Added language: " + language);
            saveLanguagesToFile(language);
            return true;
        }
        return false;
    }

    private void saveLanguagesToFile(String language) {
        try {
            Path projectRoot = Paths.get(".");
            Path targetFile = projectRoot.resolve(LANG_FILE);
            Files.write(targetFile, languages);
            System.out.println("Saved languages to " + LANG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving languages: " + e.getMessage());
        }
    }

    public boolean removeLanguage(String language) {
        return languages.remove(language);
    }
}
