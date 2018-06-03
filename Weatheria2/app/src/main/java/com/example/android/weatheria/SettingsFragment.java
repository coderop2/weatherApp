package com.example.android.weatheria;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference;

import com.example.android.weatheria.data.WeatherContract;
import com.example.android.weatheria.data.contants_funcs;
import com.example.android.weatheria.sync.WeatherUtilities;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by harshitkhandelwal on 12/08/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_screen);
        SharedPreferences shp=getPreferenceScreen().getSharedPreferences();
        PreferenceScreen ps=getPreferenceScreen();
        int count_screen=ps.getPreferenceCount();
        for(int i=0;i<count_screen;i++)
        {
            Preference p=ps.getPreference(i);
            if(!(p instanceof CheckBoxPreference))
            {
                String value=shp.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }
        }
    }
    public void setPreferenceSummary(Preference pref, Object value)
    {
        String key=pref.getKey();
        String valse=value.toString();
        if(pref instanceof ListPreference)
        {
            ListPreference listp=(ListPreference)pref;
            int index=listp.findIndexOfValue(valse);
            if(index>=0)
            {
                pref.setSummary(listp.getEntries()[index]);
            }
        }
        else
        {
            pref.setSummary(valse);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Activity act=getActivity();
        if(s.equals(R.string.location_edit))
        {
            contants_funcs.resetLocationCoordinates(act);
            WeatherUtilities.startImmediateSync(act);
        }
        else if(s.equals(R.string.temp_list))
        {
            act.getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI,null);
        }
        Preference preference = findPreference(s);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(s, ""));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
