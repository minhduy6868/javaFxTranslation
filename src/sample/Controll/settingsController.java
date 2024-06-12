package sample.Controll;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.darkprograms.speech.translator.GoogleTranslate;
import javafx.event.ActionEvent;
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

public class settingsController implements Initializable
{


    public AnchorPane pan_settings;
    public ProgressBar pb_volume;
    public ProgressBar pb_speed;
    public ProgressBar pb_dopler;
    public ProgressBar pb_pitch;
    public Label lab_volume;
    public Label lab_settings;
    public Label lab_speed;
    public Label lab_pitch;
    public Label lab_dopler;
    public Button btn_apply;
    public Button btn_reset;


    SynthesiserV2 synthesizer=new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");;
    final double HEIGHT=-444f;

    public void initialize(URL location, ResourceBundle resources)
    {
    }


    public  void Sliders(MouseEvent event) {
        ProgressBar pb = (ProgressBar) event.getSource();
        double pos=event.getSceneX() - pb.getLayoutX() - 75f;
        pos/=pb.getWidth();
        if(pos<0) pos=0;
        else if(pos>1) pos=1;

        pb.setProgress(pos);
    }

    public void ApplySettings()
    {
      synthesizer.setSpeed(pb_speed.getProgress());
      synthesizer.setPitch(pb_pitch.getProgress());

        pan_settings.setVisible(false);
      //  bt.setOpacity(1f);
    }

    public  void ResetSettings()
    {
        pb_volume.setProgress(.5f);
        pb_dopler.setProgress(.5f);
        pb_speed.setProgress(.5f);
        pb_pitch.setProgress(.5f);
    }


}
