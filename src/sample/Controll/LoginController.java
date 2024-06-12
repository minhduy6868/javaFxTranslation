package sample.Controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btn_report;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private Button btn_register_next;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_login.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DB_Controller.logIn(actionEvent, tf_username.getText(), tf_password.getText());
                    }
                }
        );

        btn_register_next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DB_Controller.changeScene(actionEvent, "register.fxml", "Register", null, null);
            }
        });
        btn_report.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DB_Controller.changeScene(actionEvent, "reportError.fxml", "Report", null, null);
            }
        });
    }
}
//StrongP@ssword123 duyen@gmail.com