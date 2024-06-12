package sample.Controll;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.darkprograms.speech.translator.GoogleTranslate;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SpeechController implements Initializable {

    public AnchorPane pan_user;
    public TextArea ta_speak;
    public ComboBox<String> cb_languages;
    public Label user;
    public Label lab_translate;
    public Label lab_enter_text;
    public Button btn_speak;
    public Button btn_clear;
    public Label lab_main;

    private SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cb_languages.getItems().addAll("CRO", "ENG", "ESP", "FRA", "CZE", "VIE");
        cb_languages.setValue("ENG");
    }

    public void comboAction(ActionEvent event) {
        try {
            String selectedLanguage = cb_languages.getValue();
            System.out.println(selectedLanguage);
            String targetLanguage = getLanguageCode(selectedLanguage);
            if (targetLanguage != null) {
                String inputText = ta_speak.getText();
                if (!inputText.trim().isEmpty()) {
                    // Translate text using Google Translate API
                    String translatedText = GoogleTranslate.translate(targetLanguage, inputText);
                    ta_speak.setText(translatedText);
                } else {
                    ta_speak.setText("Please enter text to translate.");
                }
            } else {
                ta_speak.setText("Language code not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ta_speak.setText("Error in translation.");
        }
    }

    private String getLanguageCode(String language) {
        switch (language) {
            case "CRO":
                return "hr";
            case "ENG":
                return "en";
            case "ESP":
                return "es";
            case "FRA":
                return "fr";
            case "CZE":
                return "cs";
            case "VIE":
                return "vi";
            default:
                return null;
        }
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
                System.out.println("Error in playing the speech.");
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}
