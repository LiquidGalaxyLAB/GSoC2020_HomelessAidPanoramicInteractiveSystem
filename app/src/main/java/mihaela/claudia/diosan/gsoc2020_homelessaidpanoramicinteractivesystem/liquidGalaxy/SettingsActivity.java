package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.EditText;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    android.preference.EditTextPreference lg_user, lg_password, lg_ip, lg_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lg_settings_preferences);

        lg_user = (EditTextPreference) findPreference("SSH-USER");
        lg_password = (EditTextPreference) findPreference("SSH-PASSWORD");
        lg_ip = (EditTextPreference) findPreference("SSH-IP");
        lg_port = (EditTextPreference) findPreference("SSH-PORT");

    }

    public void onStop() {
        super.onStop();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        LGConnectionManager.getInstance().setData(prefs.getString("SSH-USER", "lg"), prefs.getString("SSH-PASSWORD", "lqgalaxy"), prefs.getString("SSH-IP", "192.168.86.39"), Integer.parseInt(prefs.getString("SSH-PORT", "22")));

    }

    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            android.preference.ListPreference listPreference = (android.preference.ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference.getKey().toLowerCase().contains("password")) {
            EditText edit = ((EditTextPreference) preference).getEditText();
            String pref = edit.getTransformationMethod().getTransformation(stringValue, edit).toString();
            preference.setSummary(pref);

        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}