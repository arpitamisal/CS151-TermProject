package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class CommentController {

    @FXML private TextArea commentsArea;
    @FXML private TableView<CommentEntry> commentTable;
    @FXML private TableColumn<CommentEntry, String> commentColumn;
    @FXML private TableColumn<CommentEntry, String> dateColumn;
    @FXML private Label statusLabel;
    @FXML private Label profileNameLabel;

    private Main main;
    private DataProfile dataProfile;
    private ProfileBean currentProfile;
    private ObservableList<CommentEntry> commentList = FXCollections.observableArrayList();

    // --- Dependency Injection and Setup ---

    public void setMain(Main mainApp) {
        this.main = mainApp;
    }

    public void setProfileData(DataProfile data) {
        this.dataProfile = data;
    }

    /**
     * Called by Main.java to provide the profile whose comments we are viewing/adding.
     */
    public void setProfile(ProfileBean profile) {
        this.currentProfile = profile;
        if (profile != null) {
            // Display the profile name in the status bar for context
            statusLabel.setText("Viewing comments for: " + profile.getFullName());
            profileNameLabel.setText(profile.getFullName());
            loadComments(profile.getComments());
        } else {
            statusLabel.setText("Error: No profile selected.");
        }
    }

    @FXML
    private void initialize() {
        // 1. Setup Table Columns
        // Note: We use a wrapper class (CommentEntry) for ObservableList to fit 
        // the TableView and correctly display the comment and formatted date.
        
        // Bind the 'comment' property of CommentEntry to the comment column
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        
        // Bind the 'formattedDate' property of CommentEntry to the date column
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));

        // Set the items source for the table
        commentTable.setItems(commentList);
    }

    /**
     * Loads existing comments from the ProfileBean into the TableView.
     * @param comments The TreeMap of comments (Date as Integer, Comment as String).
     */
    private void loadComments(TreeMap<Integer, String> comments) {
        commentList.clear();
        for (Map.Entry<Integer, String> entry : comments.entrySet()) {
            commentList.add(new CommentEntry(entry.getKey(), entry.getValue()));
        }
        // Reverse the list to show newest comments at the top
        FXCollections.reverse(commentList); 
    }

    // --- Action Handlers ---

    @FXML
    private void addComment() {
        String newCommentText = commentsArea.getText().trim();

        if (currentProfile == null) {
            statusLabel.setText("Error: Cannot add comment, no profile is loaded.");
            return;
        }

        if (newCommentText.isEmpty()) {
            statusLabel.setText("Comment field cannot be empty.");
            return;
        }

        // 1. Get current timestamp as an integer (using seconds since epoch)
        int timestamp = (int) Instant.now().getEpochSecond();
        
        // 2. Add comment to the ProfileBean and the DataProfile model
        dataProfile.addComments(newCommentText, currentProfile.getId());

        // 3. Update the view immediately
        loadComments(currentProfile.getComments());
        commentsArea.clear();
        statusLabel.setText("Comment added successfully for " + currentProfile.getFullName() + ".");
    }

    @FXML
    private void showSearch() {
        try {
            // Assuming navigation is always back to the search view, 
            // as this feature is initiated from SearchController.
            main.showSearch(); 
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error navigating back: " + e.getMessage());
        }
    }

    /**
     * Helper class to structure comments for the JavaFX TableView.
     * TableView requires properties to bind correctly.
     */
    public static class CommentEntry {
        private final int date;
        private final String comment;
        private final String formattedDate;

        public CommentEntry(int date, String comment) {
            this.date = date;
            this.comment = comment;
            this.formattedDate = formatTimestamp(date);
        }
        
        // Helper method to convert Unix timestamp (int) to readable date string
        private String formatTimestamp(int timestamp) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp), 
                ZoneId.systemDefault()
            );
            // Use a clear, common date/time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dateTime.format(formatter);
        }

        // Getters for PropertyValueFactory
        public String getComment() {
            return comment;
        }

        public int getDate() {
            return date;
        }

        public String getFormattedDate() {
            return formattedDate;
        }
    }
}