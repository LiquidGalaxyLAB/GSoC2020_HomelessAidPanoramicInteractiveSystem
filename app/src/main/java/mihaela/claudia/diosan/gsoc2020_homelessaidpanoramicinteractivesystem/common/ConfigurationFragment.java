package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;


public class ConfigurationFragment extends PreferenceFragmentCompat {

    private EditTextPreference phonePreference;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.user_preferences, rootKey);

        phonePreference = (androidx.preference.EditTextPreference) findPreference("phone_volunteer");
    }

}
