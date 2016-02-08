package com.services.able2includeapp;
/*
<license>
	<name> Apache License, Version 2.0 </name>
	<url> http://www.apache.org/licenses/LICENSE-2.0 </url>
	<comments> 
		The work represented by this file is partially funded by the ABLE-TO-INCLUDE project through the 
		European Commission's ICT Policy Support Programme as part of the Competitiveness & Innovation 
		Programme (Grant no.: 621055)
		Copyright © 2016, ABLE-TO-INCLUDE Consortium.
		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
		Unless required by applicable law or agreed to in writing, software distributed under the License 
		is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
		implied.
		See the License for the specific language governing permissions & limitations under the License.
	</comments>
</license>
*/
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.services.able2includeapp.R;

public class ServiceSettingsActivity extends PreferenceActivity{
   ListPreference mlistEngine, mlistlanguage;
   EditTextPreference mtextSpeed;
   OnPreferenceChangeListener listenerEngine = new OnPreferenceChangeListener() {    
	   @Override
	   public boolean onPreferenceChange(Preference preference, Object newValue) {
	       // newValue is the value you choose
		  // mlistEngine.setEnabled(true);
		   String val = newValue.toString();
		   if(val.contains("Local"))
		   {
			 //  mtextSpeed.setEnabled(true);
			   mlistlanguage.setEnabled(false);
		   }
		   else
		   {
			 //  mtextSpeed.setEnabled(false);
			   mlistlanguage.setEnabled(true);
		   }
	       return true;
	   }
	};
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.PrefTheme);
	   super.onCreate(savedInstanceState);
	   addPreferencesFromResource(R.xml.prefs);
	 
	   mlistEngine= (ListPreference)findPreference("listextToSpeechEngine");
	   mlistlanguage= (ListPreference)findPreference("listextToSpeechLang");
	   //mtextSpeed = (EditTextPreference)findPreference("txtSpeed");
	   if(mlistEngine.getValue() != null)
	   {
	   if(mlistEngine.getValue().contains("Local") )
	   {
	        // playButton.setVisibility(View.GONE);
           //  stopButton.setVisibility(View.VISIBLE);
		  // mtextSpeed.setEnabled(true);
		   mlistlanguage.setEnabled(false);
	   }
	   else
	   {
		   //mtextSpeed.setEnabled(false);
		   mlistlanguage.setEnabled(true);
	   }
	   }
	   mlistEngine.setOnPreferenceChangeListener(listenerEngine);
	}
	
}
