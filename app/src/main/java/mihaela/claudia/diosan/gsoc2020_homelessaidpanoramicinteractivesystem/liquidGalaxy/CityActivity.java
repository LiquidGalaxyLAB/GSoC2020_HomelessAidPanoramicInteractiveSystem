package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {

    TextView city_tv, country_tv;
    MaterialCardView homeless, donors, volunteers;
    ImageView goHome;

    SharedPreferences preferences;
    SharedPreferences defaultPrefs;
    /*Firebase*/
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initViews();
        setActualLocation();
        mFirestore = FirebaseFirestore.getInstance();

        homeless.setOnClickListener(this);
        donors.setOnClickListener(this);
        volunteers.setOnClickListener(this);
        goHome.setOnClickListener(this);

    }

    private void initViews(){
        city_tv = findViewById(R.id.city_text);
        country_tv = findViewById(R.id.country_text);
        homeless = findViewById(R.id.homeless_cv);
        donors = findViewById(R.id.donors_cv);
        volunteers = findViewById(R.id.volunteers_cv);
        goHome = findViewById(R.id.go_home_iv);

    }

    private void setActualLocation(){

        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String city = preferences.getString("city","");
        String country = preferences.getString("country","");

        city_tv.setText(city);
        country_tv.setText(country);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeless_cv:
                showAllHomeless();
                startActivity(new Intent(CityActivity.this, HomelessActivity.class));
                break;
            case R.id.donors_cv:
                showAllDonors();
                startActivity(new Intent(CityActivity.this, DonorsActivity.class));
                break;
            case R.id.volunteers_cv:
                showAllVolunteers();
                startActivity(new Intent(CityActivity.this, VolunteersActivity.class));
                break;
            case R.id.go_home_iv:
                startActivity(new Intent(CityActivity.this, MainActivityLG.class));
                break;
        }
    }

    private void showAllHomeless(){

        String city = preferences.getString("city","");
        String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));

        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("homelessUsername");
                                final String longitude = document.getString("homelessLongitude");
                                final String latitude = document.getString("homelessLatitude");

                                 POI Homeless = new POI()
                                         .setName(name)
                                        .setLongitude(Double.parseDouble(longitude))
                                        .setLatitude(Double.parseDouble(latitude))
                                        .setAltitude(0.0d)
                                        .setHeading(0d)
                                        .setTilt(0d)
                                        .setRange(100.0d)
                                        .setAltitudeMode("relativeToSeaFloor ");
                                 POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "homeless");
                                 POIController.getInstance().showPlacemark(Homeless,null, "http://maps.google.com/mapfiles/kml/paddle/H.png", "homeless");
                            }}
                    }
                });
    }

    private void showAllDonors(){

        String city = preferences.getString("city","");
        String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));

        mFirestore.collection("donors").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("username");
                                final String longitude = document.getString("longitude");
                                final String latitude = document.getString("latitude");

                                POI Homeless = new POI()
                                        .setName(name)
                                        .setLongitude(Double.parseDouble(longitude))
                                        .setLatitude(Double.parseDouble(latitude))
                                        .setAltitude(0.0d)
                                        .setHeading(0d)
                                        .setTilt(0d)
                                        .setRange(100.0d)
                                        .setAltitudeMode("relativeToSeaFloor ");
                                POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "donors");
                                POIController.getInstance().showPlacemark(Homeless,null, "http://maps.google.com/mapfiles/kml/paddle/grn-diamond.png", "donors");
                            }}
                    }
                });
    }

    private void showAllVolunteers(){

        String city = preferences.getString("city","");
        String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));

        mFirestore.collection("volunteers").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("username");
                                final String longitude = document.getString("longitude");
                                final String latitude = document.getString("latitude");

                                POI Homeless = new POI()
                                        .setName(name)
                                        .setLongitude(Double.parseDouble(longitude))
                                        .setLatitude(Double.parseDouble(latitude))
                                        .setAltitude(0.0d)
                                        .setHeading(0d)
                                        .setTilt(0d)
                                        .setRange(100.0d)
                                        .setAltitudeMode("relativeToSeaFloor ");
                                POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "volunteers");
                                POIController.getInstance().showPlacemark(Homeless,null, "http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png", "volunteers");
                            }}
                    }
                });
    }


}