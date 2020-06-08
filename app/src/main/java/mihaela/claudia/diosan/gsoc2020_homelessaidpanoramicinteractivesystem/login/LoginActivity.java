package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.FirstActivityLG;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register.RegisterActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer.HomeVolunteer;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*TextViews*/
    TextView forgotPassword;

    /*Buttons*/
    Button loginBtn;
    MaterialButton signUp;
    MaterialButton statistics;

    /*EditTexts*/
    TextInputEditText loginEmailEditText;
    TextInputEditText loginPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        makeFullscreenActivity();
        initViews();



        loginBtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        statistics.setOnClickListener(this);
    }

    private void initViews() {
        forgotPassword = findViewById(R.id.forgot_password_text_view);
        signUp = findViewById(R.id.signup);
        statistics = findViewById(R.id.statistics_tv);
        loginEmailEditText = findViewById(R.id.login_email_edit_text);
        loginPasswordEditText = findViewById(R.id.login_password_edit_text);
        loginBtn = findViewById(R.id.login_button);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password_text_view:
                Intent forgotPassActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class );
                startActivity(forgotPassActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.signup:
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.statistics_tv:
                Intent statisticsActivity = new Intent(LoginActivity.this, FirstActivityLG.class);
                startActivity(statisticsActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_button:
             //TODO: firebase login
            startActivity(new Intent(LoginActivity.this, HomeVolunteer.class));
        }
    }

    private void makeFullscreenActivity() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }

}
