package cs151.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataProfile {
    private static final String profile_file = "./src/main/java/cs151/application/data/profiles.txt";
    private static final String comment_file = "./src/main/java/cs151/application/data/comments.txt";
    private final ObservableList<ProfileBean> profiles = FXCollections.observableArrayList();
    private int nextID = 1;

    public DataProfile() {
        setupFile(profile_file);
        setupFile(comment_file);
        loadProfile();
        loadComments();
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
            System.err.println("Failed to ensure " + filename + ".txt: " + e.getMessage());
        }
    }

    public void loadProfile() {
        profiles.clear(); // clear existing list first
        Path filePath = Paths.get(profile_file);

        if (!Files.exists(filePath)) {
            System.out.println("⚠️ Profiles file not found: " + filePath);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue; // skip empty lines

                String[] parts = line.split("\\|", -1); // -1 keeps empty fields
                if (parts.length < 10) {
                    System.err.println("⚠️ Skipping malformed line: " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0].trim());
                String fullName = parts[1].trim();
                String academicStatus = parts[2].trim();
                boolean isEmployed = Boolean.parseBoolean(parts[3].trim());
                String jobDetails = parts[4].trim();
                String preferredRole = parts[5].trim();

                List<String> programmingLanguages = splitList(parts[6]);
                List<String> databases = splitList(parts[7]);

                boolean isWhitelisted = Boolean.parseBoolean(parts[8].trim());
                boolean isBlacklisted = Boolean.parseBoolean(parts[9].trim());

                ProfileBean bean = new ProfileBean(
                        id,
                        fullName,
                        academicStatus,
                        isEmployed,
                        jobDetails,
                        preferredRole,
                        programmingLanguages,
                        databases,
                        isWhitelisted,
                        isBlacklisted);

                profiles.add(bean);
                if (id >= nextID) {
                    nextID = id + 1;
                }
            }

            System.out.println("✅ Loaded " + profiles.size() + " profiles from file.");

        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Parsing error: " + e.getMessage());
        }

    }

    public void loadComments() {
        Path filePath = Paths.get(comment_file);

        if (!Files.exists(filePath)) {
            System.out.println("⚠️ Comments file not found: " + filePath);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue; // skip empty lines

                String[] parts = line.split("\\|", -1); // -1 keeps empty fields
                if (parts.length < 3) {
                    System.err.println("⚠️ Skipping malformed line: " + line);
                    continue;
                }

                counter++;



                int id = Integer.parseInt(parts[0].trim());
                int date = Integer.parseInt(parts[1].trim());
                String comment = parts[2].trim();

                ProfileBean bean = findById(id);
                if (bean != null) {
                    bean.addComments(date, comment);
                }
                bean.addComments(date, comment);
                
            }

            System.out.println("✅ Loaded " + counter + " Comments from file.");

        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Parsing error: " + e.getMessage());
        }


    }

    private static List<String> splitList(String data) {
        if (data == null || data.trim().isEmpty())
            return new ArrayList<>();
        String[] items = data.split(",");
        List<String> list = new ArrayList<>();
        for (String s : items) {
            list.add(s.trim());
        }
        return list;
    }

    public void addComments(String comment, int profileId) {
        ProfileBean profile = findById(profileId);

        if (profile != null) {
            int timestamp = (int) Instant.now().getEpochSecond();
            profile.addComments(timestamp, comment);
            saveCommentsToFile();
        }
        

    }

    public ProfileBean createProfile(
            String fullName,
            String academicStatus,
            boolean isEmployed,
            String jobDetails,
            String preferredRole,
            List<String> programmingLanguages,
            List<String> databases,
            boolean isWhitelisted,
            boolean isBlacklisted,
            String comment) {
        // Assign unique ID
        int id = nextID++;

        // Create the ProfileBean
        ProfileBean profile = new ProfileBean(
                id,
                fullName,
                academicStatus,
                isEmployed,
                jobDetails,
                preferredRole,
                new ArrayList<>(programmingLanguages),
                new ArrayList<>(databases),
                isWhitelisted,
                isBlacklisted);

        // Add to the internal list
        profiles.add(profile);
        if (comment != null && !comment.trim().isEmpty()) {
            addComments(comment, id);
        }

        saveProfilesToFile();

        return profile;
    }

    public void editProfile(int id,
            String fullName,
            String academicStatus,
            boolean isEmployed,
            String jobDetails,
            String preferredRole,
            List<String> programmingLanguages,
            List<String> databases,
            boolean isWhitelisted,
            boolean isBlacklisted) {


        // edit profile beann
        ProfileBean currBean = findById(id);
        currBean.setFullName(fullName);
        currBean.setAcademicStatus(academicStatus);
        currBean.setEmployed(isEmployed);
        currBean.setJobDetails(jobDetails);
        currBean.setPreferredRole(preferredRole);
        currBean.getProgrammingLanguages().clear();
        currBean.getProgrammingLanguages().addAll(programmingLanguages);
        currBean.getDatabases().clear();
        currBean.getDatabases().addAll(databases);
        currBean.setWhitelisted(isWhitelisted);
        currBean.setBlacklisted(isBlacklisted);

        saveProfilesToFile();

    }

    private void saveCommentsToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(comment_file))) {

            for (ProfileBean profile : profiles) {
                for (var entry : profile.getComments().entrySet()) {
                    String line = profile.getId() + "|" +
                            entry.getKey() + "|" +
                            entry.getValue().trim();
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProfilesToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(profile_file))) {

            for (ProfileBean profile : profiles) {
                String line = profile.getId() + "|" +
                        profile.getFullName().trim() + "|" +
                        profile.getAcademicStatus().trim() + "|" +
                        profile.isEmployed() + "|" +
                        profile.getJobDetails().trim() + "|" +
                        profile.getPreferredRole().trim() + "|" +
                        String.join(",", profile.getProgrammingLanguages()) + "|" +
                        String.join(",", profile.getDatabases()) + "|" +
                        profile.isWhitelisted() + "|" +
                        profile.isBlacklisted();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProfileBean findById(int id) {
        return profiles.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public ObservableList<ProfileBean> getAllProfiles() {
        return profiles;
    }

    public boolean deleteProfile(int profileId) {
        ProfileBean profile = findById(profileId);
        if (profile == null) {
            return false;
        }
        profiles.remove(profile);

        
        saveProfilesToFile();
        saveCommentsToFile();

        return true;
    }

}