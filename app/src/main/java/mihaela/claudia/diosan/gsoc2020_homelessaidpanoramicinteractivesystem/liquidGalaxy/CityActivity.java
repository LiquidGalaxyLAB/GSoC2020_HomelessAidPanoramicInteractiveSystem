package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {

    TextView city_tv, country_tv;
    MaterialCardView homeless, donors, volunteers;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initViews();
        setActualLocation();

        homeless.setOnClickListener(this);
        donors.setOnClickListener(this);
        volunteers.setOnClickListener(this);

    }

    private void initViews(){
        city_tv = findViewById(R.id.city_text);
        country_tv = findViewById(R.id.country_text);
        homeless = findViewById(R.id.homeless_cv);
        donors = findViewById(R.id.donors_cv);
        volunteers = findViewById(R.id.volunteers_cv);

    }

    private void setActualLocation(){

        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        String city = preferences.getString("city","");
        String country = preferences.getString("country","");

        city_tv.setText(city);
        country_tv.setText(country);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeless_cv:
                startActivity(new Intent(CityActivity.this, HomelessActivity.class));
                break;
            case R.id.donors_cv:
                startActivity(new Intent(CityActivity.this, DonorsActivity.class));
                break;
            case R.id.volunteers_cv:
                startActivity(new Intent(CityActivity.this, VolunteersActivity.class));
                break;
        }
    }
}