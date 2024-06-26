package sample.Controll;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;

public class DB_Controller {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void changeScene0(ActionEvent event, String fxmlFile, String title, boolean isTransparent) {
        try {
            FXMLLoader loader = new FXMLLoader(DB_Controller.class.getResource(fxmlFile));
            Parent root = loader.load();

            if (root == null) {
                throw new IOException("The root loaded from FXML is null.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            if (isTransparent) {
                stage.initStyle(StageStyle.TRANSPARENT);
                root.setStyle("-fx-background-color: transparent;");
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading scene: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static void changeScene1(MouseEvent event, String fxmlFile, String title, boolean isTransparent) {
        try {
            FXMLLoader loader = new FXMLLoader(DB_Controller.class.getResource(fxmlFile));
            Parent root = loader.load();

            if (root == null) {
                throw new IOException("The root loaded from FXML is null.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            if (isTransparent) {
                stage.initStyle(StageStyle.TRANSPARENT);
                root.setStyle("-fx-background-color: transparent;");
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading scene: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String password, boolean isTransparent) {
        Parent root = null;

        if (username != null && password != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DB_Controller.class.getResource(fxmlFile));
                root = loader.load();

                if (root == null) {
                    throw new IOException("The root loaded from FXML is null.");
                }

                // HomeController homeController = loader.getController();
                // homeController.setUserInfor(username, password);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading scene: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        } else {
            try {
                root = FXMLLoader.load(DB_Controller.class.getResource(fxmlFile));

                if (root == null) {
                    throw new IOException("The root loaded from FXML is null.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading scene: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));

        if (isTransparent) {
            stage.initStyle(StageStyle.TRANSPARENT);
            root.setStyle("-fx-background-color: transparent;");
        }

        stage.show();
    }

    public static void registerUser(ActionEvent actionEvent, String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream();
             ObjectOutputStream objectOutput = new ObjectOutputStream(output);
             ObjectInputStream objectInput = new ObjectInputStream(input)) {

            objectOutput.writeObject("register");
            objectOutput.writeObject(username);
            objectOutput.writeObject(password);

            boolean success = (boolean) objectInput.readObject();
            if (success) {
                changeScene(actionEvent, "/sample/FXML/sample.fxml", "Welcome", username, password, true);
            } else {
                showAlert("Registration failed", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            showAlert("Could not connect to server. Please check your network connection or try again later.", Alert.AlertType.ERROR);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void logIn(ActionEvent actionEvent, String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream();
             ObjectOutputStream objectOutput = new ObjectOutputStream(output);
             ObjectInputStream objectInput = new ObjectInputStream(input)) {

            objectOutput.writeObject("login");
            objectOutput.writeObject(username);
            objectOutput.writeObject(password);

            boolean success = (boolean) objectInput.readObject();
            if (success) {
                changeScene(actionEvent, "/sample/FXML/sample.fxml", "Home", username, password, true);
            } else {
                showAlert("Login failed", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            showAlert("Could not connect to server. Please check your network connection or try again later.", Alert.AlertType.ERROR);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to display alert
    private static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
