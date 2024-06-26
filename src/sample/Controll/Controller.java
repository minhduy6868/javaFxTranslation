package sample.Controll;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.darkprograms.speech.translator.GoogleTranslate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    public ImageView btn_settings;
    public AnchorPane pan_user;
    public ImageView btn_user;
    public ImageView btn_logout;
    public ImageView btn_power;
    public AnchorPane pan_settings;
    public ProgressBar pb_volume;
    public AnchorPane pan_topbar;
    public AnchorPane pan_splas;
    public TextArea ta_speak;
    public ProgressBar pb_speed;
    public ProgressBar pb_dopler;
    public ProgressBar pb_pitch;
    public ComboBox<String> cb_languages;
    public Label user;
    public Label email;
    public Label lab_volume;
    public Label lab_settings;
    public Label lab_speed;
    public Label lab_pitch;
    public Label lab_dopler;
    public Button btn_apply;
    public Button btn_reset;
    public Label lab_translate;
    public Label lab_enter_text;
    public Button btn_speak;
    public Button btn_clear;
    public Label lab_main;
    public AnchorPane pan_internet;
    public Label lab_internet;
    public Button btn_internet_check;
    public AnchorPane pan_container;
    double translate = 4f;

    SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
    final double HEIGHT = -444f;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initApp();
        //showSplashScreen();
//        btn_logout.setOnAction(new EventHandler<ActionEvent>(){
//            @Override
//            public void handle(ActionEvent event) {
//                DB_Controller.changeScene0(event, "/sample/FXML/login.fxml", "Login", true);
//            }
//        });
    }

    private void showSplashScreen() {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                pan_splas.setTranslateY(pan_splas.getTranslateY() - 5);
                pan_splas.setOpacity(pan_splas.getOpacity() + .01f);

                if (pan_splas.getTranslateY() < HEIGHT) {
                    timer.cancel();
                    timer.purge();
                    pan_topbar.setVisible(true);
                    pan_splas.setVisible(false);
                }
            }
        };
        timer.schedule(task, 1, 20);
    }

    private void initApp() {
        btn_user.setOpacity(1f);
        btn_settings.setOpacity(1);
        btn_power.setOpacity(1);

        pan_user.setVisible(false);
        pan_settings.setVisible(false);
        pan_topbar.setVisible(false);
        lab_internet.setText("Application requires an internet connection. Check your connection, then try again.");
        user.setText(System.getProperty("user.name"));
        //email.setText(username);
        synthesizer.setSpeed(pb_speed.getProgress() * 10f);
        synthesizer.setPitch(pb_pitch.getProgress() * 10f);

        cb_languages.getItems().addAll(
                "VIETNAMESE",
                "CROATIAN",
                "ENGLISH",
                "SPANISH",
                "FRENCH",
                "CZECH"

        );

        checkConnectionToInternet();
    }

    public void checkConnectionToInternet() {
        try {
            pan_internet.setVisible(false);
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            showSplashScreen();
        } catch (IOException e) {
            System.out.println("Failed to verify connection catch");
            pan_internet.setVisible(true);
            disableAll();
            e.printStackTrace();
        }
    }

    public void disableAll() {
        pan_user.setVisible(false);
        pan_settings.setVisible(false);
        pan_topbar.setVisible(false);
    }

    public void comboAction(ActionEvent event) throws IOException {
        System.out.println(cb_languages.getValue());
        String targetLanguage = getLanguageCode(cb_languages.getValue());

        if (targetLanguage != null) {
            try {
                checkConnectionToInternet();
                String translatedText = GoogleTranslate.translate(targetLanguage, ta_speak.getText());
                ta_speak.setText(translatedText);
            } catch (IOException e) {
                e.printStackTrace();
                ta_speak.setText("Error in translation.");
            }
        } else {
            ta_speak.setText("Language code not found.");
        }
    }

    private String getLanguageCode(String language) {
        switch (language.toUpperCase()) {
            case "VIETNAMESE":
                return "vi";
            case "CROATIAN":
                return "hr";
            case "ENGLISH":
                return "en";
            case "SPANISH":
                return "es";
            case "FRENCH":
                return "fr";
            case "CZECH":
                return "cs";
            default:
                return null;
        }
    }


    public void showSettings(MouseEvent event) {
        pan_settings.setVisible(!pan_settings.isVisible());
        pan_user.setVisible(false);
        btn_settings.setOpacity(pan_settings.isVisible() ? .5f : 1);
        btn_user.setOpacity(1f);
    }

    public void showUser(MouseEvent event) {
        pan_settings.setVisible(false);
        pan_user.setVisible(!pan_user.isVisible());
        btn_settings.setOpacity(1f);
        btn_user.setOpacity(pan_user.isVisible() ? .5f : 1);
    }

    public void closeInternetCheckPanel() {
        System.exit(1);
    }

    public void QuitApp(MouseEvent event) {
        btn_power.setOpacity(.5f);
        pan_splas.setVisible(true);
        pan_splas.setOpacity(1f);
        pan_topbar.setVisible(false);
        pan_settings.setVisible(false);
        pan_user.setVisible(false);
        final Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                pan_splas.setTranslateY(pan_splas.getTranslateY() + 20);
                pan_splas.setOpacity(pan_splas.getOpacity() - .05f);
                if (pan_splas.getTranslateY() > 0) {
                    timer.cancel();
                    timer.purge();
                    System.exit(1);
                }
            }
        };
        timer.schedule(task, 1, 20);
    }

    public void Sliders(MouseEvent event) {
        ProgressBar pb = (ProgressBar) event.getSource();
        double pos = event.getSceneX() - pb.getLayoutX() - 75f;
        pos /= pb.getWidth();
        if (pos < 0) pos = 0;
        else if (pos > 1) pos = 1;

        pb.setProgress(pos);
    }

    public void ApplySettings() {
        synthesizer.setSpeed(pb_speed.getProgress());
        synthesizer.setPitch(pb_pitch.getProgress());

        pan_settings.setVisible(false);
        btn_settings.setOpacity(1f);
    }

    public void ResetSettings() {
        pb_volume.setProgress(.5f);
        pb_dopler.setProgress(.5f);
        pb_speed.setProgress(.5f);
        pb_pitch.setProgress(.5f);
    }
    public void setupLogoutButton(MouseEvent event) {
        System.out.println("Logout button pressed.");  // Debugging line

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to log out of this account?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                System.out.println("User confirmed logout.");  // Debugging line
                DB_Controller.changeScene1(event, "/sample/FXML/login.fxml", "Login", true);
            } else {
                System.out.println("User canceled logout.");  // Debugging line
            }
        });
    }



    public void ClearText() {
        ta_speak.setText("");
    }

    public void Speak(MouseEvent event) {
        System.out.println(ta_speak.getText());

        Thread thread = new Thread(() -> {
            try {
                String textToSpeak = ta_speak.getText();
                if (!textToSpeak.trim().isEmpty()) {
                    AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(textToSpeak));
                    player.play();
                    System.out.println("Successfully played the speech.");
                } else {
                    System.out.println("Text area is empty.");
                }
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
                checkConnectionToInternet();
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}
