package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.MainActivityLG;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;


public class CityStatisticsTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    Activity activity;
    Context context;
    String command;
    private Map<String,String> cityInfo = new HashMap<>();

    /*Firebase*/
    private FirebaseFirestore mFirestore;


    public CityStatisticsTask(String command, Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.command = command;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog == null) {
            mFirestore = FirebaseFirestore.getInstance();

            dialog = new ProgressDialog(context);
            String message = context.getResources().getString(R.string.viewing) + context.getResources().getString(R.string.inLG);
            dialog.setMessage(message);
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);


            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    cancel(true);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });


        }
    }


    @Override
    protected String doInBackground(Void... params) {
        setCitiesStatistics();
            return "";

        }

    public static void downloadCityPhoto(String city, String imageUrl){
        String sentence = "cd /var/www/html/hapis/balloons/statistics/cities/ ;curl -o " + city + " " + imageUrl;
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
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
                                downloadCityPhoto(cityPOI.getName(),image );
                                // Toast.makeText(MainActivityLG.this,  buildCityStatistics(city,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), Toast.LENGTH_SHORT).show();

                              //  POIController.getInstance().showBalloonOnSlave(cityPOI, null, buildCityStatistics(cityWS,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), cityPOI.getName(), "slave_3");
                                //POIController.getInstance().sendBalloon(cityPOI, null,"balloons/statistics/cities" );
                                POIController.getInstance().flyToCity(cityPOI, null);

                                //  Toast.makeText(MainActivityLG.this,  buildCityStatistics(city,homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), Toast.LENGTH_SHORT).show();
                                /*    POIController.getInstance().sendPlacemark(cityPOI,null, "192.168.86.228","balloons/statistics/cities" );*/
                            }
                        }
                    }
                });
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

    private String buildCityStatistics(String city, String homeless, String donors, String volunteers, String food, String clothes, String work, String lodging, String hygiene_products){

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

        private void getCityFood(String city){
            mFirestore.collection("homeless")
                    .whereEqualTo("homelessNeed", context.getString(R.string.chip_food) )
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


        private void getCityClothes(String city){
            mFirestore.collection("homeless")
                    .whereEqualTo("homelessNeed", context.getString(R.string.chip_clothes))
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

        private void getCityWork(String city){
            mFirestore.collection("homeless")
                    .whereEqualTo("homelessNeed", context.getString(R.string.chip_work))
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

        private void getCityLodging(String city){
            mFirestore.collection("homeless")
                    .whereEqualTo("homelessNeed", context.getString(R.string.chip_lodging))
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

        private void getCityHygiene(String city){
            mFirestore.collection("homeless")
                    .whereEqualTo("homelessNeed", context.getString(R.string.chip_hygiene_products))
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



        @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        if (success != null) {
            if (dialog != null) {
                dialog.hide();
                dialog.dismiss();
            }
        }
    }
}
