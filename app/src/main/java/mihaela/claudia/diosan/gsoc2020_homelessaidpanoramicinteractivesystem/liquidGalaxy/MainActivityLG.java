package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters.CitiesCardsAdapter;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.CityStatisticsTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.GetSessionTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.VisitPoiTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.Cities;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;

public class MainActivityLG extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cities, globalStatistics, cityStatistics;
    SharedPreferences preferences;

    private Map<String,String> cityInfo = new HashMap<>();
    private Map<String,String> globalInfo = new HashMap<>();

    /*Firebase*/
    private FirebaseFirestore mFirestore;

    private ProgressDialog dialog;
    private Session session;


    public static final POI EARTH_POI = new POI()
            .setName("Earth")
            .setLongitude(-3.629954d)  //10.52668d
            .setLatitude(40.769083d)  //40.085941d
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


        String user = preferences.getString("SSH-USER", "lg");
        String password = preferences.getString("SSH-PASSWORD", "lqgalaxy");
        String hostname = preferences.getString("SSH-IP", "");
        String port = preferences.getString("SSH-PORT", "22");

        LGConnectionManager lgConnectionManager = new LGConnectionManager();
        lgConnectionManager.setData(user, password, hostname, Integer.parseInt(port));


        cities.setOnClickListener(this);
        globalStatistics.setOnClickListener(this);
        cityStatistics.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        String user = preferences.getString("SSH-USER", "lg");
        String password = preferences.getString("SSH-PASSWORD", "lqgalaxy");
        String hostname = preferences.getString("SSH-IP", "192.168.1.76");
        String port = preferences.getString("SSH-PORT", "22");

        LGConnectionManager lgConnectionManager = new LGConnectionManager();
        lgConnectionManager.setData(user, password, hostname, Integer.parseInt(port));

    }

    private void initViews() {
        cities = findViewById(R.id.cities_cv);
        globalStatistics = findViewById(R.id.statistics_cv);
        cityStatistics = findViewById(R.id.city_statistics);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cities_cv:
                startActivity(new Intent(MainActivityLG.this, CitiesActivity.class));
                POIController.getInstance().moveToPOI(EARTH_POI, null);
                break;
            case R.id.statistics_cv:
                POIController.cleanKm();
                setGlobalStatistics();
                globalStatistics();
                break;

            case R.id.city_statistics:
                String message = getString(R.string.viewing) + " " + getString(R.string.city_statistics_txt) + " " + getResources().getString(R.string.inLG);

                dialog = new ProgressDialog(MainActivityLG.this);
                dialog.setMessage(message);
                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.stop), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LGUtils.closeSession(session);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                setCitiesStatistics();


        }
    }


    private void setGlobalStatistics(){
        getHomelessNumber();
        getDonorsNumber();
        getVolunteersNumber();
        getFood();
        getWork();
        getLodging();
        getClothes();
        getHygiene();
        getPersonallyNumber();
        getThroughVolunteerNumber();
    }

    private void globalStatistics(){
        mFirestore.collection("statistics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String homeless = document.getString("homeless");
                                final String donors = document.getString("donors");
                                final String volunteers = document.getString("volunteers");
                                final String food = document.getString("food");
                                final String clothes = document.getString("clothes");
                                final String work = document.getString("work");
                                final String lodging = document.getString("lodging");
                                final String hygiene = document.getString("hygiene");
                                final String personallyDonations = document.getString("personallyStatistics");
                                final String throughVolunteerDonations = document.getString("throughVolunteerStatistics");
                                final String image = document.getString("image");


                                POIController.getInstance().moveToPOI(STATISTICS, null);
                                POIController.getInstance().showBalloon(STATISTICS, null, buildGlobalStatistics(homeless, donors, volunteers, food, clothes, work, lodging, hygiene,personallyDonations, throughVolunteerDonations), null, "balloons/statistics");
                                POIController.getInstance().sendBalloon(STATISTICS, null, "balloons/statistics");

                                Toast.makeText(MainActivityLG.this,   buildGlobalStatistics(homeless, donors, volunteers, food, clothes, work, lodging, hygiene,personallyDonations, throughVolunteerDonations), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });



    }


    private void setCitiesStatistics(){
        mFirestore.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String city = document.getString("city");
                                final String cityWS = document.getString("cityWS");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");
                                final String altitude = document.getString("altitude");
                                final String homeless = document.getString("homelessNumber");
                                final String donors = document.getString("donorsNumber");
                                final String volunteers = document.getString("volunteersNumber");
                                final String foodSt = document.getString("foodSt");
                                final String clothesSt = document.getString("clothesSt");
                                final String workSt = document.getString("workSt");
                                final String lodgingSt = document.getString("lodgingSt");
                                final String hygieneSt = document.getString("hygieneSt");
                                final String image = document.getString("image");

                                getCityHomelessNumber(city);
                                getCityDonorsNumber(city);
                                getCityVolunteersNumber(city);
                                getCityFood(city);
                                getCityClothes(city);
                                getCityWork(city);
                                getCityLodging(city);
                                getCityHygiene(city);

                                POI cityPOI = createPOI(cityWS, latitude, longitude, altitude);

                                Toast.makeText(MainActivityLG.this,  buildCityStatistics(city,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), Toast.LENGTH_SHORT).show();

                                POIController.getInstance().showBalloon(cityPOI, null, buildCityStatistics(cityWS,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), null, "balloons/statistics/cities");
                                POIController.getInstance().sendBalloon(cityPOI, null,"balloons/statistics/cities" );
                                POIController.getInstance().flyToCity(cityPOI, null);
                                //  Toast.makeText(MainActivityLG.this,  buildCityStatistics(city,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), Toast.LENGTH_SHORT).show();
                            /*    POIController.getInstance().sendPlacemark(cityPOI,null, "192.168.86.228","balloons/statistics/cities" );*/
                            }
                        }
                    }
                });
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
                startActivity(new Intent(this, HelpActivity.class));
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

    private POI createPOI(String name, String latitude, String longitude, String altitude){

        POI poi = new POI()
                .setLongitude(Double.parseDouble(longitude))
                .setName(name)
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(Double.parseDouble(altitude))
                .setHeading(0.0d)
                .setTilt(40.0d)
                .setRange(800.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }

    private String buildGlobalStatistics(String homeless, String donors, String volunteers, String food, String clothes, String work, String lodging, String hygiene_products,String personallyStatistics,String throughVolunteerStatistics){
        return  "<h2> <b> USERS</b></h2>\n" +
                "<p> <b> Total homeless: </b> " + homeless + "</p>\n" +
                "<p> <b> Total donors: </b> " + donors + "</p>\n" +
                "<p> <b> Total volunteers: </b> " + volunteers + "</p>\n" +
                "<h2> <b> NEEDS</b></h2>\n" +
                "<p> <b> Food: </b> " + food + "</p>\n" +
                "<p> <b> Clothes: </b> " + clothes + "</p>\n" +
                "<p> <b> Work: </b> " + work + "</p>\n" +
                "<p> <b> Lodging: </b> " + lodging + "</p>\n" +
                "<p> <b> Hygiene products: </b> " + hygiene_products + "</p>\n" +
                "<h2> <b> DONATIONS</b></h2>\n" +
                "<p> <b> Personally Donations: </b> " + personallyStatistics + "</p>\n" +
                "<p> <b> Through Volunteer Donations: </b> " + throughVolunteerStatistics + "</p>\n" ;
    }


    public static  String buildCityStatistics(String city, String homeless, String donors, String volunteers, String food, String clothes, String work, String lodging, String hygiene_products){

        return  "<h2> <b> Local statistics from: </b> " + city + "</h2>\n" +
                "<h2> <b> USERS</b></h2>\n" +
                "<p> <b> Total homeless: </b> " + homeless + "</p>\n" +
                "<p> <b> Total donors: </b> " + donors + "</p>\n" +
                "<p> <b> Total volunteers: </b> " + volunteers + "</p>\n" +
                "<h2> <b> NEEDS</b></h2>\n" +
                "<p> <b> Food: </b> " + food + "</p>\n" +
                "<p> <b> Clothes: </b> " + clothes + "</p>\n" +
                "<p> <b> Work: </b> " + work + "</p>\n" +
                "<p> <b> Lodging: </b> " + lodging + "</p>\n" +
                "<p> <b> Hygiene products: </b> " + hygiene_products + "</p>\n";
    }


    private void getHomelessNumber(){

        mFirestore.collection("homeless").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String homeless = String.valueOf(task.getResult().size());
                            globalInfo.put("homeless", homeless);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }



    private void getCityHomelessNumber(String city){

        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String homelessNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("homelessNumber", homelessNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String donors = String.valueOf(task.getResult().size());
                            globalInfo.put("donors", donors);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityDonorsNumber(String city){
        mFirestore.collection("donors").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String donorsNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("donorsNumber", donorsNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String volunteers = String.valueOf(task.getResult().size());
                            globalInfo.put("volunteers", volunteers);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityVolunteersNumber(String city){
        mFirestore.collection("volunteers").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String volunteersNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("volunteersNumber", volunteersNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String food = String.valueOf(task.getResult().size());
                            globalInfo.put("food", food);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityFood(String city){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_food) )
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String foodSt = String.valueOf(task.getResult().size());
                            cityInfo.put("foodSt", foodSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String clothes = String.valueOf(task.getResult().size());
                            globalInfo.put("clothes", clothes);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityClothes(String city){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_clothes))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String clothesSt = String.valueOf(task.getResult().size());
                            cityInfo.put("clothesSt", clothesSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String work = String.valueOf(task.getResult().size());
                            globalInfo.put("work", work);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityWork(String city){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_work))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String workSt = String.valueOf(task.getResult().size());
                            cityInfo.put("workSt", workSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String lodging = String.valueOf(task.getResult().size());
                            globalInfo.put("lodging", lodging);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityLodging(String city){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_lodging))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String lodgingSt = String.valueOf(task.getResult().size());
                            cityInfo.put("lodgingSt", lodgingSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String hygieneProducts = String.valueOf(task.getResult().size());
                            globalInfo.put("hygieneProducts", hygieneProducts);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityHygiene(String city){
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_hygiene_products))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String hygieneSt = String.valueOf(task.getResult().size());
                            cityInfo.put("hygieneSt", hygieneSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
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
                            String personallyStatistics = String.valueOf(task.getResult().size());
                            globalInfo.put("personallyStatistics", personallyStatistics);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
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
                            String throughVolunteerStatistics = String.valueOf(task.getResult().size());
                            globalInfo.put("throughVolunteerStatistics", throughVolunteerStatistics);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    


}


