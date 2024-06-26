
package sample.Controll;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    @FXML
    private Button btn_register;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_confirm_password;
    @FXML
    private Button btn_login_next;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_register.setOnAction(this::registerUser);
        btn_login_next.setOnAction(this::changeToLoginScene);
    }

    private void registerUser(ActionEvent actionEvent) {
        if(tf_confirm_password.getText().equals(tf_password.getText()) ) {
            DB_Controller.registerUser(actionEvent, tf_username.getText(), tf_password.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Mật khẩu không khớp");
            alert.show();
        }



    }

    private void changeToLoginScene(ActionEvent actionEvent) {
        DB_Controller.changeScene(actionEvent, "/sample/FXML/login.fxml", "Login", null, null, true);
    }
}
