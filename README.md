# CS151 Term Project – UNIBASE (JavaFX)

UNIBASE is a JavaFX desktop application that allows users to define and manage programming languages through a simple graphical interface. The application supports persistent local storage and dynamic table updates.

## Tech Stack
- Java (JDK 21 – Zulu 21)
- JavaFX 17
- IntelliJ IDEA
- FXML (MVC Architecture)

## Features
- Landing page with navigation
- Define and add new programming languages
- Persistent local file storage (~/.cs151_languages.txt)
- TableView display with automatic A–Z sorting
- Modular structure using Main, MainController, and FXML views

## How to Run
- Run `cs151.application.Main` in IntelliJ
- From Home, select **Define Programming Language**
- Enter a name and click **Save**
- Entries are stored locally and displayed in a sorted table

Built and tested on macOS (Apple Silicon).

