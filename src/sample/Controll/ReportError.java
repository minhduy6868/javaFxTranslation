package sample.Controll;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportError implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TextField email;
    @FXML
    private TextArea txtTitles;
    @FXML
    private TextArea txtContents;
    @FXML
    private Button submit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DB_Controller.changeScene0(actionEvent, "/sample/FXML/login.fxml", "Login", true);
            }
        });

        submit.setOnAction(event -> {
            final String username = "mduy.test@gmail.com";
            final String password = "picydjvluzpdddcl";  // or use an app-specific password

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(prop,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            // ExecutorService for running tasks concurrently
            ExecutorService executor = Executors.newCachedThreadPool();

            // Task for sending email
            executor.submit(() -> {
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(email.getText()));
                    message.setSubject(txtTitles.getText());
                    message.setText("Thanks for your report: \n Your contents of report: \n " + txtContents.getText());

                    Transport.send(message);

                    System.out.println("Email sent successfully!");

                    // Run UI update on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        // Display a confirmation dialog using JavaFX
                        Alert emailSentAlert = new Alert(Alert.AlertType.INFORMATION);
                        emailSentAlert.setTitle("Email Sent");
                        emailSentAlert.setHeaderText(null);
                        emailSentAlert.setContentText("Email has been sent successfully!");
                        emailSentAlert.showAndWait();
                    });

                } catch (MessagingException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Could not send email");
                    errorAlert.setContentText("Error: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            });

            // Task for saving to file
            executor.submit(() -> {
                saveToFile(email.getText(), txtTitles.getText(), txtContents.getText());
            });

            // Shutdown executor after tasks are completed
            executor.shutdown();
        });
    }

    private void saveToFile(String email, String title, String content) {
        String filename = "report.txt";  // You can choose a different file name
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) { // Append mode
            writer.write("Email: " + email + "\n");
            writer.write("Title: " + title + "\n");
            writer.write("Content: " + content + "\n");
            writer.write("-------------------------------\n");
            System.out.println("Email content saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save to file");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
