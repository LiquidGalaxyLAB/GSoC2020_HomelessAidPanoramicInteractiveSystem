package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.install_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.first_time_install:
                    install();
                return true;
            case R.id.complete_uninstall:
                    uninstall();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void install(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.install_title))
                .setMessage(getString(R.string.install_description))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sentence = "cd /var/www/html/ ; mkdir -p hapis/{balloons/{basic/{donors,volunteers,homeless},bio/{donors,volunteers,homeless},statistics,transactions/{donors,volunteers,homeless}},placemarks/{donors,volunteers,homeless}} ; sudo apt install curl";
                        String donor = "cd /var/www/html/hapis/balloons/basic/donors/ ; curl -o donor.jpg https://ibb.co/0MKdfWm ;cd /var/www/html/hapis/balloons/bio/donors/ ; curl -o donor.jpg https://ibb.co/0MKdfWm ;cd /var/www/html/hapis/balloons/transactions/donors/ ; curl -o donor.jpg https://ibb.co/0MKdfWm";
                        String volunteer = " cd /var/www/html/hapis/balloons/basic/volunteers/ ; curl -o volunteer.jpg https://ibb.co/JcKMWwC;cd /var/www/html/hapis/balloons/bio/volunteers/ ; curl -o volunteer.jpg https://ibb.co/JcKMWwC; cd /var/www/html/hapis/balloons/transactions/volunteers/ ; curl -o volunteer.jpg https://ibb.co/JcKMWwC";
                        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));
                        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(donor, LGCommand.CRITICAL_MESSAGE, null));
                        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(volunteer, LGCommand.CRITICAL_MESSAGE, null));
                        Toast.makeText(ToolsActivity.this, getString(R.string.install_toast), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void uninstall(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.uninstall_title))
                .setMessage(getString(R.string.uninstall_description))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sentence = "cd /var/www/html/ ; rm -r hapis";
                        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));
                        Toast.makeText(ToolsActivity.this, getString(R.string.uninstall_toast), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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