package sample.Controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Label lb_welcome;
    @FXML
    private Label lb_heyou;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_next;

    private String username;
    private String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DB_Controller.changeScene(actionEvent, "login.fxml", "Login", null, null );
            }
        });

        btn_next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DB_Controller.changeScene(event, "sample.fxml", "Main", username, password);
            }
        });
    }

    public void setUserInfor(String username, String password) {
        this.username = username;
        this.password = password;
        lb_welcome.setText("Welcome " + username);
        lb_heyou.setText("Your password is: " + password);
    }
}
