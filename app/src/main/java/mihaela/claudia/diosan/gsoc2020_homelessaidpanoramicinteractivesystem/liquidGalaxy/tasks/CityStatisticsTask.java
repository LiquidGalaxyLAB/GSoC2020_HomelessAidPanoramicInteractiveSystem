package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;

public class CityStatisticsTask extends AsyncTask<Void, Void, String> {

    String command;
    POI currentPoi;
    private ProgressDialog progressDialog;
    Activity activity;
    Context context;

    public CityStatisticsTask(String command, POI currentPoi,Activity activity, Context context) {
        this.command = command;
        this.currentPoi = currentPoi;
        this.activity = activity;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (progressDialog == null){
            progressDialog = new ProgressDialog(context);
            String message = context.getResources().getString(R.string.viewing) + " " + "local statistics " + " " + context.getResources().getString(R.string.inLG);
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);

            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();cancel(true);
                }
            });
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            progressDialog.show();
        }

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Session session = LGUtils.getSession(activity);
            LGUtils.setConnectionWithLiquidGalaxy(session, command, activity);


            while (!isCancelled()){
                session.sendKeepAliveMsg();
                Thread.sleep(10000);

            }

            return "";
        } catch (JSchException e) {
            this.cancel(true);
            if (progressDialog != null){
                progressDialog.dismiss();
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } catch (IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getResources().getString(R.string.visualizationCanceled), Toast.LENGTH_LONG).show();
                }
            });
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null){
            if (progressDialog != null){
                progressDialog.hide();
                progressDialog.dismiss();
            }
        }
    }
}
