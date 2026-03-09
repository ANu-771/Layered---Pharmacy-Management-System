package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.UserBO;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // BO Connection
    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);

    @FXML
    private void btnLoginOnAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        usernameField.setStyle(defaultStyle);
        passwordField.setStyle(defaultStyle);

        if (username.isEmpty()) { usernameField.setStyle(errorStyle); usernameField.requestFocus(); return; }
        if (password.isEmpty()) { passwordField.setStyle(errorStyle); passwordField.requestFocus(); return; }

        try {
            // Check username using BO
            boolean isUserFound = userBO.isUsernameExists(username);

            if (!isUserFound) {
                usernameField.setStyle(errorStyle);
                usernameField.requestFocus();
                new Alert(Alert.AlertType.ERROR, "Username not found!").show();
                return;
            }

            // Check Password using BO
            String role = userBO.checkLogin(username, password);

            if (role != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/layout.fxml"));
                Parent root = loader.load();
                LayoutController layoutController = loader.getController();
                layoutController.setRole(role);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.centerOnScreen();
                stage.show();
            } else {
                passwordField.setStyle(errorStyle);
                passwordField.requestFocus();
                new Alert(Alert.AlertType.ERROR, "Incorrect Password!").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void linkForgotPasswordOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgot_password.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Forgot Password");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load Forgot Password form.").show();
        }
    }
}