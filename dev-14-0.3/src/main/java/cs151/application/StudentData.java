package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class StudentData {
    private static final ObservableList<Student> STORE = FXCollections.observableArrayList();

    static {
        // 5 profiles
        STORE.add(make("Alice Kim",  Student.AcademicStatus.JUNIOR,    false, "",
                List.of("Java"),           List.of("SQLite"),      Student.ProfessionalRole.FRONT_END));

        STORE.add(make("Bob Singh",  Student.AcademicStatus.SENIOR,    true,  "Intern @ ABC",
                List.of("Python"),         List.of("MySQL"),       Student.ProfessionalRole.DATA));

        STORE.add(make("Cara Lopez", Student.AcademicStatus.GRADUATE,  false, "",
                List.of("C++"),            List.of("PostgreSQL"),  Student.ProfessionalRole.BACK_END));

        STORE.add(make("Dev Patel",  Student.AcademicStatus.SOPHOMORE, true,  "PT QA @ Lab",
                List.of("Java","Python"),  List.of("SQLite"),      Student.ProfessionalRole.FULL_STACK));

        STORE.add(make("Eva Chen",   Student.AcademicStatus.SENIOR,    false, "",
                List.of("Python","C++"),   List.of("MongoDB"),     Student.ProfessionalRole.OTHER));
    }

    private static Student make(String name, Student.AcademicStatus st, boolean employed, String job,
                                List<String> langs, List<String> dbs, Student.ProfessionalRole role) {
        Student s = new Student(name);
        s.setAcademicStatus(st);
        s.setEmployed(employed);
        s.setJobDetails(job);
        s.setProgrammingLanguages(langs);
        s.setDatabases(dbs);
        s.setPreferredRole(role);
        return s;
    }

    public static ObservableList<Student> loadAll() {
        // return a fresh list to avoid accidental shared mutations
        return FXCollections.observableArrayList(STORE);
    }

    public static void delete(Student s) {
        STORE.remove(s);
    }
}
