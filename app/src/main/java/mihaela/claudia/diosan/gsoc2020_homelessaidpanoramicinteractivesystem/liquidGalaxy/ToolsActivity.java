package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;

public class ToolsActivity extends AppCompatActivity {

    MaterialButton cleanKmls, relaunchLG, rebootLG, shutdownLG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        initViews();
        setCleanKMLButtonBehaviour();
        //   setRelaunchButtonBehaviour();
        setRebootButtonBehaviour();
        setShutDownButtonBehaviour();


        relaunchLG.setOnClickListener(v -> {
            try {
                String sentence = "/home/lg/bin/lg-relaunch > /home/lg/log.txt";
                showAlertAndExecution(sentence, "relaunch");
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initViews(){
        cleanKmls = findViewById(R.id.cleanKmls);
        relaunchLG = findViewById(R.id.relaunch);
        rebootLG = findViewById(R.id.reboot);
        shutdownLG = findViewById(R.id.shutdown);
    }


    private void setCleanKMLButtonBehaviour() {
        cleanKmls.setOnClickListener(view -> {
            try {
                //String sentence = "rm -f /var/www/html/kmls.txt; touch /var/www/html/kmls.txt > /home/lg/log.txt";
                String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
                showAlertAndExecution(sentence, "clean kml files");
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*RELAUNCH*/
    //When relaunch Liquid Galaxy button is clicked, the sentence to achieve it is send to the LG.
    private void setRelaunchButtonBehaviour() {
        relaunchLG.setOnClickListener(v -> {
            try {
                String sentence = "/home/lg/bin/lg-relaunch > /home/lg/log.txt";
                showAlertAndExecution(sentence, "relaunch");
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*REBOOT*/
    //When reboot Liquid Galaxy button is clicked, the sentence to achieve it is send to the LG.
    private void setRebootButtonBehaviour() {
        rebootLG.setOnClickListener(v -> {
            try {
                String sentence = "/home/lg/bin/lg-reboot > /home/lg/log.txt";
                showAlertAndExecution(sentence, "reboot");

            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*SHUT DOWN*/
    //When shut down Liquid Galaxy button is clicked, the sentence to achieve it is send to the LG.
    private void setShutDownButtonBehaviour() {
        shutdownLG.setOnClickListener(v -> {
            try {
                String sentence = "/home/lg/bin/lg-poweroff > /home/lg/log.txt";
                showAlertAndExecution(sentence, "shut down");
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*SHUT DOWN, RELAUNCH and REBOOT*/
    private void showAlertAndExecution(final String sentence, String action) {
        // prepare the alert box
        MaterialAlertDialogBuilder alertbox = new MaterialAlertDialogBuilder(this);

        // set the message to display
        alertbox.setMessage("Are you sure to " + action + " Liquid Galaxy?");

        // set a positive/yes button and create a listener
        // When button is clicked
        alertbox.setPositiveButton(getResources().getString(R.string.lg_positive_btn), (arg0, arg1) -> {
            LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));
        });

        // set a negative/no button and create a listener
        // When button is clicked
        alertbox.setNegativeButton(getResources().getString(R.string.lg_negative_btn), (arg0, arg1) -> {
        });
        // display box
        alertbox.show();
    }



}