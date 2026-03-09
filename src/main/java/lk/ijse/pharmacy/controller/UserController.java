package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.UserBO;
import lk.ijse.pharmacy.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableView<UserDTO> tblUser;
    @FXML
    private TableColumn<UserDTO, Integer> colId;
    @FXML
    private TableColumn<UserDTO, String> colName;
    @FXML
    private TableColumn<UserDTO, String> colEmail;
    @FXML
    private TableColumn<UserDTO, String> colRole;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);

    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("admin", "employee");
        cmbRole.setItems(roles);

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadAllUsers();

        tblUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });
    }

    private void populateFields(UserDTO user) {
        txtId.setText(String.valueOf(user.getUserId()));
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtPassword.setText(user.getPassword());
        cmbRole.setValue(user.getRole());
    }

    private void loadAllUsers() {
        try {
            List<UserDTO> userList = userBO.getAllUsers();
            ObservableList<UserDTO> obList = FXCollections.observableArrayList(userList);
            tblUser.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load users: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!validateUserInput(name, email, password)) {
            return;
        }

        UserDTO user = new UserDTO(0, name, email, password, role);

        try {
            if (userBO.saveUser(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User Saved!").show();
                btnClearOnAction(event);
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String idText = txtId.getText().trim();

        if (idText.isEmpty() || !idText.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid User ID!").show();
            return;
        }

        int id = Integer.parseInt(idText);
        String name = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        if (!validateUserInput(name, email, password)) {
            return;
        }

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Update",
                "Are you sure you want to update this user details?",
                "update-alert");

        if (!confirmed) return;

        UserDTO user = new UserDTO(id, name, email, password, role);

        try {
            if (userBO.updateUser(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User Updated!").show();
                btnClearOnAction(event);
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed. ID may not exist.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText().trim();

        if (id.isEmpty() || !id.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Enter a valid User ID to delete").show();
            return;
        }

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Deletion",
                "Are you sure you want to delete this user?",
                "delete-alert");

        if (!confirmed) return;

        try {
            if (userBO.deleteUser(Integer.parseInt(id))) {
                new Alert(Alert.AlertType.INFORMATION, "User Deleted!").show();
                btnClearOnAction(event);
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Delete Failed. User Not Found.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText().trim();

        if (id.isEmpty() || !id.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Enter valid numeric ID").show();
            return;
        }

        try {
            UserDTO user = userBO.searchUser(Integer.parseInt(id));
            if (user != null) {
                populateFields(user);
            } else {
                new Alert(Alert.AlertType.WARNING, "User not found").show();
                btnClearOnAction(event);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtId.clear();
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        tblUser.getSelectionModel().clearSelection();
    }

    private boolean validateUserInput(String username, String email, String password) {
        if (!username.matches("[A-Za-z ]{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Username! Use letters only (A-Z).").show();
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email Address!").show();
            return false;
        }

        if (!password.matches("^.{3,}$")) {
            new Alert(Alert.AlertType.ERROR, "Password too short! Must be at least 3 characters.").show();
            return false;
        }

        return true;
    }
}