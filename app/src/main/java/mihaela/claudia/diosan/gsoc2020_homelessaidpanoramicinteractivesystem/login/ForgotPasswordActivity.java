package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }
}
