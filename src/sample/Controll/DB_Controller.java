
package sample.Controll;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class DB_Controller {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String password) {
        Parent root = null;

        if(username != null && password != null) {
            try{
                FXMLLoader loader = new FXMLLoader(DB_Controller.class.getResource(fxmlFile));
                root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setUserInfor(username, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                root = FXMLLoader.load(DB_Controller.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();

    }


    public static void registerUser(ActionEvent actionEvent, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement checkUser = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/browser_account", "root","" );
            System.out.println(connection);
            checkUser = connection.prepareStatement("SELECT * FROM browser_account WHERE username = ?");
            checkUser.setString(1, username);
            resultSet = checkUser.executeQuery();

            String EMAIL_PATTERN =
                    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            String PASSWORD_PATTERN =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

            if(username.equals("") || password.equals("")) {
                showAlert("Vui lòng nhập đầy đủ các trường", Alert.AlertType.ERROR);
            } else if(!username.matches(EMAIL_PATTERN)){
                showAlert("Email không đúng định dạng", Alert.AlertType.ERROR);
            } else if(!password.matches(PASSWORD_PATTERN)) {
                showAlert("Mật khẩu quá yếu, thử lại", Alert.AlertType.ERROR);
            } else if (resultSet.next()) {
                showAlert("Người dùng đã tồn tại", Alert.AlertType.ERROR);
            } else {
                String hashedPassword = PasswordHashing.sha1(password);
                psInsert = connection.prepareStatement("INSERT INTO browser_account (username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, hashedPassword);
                psInsert.executeUpdate();
                System.out.println("Thành công nhập vào cơ sở dữ liệu");

                changeScene(actionEvent, "home.fxml", "Welcome", username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try{
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psInsert != null) {
                try{
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(checkUser != null) {
                try{
                    checkUser.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Phương thức hiển thị thông báo
    private static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    public static void logIn(ActionEvent actionEvent, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/browser_account", "root","" );
            System.out.println(connection);
            preparedStatement = connection.prepareStatement("SELECT password FROM browser_account WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("Người dùng không tồn tại");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ban không xài đợc tên tài khoản này, OK?");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String getPass = resultSet.getString("password");
                    String hashedInputPass = PasswordHashing.sha1(password); // Mã hóa mật khẩu người dùng nhập vào
                    if(getPass.equals(hashedInputPass)) {
                        changeScene(actionEvent, "home.fxml", "Home", username, password);
                    } else {
                        System.out.println("pass sai");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Nhập cái pass cũng sai, tương lai mày làm được gì?");
                        alert.show();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String URL = "jdbc:mysql://localhost:3306/browser_account";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}