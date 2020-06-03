package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    /*Buttons*/
    MaterialButton knowMoreDonorBtn;
    MaterialButton knowMoreVolunteerBtn;
    MaterialButton startRegisterDonorBtn;
    MaterialButton startRegisterVolunteerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        makeActivityFullScreen();
        initViews();

        knowMoreDonorBtn.setOnClickListener(this);
        knowMoreVolunteerBtn.setOnClickListener(this);
        startRegisterDonorBtn.setOnClickListener(this);
        startRegisterVolunteerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.know_more_donor_button:
                knowMorePopUp(getString(R.string.donor_know_more_title), getString(R.string.donor_know_more_text));
                break;
            case R.id.start_register_donor:
                startActivity(new Intent(RegisterActivity.this, RegisterDonorActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.know_more_volunteer_button:
                knowMorePopUp(getString(R.string.volunteer_know_more_title), getString(R.string.volunteer_know_more_text));
                break;
            case R.id.start_register_volunteer:
                startActivity(new Intent(RegisterActivity.this, RegisterVolunteerActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    private void makeActivityFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void initViews() {
        knowMoreDonorBtn = findViewById(R.id.know_more_donor_button);
        startRegisterDonorBtn = findViewById(R.id.start_register_donor);
        knowMoreVolunteerBtn = findViewById(R.id.know_more_volunteer_button);
        startRegisterVolunteerBtn = findViewById(R.id.start_register_volunteer);
    }


    private void knowMorePopUp(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.register_pop_up_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
