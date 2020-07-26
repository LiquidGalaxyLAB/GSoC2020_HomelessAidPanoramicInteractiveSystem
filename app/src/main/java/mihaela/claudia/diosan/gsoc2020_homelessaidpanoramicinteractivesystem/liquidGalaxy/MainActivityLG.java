package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;

public class MainActivityLG extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cities, statistics, demo, tour;
    private Context context;
    private Activity activity;
    private Session session;
    SharedPreferences preferences;
    TextView homelessNumberTV, donorNumberTV, volunteerNumberTV, foodStatisticsTV, clothesStatisticsTV, workStatisticsTV;
    TextView lodgingStatisticsTV, hygieneProductsStatisticsTV, personallyStatistics, throughVolunteerStatistics;

    /*Firebase*/
    private FirebaseFirestore mFirestore;


    public static final POI EARTH_POI = new POI()
            .setName("Earth")
            .setLongitude(-125.648786d)  //10.52668d
            .setLatitude(-9.784198d)  //40.085941d
            .setAltitude(0.0d)
            .setHeading(90.0d)      //90.0d
            .setTilt(0.0d)
            .setRange(10000000.0d)  //10000000.0d
            .setAltitudeMode("relativeToSeaFloor");

    public static final POI STATISTICS = new POI()
            .setName("GLOBAL_STATISTICS")
            .setLongitude(-3.285760d)
            .setLatitude(40.531229d)
            .setAltitude(0.0d)
            .setHeading(90.0d)
            .setTilt(0.0d)
            .setRange(10000000.0d)
            .setAltitudeMode("relativeToSeaFloor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_l_g);

        initViews();
        POIController.cleanKm();
        POIController.getInstance().moveToPOI(EARTH_POI, null);
        mFirestore = FirebaseFirestore.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        getHomelessNumber();
        getDonorsNumber();
        getVolunteersNumber();
        getFood();
        getClothes();
        getLodging();
        getWork();
        getHygiene();
        getPersonallyNumber();
        getThroughVolunteerNumber();

        cities.setOnClickListener(this);
        statistics.setOnClickListener(this);
        demo.setOnClickListener(this);
        tour.setOnClickListener(this);
        statistics.setOnClickListener(this);

    }



    private void initViews(){
        cities = findViewById(R.id.cities_cv);
        statistics = findViewById(R.id.statistics_cv);
        demo = findViewById(R.id.demo_cv);
        tour = findViewById(R.id.tour_cv);
        homelessNumberTV = findViewById(R.id.homelessNumberTV);
        homelessNumberTV.setVisibility(View.INVISIBLE);
        donorNumberTV = findViewById(R.id.donorNumberTV);
        donorNumberTV.setVisibility(View.INVISIBLE);
        volunteerNumberTV = findViewById(R.id.volunteerNumberTV);
        volunteerNumberTV.setVisibility(View.INVISIBLE);
        foodStatisticsTV = findViewById(R.id.foodStatisticsTV);
        foodStatisticsTV.setVisibility(View.INVISIBLE);
        clothesStatisticsTV = findViewById(R.id.clothesStatisticsTV);
        clothesStatisticsTV.setVisibility(View.INVISIBLE);
        workStatisticsTV = findViewById(R.id.workStatisticsTV);
        workStatisticsTV.setVisibility(View.INVISIBLE);
        lodgingStatisticsTV = findViewById(R.id.lodgingStatisticsTV);
        lodgingStatisticsTV.setVisibility(View.INVISIBLE);
        hygieneProductsStatisticsTV = findViewById(R.id.hygieneProductsStatisticsTV);
        hygieneProductsStatisticsTV.setVisibility(View.INVISIBLE);
        personallyStatistics = findViewById(R.id.personallyStatistics);
        personallyStatistics.setVisibility(View.INVISIBLE);
        throughVolunteerStatistics = findViewById(R.id.throughVolunteerStatistics);
        throughVolunteerStatistics.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cities_cv:
                startActivity(new Intent(MainActivityLG.this, CitiesActivity.class));
                POIController.getInstance().moveToPOI(EARTH_POI, null);
                break;
            case R.id.statistics_cv:
                POIController.cleanKm();
                POIController.getInstance().moveToPOI(STATISTICS, null);
                POIController.getInstance().showBalloon(STATISTICS, null, buildStatistics(),null, "balloons/statistics");
                POIController.getInstance().sendBalloon(STATISTICS, null, "balloons/statistics");
               break;

            case R.id.demo_cv:
                MainActivity.showSuccessToast(this,"Demo");
                break;
            case R.id.tour_cv:
                POIController.cleanKm();
                MainActivity.showSuccessToast(this,"Tour");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_lg:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_tools_lg:
                startActivity(new Intent(this, ToolsActivity.class));
                return true;
            case R.id.help_lg:
                startActivity(new Intent(this, HelpActivity .class));
                return true;
            case R.id.action_about_lg:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lg_about_dialog, null);

        androidx.appcompat.app.AlertDialog alert = new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.lg_about_title))
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.lg_about_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create();

        alert.show();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }

    private String buildStatistics(){
        return  "<h2> <b> USERS</b></h2>\n" +
                "<p> <b> Total homeless: </b> " + homelessNumberTV.getText().toString() + "</p>\n" +
                "<p> <b> Total donors: </b> " + donorNumberTV.getText().toString() + "</p>\n" +
                "<p> <b> Total volunteers: </b> " + volunteerNumberTV.getText().toString() + "</p>\n" +
                "<h2> <b> NEEDS</b></h2>\n" +
                "<p> <b> Food: </b> " + foodStatisticsTV.getText().toString() + "</p>\n" +
                "<p> <b> Clothes: </b> " + clothesStatisticsTV.getText().toString() + "</p>\n" +
                "<p> <b> Work: </b> " + workStatisticsTV.getText().toString() + "</p>\n" +
                "<p> <b> Lodging: </b> " + lodgingStatisticsTV.getText().toString() + "</p>\n" +
                "<p> <b> Hygiene products: </b> " + hygieneProductsStatisticsTV.getText().toString() + "</p>\n" +
                "<h2> <b> DONATIONS</b></h2>\n" +
                "<p> <b> Personally Donations: </b> " + personallyStatistics.getText().toString() + "</p>\n" +
                "<p> <b> Through Volunteer Donations: </b> " + throughVolunteerStatistics.getText().toString() + "</p>\n" ;
    }


    private void getHomelessNumber(){

        mFirestore.collection("homeless").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            homelessNumberTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getDonorsNumber(){
        mFirestore.collection("donors").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            donorNumberTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getVolunteersNumber(){
        mFirestore.collection("volunteers").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            volunteerNumberTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getFood(){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_food))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            foodStatisticsTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getClothes(){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_clothes))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            clothesStatisticsTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getWork(){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_work))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            workStatisticsTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getLodging(){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_lodging))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            lodgingStatisticsTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getHygiene(){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_hygiene_products))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            hygieneProductsStatisticsTV.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getPersonallyNumber(){
        mFirestore.collection("personallyDonations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            personallyStatistics.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }

    private void getThroughVolunteerNumber(){
        mFirestore.collection("throughVolunteerDonations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            throughVolunteerStatistics.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
    }


}


