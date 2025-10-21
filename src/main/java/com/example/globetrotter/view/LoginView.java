package com.example.globetrotter.view;

import com.example.globetrotter.model.SqliteUserDAO;
import com.example.globetrotter.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

import java.util.Objects;


public class LoginView extends StackPane {


    public LoginView() {
        UserService userService = new UserService(new SqliteUserDAO());


        String defaultStyle = """
            -fx-background-color: transparent;
            -fx-border-color: #00022e;
            -fx-text-fill: #00022e;
            -fx-border-width: 2;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-size: 14px;
        """;

        String selectedStyle = """
            -fx-background-color: #00022e;
            -fx-border-color: #00022e;
            -fx-text-fill: white;
            -fx-border-width: 2;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-size: 14px;
        """;

        StackPane root = new StackPane();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/loginBackground.png")));
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setTranslateY(50);

        imageView.setFitWidth(2000);
        imageView.setFitHeight(1125);

        VBox signUpCard = new VBox(15);
        signUpCard.setAlignment(Pos.TOP_CENTER);
        signUpCard.setPadding(new Insets(20));
        signUpCard.setMaxWidth(350);
        signUpCard.setMaxHeight(450);
        signUpCard.setStyle("""
                           -fx-background-color: rgb(255,255,255, 0.9);
                           -fx-background-radius: 15;
                           -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 0);
        """);
        Label globetrotter = new Label("GlobeTrotter");
        Label signUp = new Label("Sign Up");
        Label blurb = new Label("Create your account to explore exciting travel destinations and adventures.");

        globetrotter.setId("app-title-dark");
        signUp.setStyle("""
                -fx-font-size: 22px; 
                -fx-text-fill: #333333;                 
                -fx-font-weight: bold; 
                """);
        blurb.setWrapText(true);
        blurb.setTextAlignment(TextAlignment.CENTER);
        blurb.setStyle("""
                -fx-font-size: 12px;
                -fx-text-fill: #555555;
                """);
        blurb.setMaxWidth(300);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name (e.g. John)");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name (e.g. Deer)");
        TextField emailField = new TextField();
        emailField.setPromptText("E-mail (e.g. JohnD@gmail.com)");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Student and Teacher buttons side by side
        HBox roleButtons = new HBox(10);
        roleButtons.setAlignment(Pos.CENTER);

        Button studentBtn = new Button("Student");
        Button teacherBtn = new Button("Teacher");

        studentBtn.setStyle(defaultStyle);
        teacherBtn.setStyle(defaultStyle);

        studentBtn.setOnAction(e ->{
            studentBtn.setStyle(selectedStyle);
            teacherBtn.setStyle(defaultStyle);
        });
        teacherBtn.setOnAction(e ->{
            teacherBtn.setStyle(selectedStyle);
            studentBtn.setStyle(defaultStyle);
        });


        roleButtons.getChildren().addAll(studentBtn, teacherBtn);

        // Already a member? log in
        Button loginRedirectBtn = new Button("Already a member? Log in");
        loginRedirectBtn.setStyle("""
                                  -fx-background-color: transparent;
                                  -fx-text-fill: blue;
                                  -fx-underline: true;
                                  -fx-font-size: 12px;
        """);

        // Sign Up button
        Button signUpBtn = new Button("Sign Up");
        signUpBtn.setMaxWidth(Double.MAX_VALUE);
        signUpBtn.setStyle("""
                           -fx-background-color: #1f6357;
                           -fx-text-fill: white;
                           -fx-font-size: 14px;
                           -fx-background-radius: 8;
                           -fx-border-width: 0;
        """);
        Region spacer = new Region();
        spacer.setPrefHeight(20);

        signUpCard.getChildren().addAll(
                globetrotter,
                signUp,
                blurb,
                usernameField,
                firstNameField,
                lastNameField,
                emailField,
                passwordField,
                roleButtons,
                loginRedirectBtn,
                signUpBtn
        );

        root.getChildren().add(imageView);

        // ---------- LOGIN CARD ----------
        VBox loginCard = new VBox(15);
        loginCard.setAlignment(Pos.TOP_CENTER);
        loginCard.setPadding(new Insets(20));
        loginCard.setMaxWidth(350);
        loginCard.setMaxHeight(450);
        loginCard.setStyle("""
                   -fx-background-color: rgb(255,255,255, 0.9);
                   -fx-background-radius: 15;
                   -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.25, 0, 0);
""");
        Label globetrotter2 = new Label("GlobeTrotter");
        globetrotter2.setId("app-title-dark");
        Label loginTitle = new Label("Log In");
        loginTitle.setStyle("""
                -fx-font-size: 22px; 
                -fx-text-fill: #333333;                 
                -fx-font-weight: bold; 
                """);



        Label loginBlurb = new Label("Welcome back");
        loginBlurb.setWrapText(true);
        loginBlurb.setAlignment(Pos.CENTER);
        loginBlurb.setStyle("""
        -fx-font-size: 12px;
        -fx-text-fill: #555555;
""");
        loginBlurb.setMaxWidth(300);

// reuse your existing fields
        TextField usernameFieldLogin = new TextField();
        usernameFieldLogin.setPromptText("Username or Email");

        PasswordField passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setPromptText("Password");

        Button backToSignUpBtn = new Button("New here? Create an account");
        backToSignUpBtn.setStyle("""
                         -fx-background-color: transparent;
                         -fx-text-fill: blue;
                         -fx-underline: true;
                         -fx-font-size: 12px;
""");

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("""
                   -fx-background-color: #1f6357;
                   -fx-text-fill: white;
                   -fx-font-size: 14px;
                   -fx-background-radius: 8;
                   -fx-border-width: 0;
""");

        loginCard.getChildren().addAll(
                globetrotter2,
                loginTitle,
                loginBlurb,
                usernameFieldLogin,
                passwordFieldLogin,
                backToSignUpBtn,
                loginBtn
        );

        loginCard.setVisible(false);

// ---------- SWITCHING ----------
        loginRedirectBtn.setOnAction(e -> {
            signUpCard.setVisible(false);
            loginCard.setVisible(true);
        });

        backToSignUpBtn.setOnAction(e -> {
            loginCard.setVisible(false);
            signUpCard.setVisible(true);
        });


        StackPane cardsStack = new StackPane(signUpCard, loginCard);
        root.getChildren().addAll(cardsStack);
        getChildren().add(root);


        signUpBtn.setOnAction(e -> {
            try {
                // Determine selected role
                String userType = null;
                if (studentBtn.getStyle().equals(selectedStyle)) userType = "Student";
                else if (teacherBtn.getStyle().equals(selectedStyle)) userType = "Teacher";

                // Read input fields
                String username = usernameField.getText().trim(); // <-- added this line
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText();

                // Validate role selected
                if (userType == null) {
                    showAlert(Alert.AlertType.ERROR, "Please select a role");
                    return;
                }

                // Call UserService
                userService.createUser(userType, username, firstName, lastName, email, password);

                showAlert(Alert.AlertType.INFORMATION, "Account created successfully!");

                // Optionally, switch to login card
                signUpCard.setVisible(false);
                loginCard.setVisible(true);

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });

        loginBtn.setOnAction(e -> {
            try {
                String email = usernameFieldLogin.getText().trim();
                String password = passwordFieldLogin.getText();

                userService.authenticateUser(email, password);

                showAlert(Alert.AlertType.INFORMATION, "Login successful!");
                // TODO: proceed to main app screen
                StackPane parent = (StackPane) this.getParent();
                parent.getChildren().remove(this);

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });

    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
