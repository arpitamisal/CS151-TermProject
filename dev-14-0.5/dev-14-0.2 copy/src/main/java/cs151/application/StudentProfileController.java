package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StudentProfileController {

    // Views
    @FXML private VBox homeBox;
    @FXML private VBox profileBox;
    @FXML private VBox listBox;
    @FXML private VBox defineBox;

    // Profile form fields
    @FXML private TextField fullNameField;
    @FXML private ComboBox<Student.AcademicStatus> academicStatusCombo;
    @FXML private RadioButton employedRadio;
    @FXML private RadioButton notEmployedRadio;
    @FXML private TextField jobDetailsField;
    @FXML private ListView<String> programmingLanguagesList;
    @FXML private ListView<String> databasesList;
    @FXML private ComboBox<Student.ProfessionalRole> preferredRoleCombo;
    @FXML private TextArea commentsArea;
    @FXML private CheckBox whitelistCheck;
    @FXML private CheckBox blacklistCheck;
    @FXML private Label statusLabel;

    // List view
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> statusColumn;
    @FXML private TableColumn<Student, String> roleColumn;
    @FXML private TableColumn<Student, String> createdColumn;

    // Define programming language form
    @FXML private TextField nameField;
    @FXML private TableView<String> table;
    @FXML private TableColumn<String, String> nameCol;

    // Data management
    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<String> availableLanguages = FXCollections.observableArrayList();
    private final ObservableList<String> availableDatabases = FXCollections.observableArrayList();
    private SortedList<Student> sortedStudents;
    private SortedList<String> sortedLanguages;
    
    // Storage
    private final Path studentsPath = Paths.get("./students.txt");
    private final Path languagesPath = Paths.get("./ProgLang.txt");
    
    // Current student being edited
    private Student currentStudent;

    @FXML
    private void initialize() {
        setupComboBoxes();
        setupRadioButtons();
        setupCheckBoxes();
        setupTable();
        setupLists();
        loadData();
    }

    private void setupComboBoxes() {
        // Academic Status
        academicStatusCombo.setItems(FXCollections.observableArrayList(Student.AcademicStatus.values()));
        academicStatusCombo.setConverter(new javafx.util.StringConverter<Student.AcademicStatus>() {
            @Override
            public String toString(Student.AcademicStatus status) {
                return status != null ? status.getDisplayName() : "";
            }

            @Override
            public Student.AcademicStatus fromString(String string) {
                return Arrays.stream(Student.AcademicStatus.values())
                        .filter(status -> status.getDisplayName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Professional Role
        preferredRoleCombo.setItems(FXCollections.observableArrayList(Student.ProfessionalRole.values()));
        preferredRoleCombo.setConverter(new javafx.util.StringConverter<Student.ProfessionalRole>() {
            @Override
            public String toString(Student.ProfessionalRole role) {
                return role != null ? role.getDisplayName() : "";
            }

            @Override
            public Student.ProfessionalRole fromString(String string) {
                return Arrays.stream(Student.ProfessionalRole.values())
                        .filter(role -> role.getDisplayName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void setupRadioButtons() {
        ToggleGroup employmentGroup = new ToggleGroup();
        employedRadio.setToggleGroup(employmentGroup);
        notEmployedRadio.setToggleGroup(employmentGroup);
        notEmployedRadio.setSelected(true);
        
        // Enable/disable job details based on employment status
        employedRadio.selectedProperty().addListener((_, _, newVal) -> {
            jobDetailsField.setDisable(!newVal);
            if (!newVal) {
                jobDetailsField.clear();
            }
        });
    }

    private void setupCheckBoxes() {
        // Mutually exclusive whitelist/blacklist
        whitelistCheck.selectedProperty().addListener((_, _, newVal) -> {
            if (newVal && blacklistCheck.isSelected()) {
                blacklistCheck.setSelected(false);
            }
        });
        
        blacklistCheck.selectedProperty().addListener((_, _, newVal) -> {
            if (newVal && whitelistCheck.isSelected()) {
                whitelistCheck.setSelected(false);
            }
        });
    }

    private void setupTable() {
        // Student table columns
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        nameColumn.setComparator(String::compareToIgnoreCase);

        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAcademicStatus() != null ? 
                data.getValue().getAcademicStatus().getDisplayName() : ""));
        statusColumn.setComparator(String::compareToIgnoreCase);

        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPreferredRole() != null ? 
                data.getValue().getPreferredRole().getDisplayName() : ""));
        roleColumn.setComparator(String::compareToIgnoreCase);

        createdColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
        createdColumn.setComparator(String::compareToIgnoreCase);

        // Wrap student data with SortedList
        sortedStudents = new SortedList<>(students);
        studentTable.setItems(sortedStudents);
        sortedStudents.comparatorProperty().bind(studentTable.comparatorProperty());

        // Default sort by name
        studentTable.getSortOrder().setAll((TableColumn<Student, ?>) nameColumn);
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);

        // Programming language table setup
        if (nameCol != null) {
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            nameCol.setComparator(String::compareToIgnoreCase);

            // Wrap language data with SortedList
            sortedLanguages = new SortedList<>(availableLanguages);
            table.setItems(sortedLanguages);
            sortedLanguages.comparatorProperty().bind(table.comparatorProperty());

            // Default sort A-Z on first show
            table.getSortOrder().setAll((TableColumn<String, ?>) nameCol);
            nameCol.setSortType(TableColumn.SortType.ASCENDING);
        }
    }

    private void setupLists() {
        // Programming languages list (multi-select)
        programmingLanguagesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Databases list (multi-select)
        databasesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // Navigation methods
    @FXML
    protected void showProfile() {
        showOnly(profileBox);
        clearForm();
        currentStudent = null;
        if (statusLabel != null) statusLabel.setText("Create a new student profile");
    }

    @FXML
    protected void showList() {
        showOnly(listBox);
        loadStudents();
    }

    @FXML
    protected void showDefine() {
        showOnly(defineBox);
        if (statusLabel != null) statusLabel.setText("Examples: Java, Python");
        if (nameField != null) { nameField.clear(); nameField.requestFocus(); }
    }

    @FXML
    protected void showHome() {
        showOnly(homeBox);
    }

    private void showOnly(VBox target) {
        for (VBox box : List.of(homeBox, profileBox, listBox, defineBox)) {
            if (box == null) continue;
            boolean show = box == target;
            box.setVisible(show);
            box.setManaged(show);
        }
    }

    // Form actions
    @FXML
    protected void saveProfile() {
        if (!validateForm()) {
            return;
        }

        Student student = currentStudent != null ? currentStudent : new Student();
        populateStudentFromForm(student);

        // Check for duplicates (case-insensitive)
        if (currentStudent == null) {
            boolean exists = students.stream()
                    .anyMatch(s -> s.getTrimmedFullName().equalsIgnoreCase(student.getTrimmedFullName()));
            if (exists) {
                statusLabel.setText("Student with this name already exists!");
                return;
            }
            students.add(student);
        }

        persistStudents();
        statusLabel.setText("Student profile saved successfully!");
        clearForm();
        currentStudent = null;
    }

    @FXML
    protected void editStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a student to edit");
            return;
        }

        currentStudent = selected;
        populateFormFromStudent(selected);
        showOnly(profileBox);
        statusLabel.setText("Editing: " + selected.getFullName());
    }

    @FXML
    protected void deleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a student to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Student Profile");
        alert.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            students.remove(selected);
            persistStudents();
            statusLabel.setText("Student profile deleted");
        }
    }

    @FXML
    protected void addComment() {
        String comment = commentsArea.getText().trim();
        if (!comment.isEmpty()) {
            commentsArea.appendText("\n" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")) + ": " + comment);
            commentsArea.clear();
        }
    }

    @FXML
    protected void saveDefine() {
        String name = nameField == null ? "" : nameField.getText().trim();
        if (name.isEmpty()) {
            if (statusLabel != null) statusLabel.setText("Name is required.");
            if (nameField != null) nameField.requestFocus();
            return;
        }

        // Avoid duplicates (case-insensitive)
        boolean exists = availableLanguages.stream()
                .anyMatch(s -> s.equalsIgnoreCase(name));
        if (exists) {
            if (statusLabel != null) statusLabel.setText("Already exists: " + name);
            return;
        }

        availableLanguages.add(name);
        persistLanguages();
        if (statusLabel != null) statusLabel.setText("Saved: " + name);
        if (nameField != null) { nameField.clear(); nameField.requestFocus(); }
    }

    private void populateStudentFromForm(Student student) {
        student.setFullName(fullNameField.getText().trim());
        student.setAcademicStatus(academicStatusCombo.getValue());
        student.setEmployed(employedRadio.isSelected());
        student.setJobDetails(jobDetailsField.getText().trim());
        
        // Programming languages
        List<String> selectedLanguages = programmingLanguagesList.getSelectionModel().getSelectedItems();
        student.setProgrammingLanguages(new ArrayList<>(selectedLanguages));
        
        // Databases
        List<String> selectedDatabases = databasesList.getSelectionModel().getSelectedItems();
        student.setDatabases(new ArrayList<>(selectedDatabases));
        
        student.setPreferredRole(preferredRoleCombo.getValue());
        
        // Comments
        String commentsText = commentsArea.getText().trim();
        if (!commentsText.isEmpty()) {
            String[] commentLines = commentsText.split("\n");
            for (String line : commentLines) {
                if (!line.trim().isEmpty()) {
                    student.addComment(line.trim());
                }
            }
        }
        
        student.setWhitelist(whitelistCheck.isSelected());
        student.setBlacklist(blacklistCheck.isSelected());
    }

    private void populateFormFromStudent(Student student) {
        fullNameField.setText(student.getFullName());
        academicStatusCombo.setValue(student.getAcademicStatus());
        employedRadio.setSelected(student.isEmployed());
        notEmployedRadio.setSelected(!student.isEmployed());
        jobDetailsField.setText(student.getJobDetails());
        
        // Programming languages
        programmingLanguagesList.getSelectionModel().clearSelection();
        for (String lang : student.getProgrammingLanguages()) {
            int index = availableLanguages.indexOf(lang);
            if (index >= 0) {
                programmingLanguagesList.getSelectionModel().select(index);
            }
        }
        
        // Databases
        databasesList.getSelectionModel().clearSelection();
        for (String db : student.getDatabases()) {
            int index = availableDatabases.indexOf(db);
            if (index >= 0) {
                databasesList.getSelectionModel().select(index);
            }
        }
        
        preferredRoleCombo.setValue(student.getPreferredRole());
        
        // Comments
        commentsArea.setText(String.join("\n", student.getComments()));
        
        whitelistCheck.setSelected(student.isWhitelist());
        blacklistCheck.setSelected(student.isBlacklist());
    }

    private void clearForm() {
        fullNameField.clear();
        academicStatusCombo.setValue(null);
        notEmployedRadio.setSelected(true);
        jobDetailsField.clear();
        programmingLanguagesList.getSelectionModel().clearSelection();
        databasesList.getSelectionModel().clearSelection();
        preferredRoleCombo.setValue(null);
        commentsArea.clear();
        whitelistCheck.setSelected(false);
        blacklistCheck.setSelected(false);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (fullNameField.getText().trim().isEmpty()) {
            errors.append("Full name is required.\n");
        }

        if (academicStatusCombo.getValue() == null) {
            errors.append("Academic status is required.\n");
        }

        if (employedRadio.isSelected() && jobDetailsField.getText().trim().isEmpty()) {
            errors.append("Job details are required when employed.\n");
        }

        if (programmingLanguagesList.getSelectionModel().getSelectedItems().isEmpty()) {
            errors.append("At least one programming language must be selected.\n");
        }

        if (databasesList.getSelectionModel().getSelectedItems().isEmpty()) {
            errors.append("At least one database must be selected.\n");
        }

        if (preferredRoleCombo.getValue() == null) {
            errors.append("Preferred professional role is required.\n");
        }

        if (errors.length() > 0) {
            statusLabel.setText(errors.toString());
            return false;
        }

        return true;
    }

    // Data persistence
    private void loadData() {
        loadLanguages();
        loadDatabases();
        loadStudents();
    }

    private void loadLanguages() {
        availableLanguages.clear();
        if (Files.exists(languagesPath)) {
            try {
                List<String> lines = Files.readAllLines(languagesPath, StandardCharsets.UTF_8).stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());
                availableLanguages.addAll(lines);
            } catch (IOException e) {
                System.err.println("Error loading languages: " + e.getMessage());
            }
        }
        programmingLanguagesList.setItems(availableLanguages);
    }

    private void loadDatabases() {
        availableDatabases.clear();
        availableDatabases.addAll(Arrays.asList("MySQL", "Postgres", "MongoDB", "SQLite", "Oracle", "SQL Server"));
        databasesList.setItems(availableDatabases);
    }

    private void loadStudents() {
        students.clear();
        if (Files.exists(studentsPath)) {
            try {
                List<String> lines = Files.readAllLines(studentsPath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Student student = Student.fromString(line);
                        if (student != null) {
                            students.add(student);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading students: " + e.getMessage());
            }
        }
    }

    private void persistStudents() {
        try {
            List<String> lines = students.stream()
                    .map(Student::toSerializedString)
                    .collect(Collectors.toList());
            Files.write(studentsPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            statusLabel.setText("Error saving student data.");
        }
    }

    private void persistLanguages() {
        try {
            List<String> sorted = availableLanguages.stream()
                    .distinct()
                    .sorted(String::compareToIgnoreCase)
                    .collect(Collectors.toList());
            Files.write(languagesPath, sorted, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            if (statusLabel != null) statusLabel.setText("Error saving data.");
        }
    }
}
