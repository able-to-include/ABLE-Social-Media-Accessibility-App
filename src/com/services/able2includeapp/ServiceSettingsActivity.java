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
import java.util.Set;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.services.able2includeapp.R;
import com.smart.able2include.PictogramSingleton;


public class ServiceSettingsActivity extends PreferenceActivity{
	   String DescOn = "";
	   String DescOff = "";
   ListPreference mlistEngine, mlistlanguage;
   EditTextPreference mtextSpeed;
   SwitchPreference CategoryConversation;
   SwitchPreference CategoryFeelings;
   SwitchPreference CategoryDimensions;
   SwitchPreference CategoryPeople;
   SwitchPreference CategoryAnimals;
   SwitchPreference CategoryHobbies;
   SwitchPreference CategoryPlace;
   SwitchPreference CategoryClothes;
   SwitchPreference CategoryNature;
   SwitchPreference CategoryFood;
   SwitchPreference CategoryStuff;
   SwitchPreference CategoryTransport;
   PreferenceScreen ScreenConversation;
   PreferenceScreen ScreenFeelings;
   PreferenceScreen ScreenDimensions;
   PreferenceScreen ScreenPeople;
   PreferenceScreen ScreenAnimals;
   PreferenceScreen ScreenHobbies;
   PreferenceScreen ScreenPlace;
   PreferenceScreen ScreenClothes;
   PreferenceScreen ScreenNature;
   PreferenceScreen ScreenFood;
   PreferenceScreen ScreenStuff;
   PreferenceScreen ScreenTransport;
   MultiSelectListPreference CategoryMulti;
   MultiSelectListPreference FeelingsMulti;
   MultiSelectListPreference DimensionsMulti;
   MultiSelectListPreference PeopleMulti;
   MultiSelectListPreference AnimalsMulti;
   MultiSelectListPreference HobbyMulti;
   MultiSelectListPreference PlaceMulti;
   MultiSelectListPreference ClothesMulti;
   MultiSelectListPreference NatureMulti;
   MultiSelectListPreference FoodMulti;
   MultiSelectListPreference StuffMulti;
   MultiSelectListPreference TransportMulti;
   PictogramSingleton mParser = null;
   
   
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

	
	OnPreferenceChangeListener listenerCategory = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				   UpdatePreferenceDisplay("CategoryConversation",newValue.toString(),true);
				   
			       return true;
			   }
			};
	OnPreferenceChangeListener listenerCategoryMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
			            try {
			                //noinspection unchecked
			                Set<String> selected = (Set<String>) newValue;
			               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			                Log.i("EDSTAG", ">>> listenerCategoryMulti <<< ");
			               mParser.updateActiveChildCategory("1",selected);

			            } catch (ClassCastException ignored) {
			            }					   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerFeelings = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryFeelingsAndBehavior",newValue.toString(),true);

			       return true;
			   }
			};	
			OnPreferenceChangeListener listenerFeelingsMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
		                mParser.updateActiveChildCategory("8",selected);	
			            } catch (ClassCastException ignored) {
			            }					  
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerDimensions = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryDimensions",newValue.toString(),true);

			       return true;
			   }
			};
			OnPreferenceChangeListener listenerDimensionsMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("21",selected);	
				            } catch (ClassCastException ignored) {
				            }							   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerPeople = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryPeople",newValue.toString(),true);

			       return true;
			   }
			};		
			OnPreferenceChangeListener listenerPeopleMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("26",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerAnimals = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryAnimals",newValue.toString(),true);

			       return true;
			   }
			};
			OnPreferenceChangeListener listenerAnimalsMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("33",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerHobbies = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryHobbies",newValue.toString(),true);

			       return true;
			   }
			};	
			OnPreferenceChangeListener listenerHobbyMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("42",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerPlace = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryPlace",newValue.toString(),true);

			       return true;
			   }
			};
			OnPreferenceChangeListener listenerPlaceMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("49",selected);	
				            } catch (ClassCastException ignored) {
				            }							   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerClothes = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryClothes",newValue.toString(),true);

			       return true;
			   }
			};			
		OnPreferenceChangeListener listenerClothesMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("59",selected);	
				            } catch (ClassCastException ignored) {
				            }							   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerNature = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryNature",newValue.toString(),true);

			       return true;
			   }
			};
		OnPreferenceChangeListener listenerNatureMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("65",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerFood = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryFood",newValue.toString(),true);

			       return true;
			   }
			};	
			OnPreferenceChangeListener listenerFoodMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("69",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerStuff = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryStuff",newValue.toString(),true);

			       return true;
			   }
			};
		OnPreferenceChangeListener listenerStuffMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("78",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};
		OnPreferenceChangeListener listenerTransport = new OnPreferenceChangeListener() {    
			   @Override
			   public boolean onPreferenceChange(Preference preference, Object newValue) {
				   UpdatePreferenceDisplay("CategoryTransport",newValue.toString(),true);

			       return true;
			   }
			};			
		OnPreferenceChangeListener listenerTransportMulti = new OnPreferenceChangeListener() {    
				   @Override
				   public boolean onPreferenceChange(Preference preference, Object newValue) {
					
		                Set<String> selected = (Set<String>) newValue;
		               // {"1", "8","21","26","33","42","49","59","65","69","78","88"};
			            try {
			                mParser.updateActiveChildCategory("88",selected);	
				            } catch (ClassCastException ignored) {
				            }						   
				       return true;
				   }
				};

			 /*  ScreenConversation = (PreferenceScreen)findPreference("CategoryConversation");
			   ScreenFeelings = (PreferenceScreen)findPreference("CategoryFeelingsAndBehavior");
			   ScreenDimensions = (PreferenceScreen)findPreference("CategoryDimensions");
			   ScreenPeople = (PreferenceScreen)findPreference("CategoryPeople"); 
			   ScreenAnimals = (PreferenceScreen)findPreference("CategoryAnimals");
			   ScreenHobbies = (PreferenceScreen)findPreference("CategoryHobbies");
			   ScreenPlace = (PreferenceScreen)findPreference("CategoryPlace");
			   ScreenClothes = (PreferenceScreen)findPreference("CategoryClothes");  
			   ScreenNature = (PreferenceScreen)findPreference("CategoryNature");
			   ScreenFood = (PreferenceScreen)findPreference("CategoryFood");
			   ScreenStuff = (PreferenceScreen)findPreference("CategoryStuff");
			   ScreenTransport = (PreferenceScreen)findPreference("CategoryTransport");  
			   ScreenMyImages = (PreferenceScreen)findPreference("CategoryMyImages");
			   ScreenPhotoFriends = (PreferenceScreen)findPreference("CategoryPhotoFriends"); */
	private PreferenceScreen getScreenObject(String key) {
		PreferenceScreen screen;
	     switch (key) {
	         case "CategoryConversation":
	        	 screen = ScreenConversation;
	        	 
	             break;
	         case "CategoryFeelingsAndBehavior":
	        	 screen = ScreenFeelings;
	             break;
	         case "CategoryDimensions":
	        	 screen = ScreenDimensions;
	             break;
	         case "CategoryPeople":
	        	 screen = ScreenPeople;
	             break;
	         case "CategoryAnimals":
	        	 screen = ScreenAnimals;
	             break;
	         case "CategoryHobbies":
	        	 screen = ScreenHobbies;
	             break;
	         case "CategoryPlace":
	        	 screen = ScreenPlace;
	             break;
	         case "CategoryClothes":
	        	 screen = ScreenClothes;
	             break;
	         case "CategoryNature":
	        	 screen = ScreenNature;
	             break;
	         case "CategoryFood":
	        	 screen = ScreenFood;
	             break;
	         case "CategoryStuff":
	        	 screen = ScreenStuff;
	             break;
	         case "CategoryTransport":
	        	 screen = ScreenTransport;
	             break;
         
	             
	         default:
	             throw new IllegalArgumentException("Invalid screen: " + key);
	     }
	     return screen;
	}
	private String getCategoryId(String key) {
		String Id;

	     switch (key) {
	         case "CategoryConversation":
	        	 Id = "1";
	        	 
	             break;
	         case "CategoryFeelingsAndBehavior":
	        	 Id = "8";
	             break;
	         case "CategoryDimensions":
	        	 Id = "21";
	             break;
	         case "CategoryPeople":
	        	 Id = "26";
	             break;
	         case "CategoryAnimals":
	        	 Id = "33";
	             break;
	         case "CategoryHobbies":
	        	 Id = "42";
	             break;
	         case "CategoryPlace":
	        	 Id = "49";
	             break;
	         case "CategoryClothes":
	        	 Id = "59";
	             break;
	         case "CategoryNature":
	        	 Id = "65";
	             break;
	         case "CategoryFood":
	        	 Id = "69";
	             break;
	         case "CategoryStuff":
	        	 Id = "78";
	             break;
	         case "CategoryTransport":
	        	 Id = "88";
	             break;
           
	             
	         default:
	             throw new IllegalArgumentException("Invalid screen: " + key);
	     }
	     return Id;
	}
	
	private void UpdatePreferenceDisplay(String screenkey ,String val,Boolean update)
	{
		   PreferenceScreen screen;
		   String base = "";
		   screen = getScreenObject(screenkey);
		 //  Log.i("EDSTAG", ">>> UpdatePreferenceDisplay<<< ");
		  base = getCategoryId(screenkey);
		  mParser.refreshPictoBase();
		  mParser.updateActiveCategory(base,val);
		   if(val.contains("true"))
		   {
			   screen.setSummary(DescOn);
			  
			   //BaseAdapter userScreenListAdapter = (BaseAdapter)ScreenConversation.getRootAdapter();
			  // Now when userX-settings are edited, I set the titles in the usersListScreen and after that call:
			   PreferenceScreen working = (PreferenceScreen)findPreference("Category");
			   BaseAdapter userScreenListAdapter =(BaseAdapter)working.getRootAdapter();
			   if(update)
			   {
				   userScreenListAdapter.notifyDataSetChanged();
			   }
			
			
		   }
		   else
		   {

			   screen.setSummary(DescOff);
			   PreferenceScreen working = (PreferenceScreen)findPreference("Category");
			   BaseAdapter userScreenListAdapter =(BaseAdapter)working.getRootAdapter();
   			   userScreenListAdapter.notifyDataSetChanged();
			   if(update)
			   {
				   userScreenListAdapter.notifyDataSetChanged();
			   }
		   }	
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.PrefTheme);
	   super.onCreate(savedInstanceState);
	   try
	   {
	   mParser = PictogramSingleton.getInstance(getApplicationContext());
	   addPreferencesFromResource(R.xml.prefs);
	 //  PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
	   DescOn = getApplicationContext().getString(R.string.CategoryON);
	   DescOff = getApplicationContext().getString(R.string.CategoryOFF);
	   mlistEngine= (ListPreference)findPreference("listextToSpeechEngine");
	   mlistlanguage= (ListPreference)findPreference("listextToSpeechLang");
	   mlistEngine.setOnPreferenceChangeListener(listenerEngine);
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

	   if(mParser != null)
	   {
	   CategoryConversation = (SwitchPreference)findPreference("checkboxConversation");
	   CategoryFeelings = (SwitchPreference)findPreference("checkboxFeelingsAndBehavior");
	   CategoryDimensions = (SwitchPreference)findPreference("checkboxDimensions");
	   CategoryPeople = (SwitchPreference)findPreference("checkboxPeople"); 
	   CategoryAnimals = (SwitchPreference)findPreference("checkboxAnimals");
	   CategoryHobbies = (SwitchPreference)findPreference("checkboxHobbies");
	   CategoryPlace = (SwitchPreference)findPreference("checkboxPlace");
	   CategoryClothes = (SwitchPreference)findPreference("checkboxClothes");  
	   CategoryNature = (SwitchPreference)findPreference("checkboxNature");
	   CategoryFood = (SwitchPreference)findPreference("checkboxFood");
	   CategoryStuff = (SwitchPreference)findPreference("checkboxStuff");
	   CategoryTransport = (SwitchPreference)findPreference("checkboxTransport");  

	   
	   ScreenConversation = (PreferenceScreen)findPreference("CategoryConversation");
	   ScreenFeelings = (PreferenceScreen)findPreference("CategoryFeelingsAndBehavior");
	   ScreenDimensions = (PreferenceScreen)findPreference("CategoryDimensions");
	   ScreenPeople = (PreferenceScreen)findPreference("CategoryPeople"); 
	   ScreenAnimals = (PreferenceScreen)findPreference("CategoryAnimals");
	   ScreenHobbies = (PreferenceScreen)findPreference("CategoryHobbies");
	   ScreenPlace = (PreferenceScreen)findPreference("CategoryPlace");
	   ScreenClothes = (PreferenceScreen)findPreference("CategoryClothes");  
	   ScreenNature = (PreferenceScreen)findPreference("CategoryNature");
	   ScreenFood = (PreferenceScreen)findPreference("CategoryFood");
	   ScreenStuff = (PreferenceScreen)findPreference("CategoryStuff");
	   ScreenTransport = (PreferenceScreen)findPreference("CategoryTransport");  


	   UpdatePreferenceDisplay("CategoryConversation",String.valueOf(CategoryConversation.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryFeelingsAndBehavior",String.valueOf(CategoryFeelings.isChecked()),false);	   
	   UpdatePreferenceDisplay("CategoryDimensions",String.valueOf(CategoryDimensions.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryPeople",String.valueOf(CategoryPeople.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryAnimals",String.valueOf(CategoryAnimals.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryHobbies",String.valueOf(CategoryHobbies.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryPlace",String.valueOf(CategoryPlace.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryClothes",String.valueOf(CategoryClothes.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryNature",String.valueOf(CategoryNature.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryFood",String.valueOf(CategoryFood.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryStuff",String.valueOf(CategoryStuff.isChecked()),false);
	   UpdatePreferenceDisplay("CategoryTransport",String.valueOf(CategoryTransport.isChecked()),false);
		   

		//checkboxConversation
		//multiselectlistCategoryConversation
		//checkboxFeelingsAndBehavior
		//multiselectlistCategoryFeelings
		//checkboxDimensions
		//multiselectlistCategoryDimensions
		//checkboxPeople
		//multiselectlistCategoryPeople
		//checkboxAnimals
		//multiselectlistCategoryAnimals
		//checkboxHobbies
		//multiselectlistCategoryHobby
		//checkboxPlace
		//multiselectlistCategoryPlace
		//checkboxClothes
		//multiselectlistCategoryClothes
		//checkboxNature
		//multiselectlistCategoryNature
		//checkboxFood
		//multiselectlistCategoryFood
		//checkboxStuff
		//multiselectlistCategoryStuff
		//checkboxTransport
		//multiselectlistCategoryTransport
	   CategoryMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryConversation");
	   FeelingsMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryFeelings");
	   DimensionsMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryDimensions");
	   PeopleMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryPeople");
	   AnimalsMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryAnimals");
	   HobbyMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryHobby");
	   PlaceMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryPlace");
	   ClothesMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryClothes");
	   NatureMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryNature");
	   FoodMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryFood");
	   StuffMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryStuff");
	   TransportMulti = (MultiSelectListPreference)findPreference("multiselectlistCategoryTransport");
	   }
	   //mtextSpeed = (EditTextPreference)findPreference("txtSpeed");

	 if(mParser != null)
	 {
	 CategoryConversation.setOnPreferenceChangeListener(listenerCategory);
	   CategoryFeelings.setOnPreferenceChangeListener(listenerFeelings);
	   CategoryDimensions.setOnPreferenceChangeListener(listenerDimensions);
	   CategoryPeople.setOnPreferenceChangeListener(listenerPeople);
	   CategoryAnimals.setOnPreferenceChangeListener(listenerAnimals);
	   CategoryHobbies.setOnPreferenceChangeListener(listenerHobbies);
	   CategoryPlace.setOnPreferenceChangeListener(listenerPlace);
	   CategoryClothes.setOnPreferenceChangeListener(listenerClothes);
	   CategoryNature.setOnPreferenceChangeListener(listenerNature);
	   CategoryFood.setOnPreferenceChangeListener(listenerFood);
	   CategoryStuff.setOnPreferenceChangeListener(listenerStuff);
	   CategoryTransport.setOnPreferenceChangeListener(listenerTransport);

	CategoryMulti.setOnPreferenceChangeListener(listenerCategoryMulti);
	   FeelingsMulti.setOnPreferenceChangeListener(listenerFeelingsMulti);
	   DimensionsMulti.setOnPreferenceChangeListener(listenerDimensionsMulti);
	   PeopleMulti.setOnPreferenceChangeListener(listenerPeopleMulti);
	   AnimalsMulti.setOnPreferenceChangeListener(listenerAnimalsMulti);
	   HobbyMulti.setOnPreferenceChangeListener(listenerHobbyMulti);
	   PlaceMulti.setOnPreferenceChangeListener(listenerPlaceMulti);
	   ClothesMulti.setOnPreferenceChangeListener(listenerClothesMulti);
	   NatureMulti.setOnPreferenceChangeListener(listenerNatureMulti);
	   FoodMulti.setOnPreferenceChangeListener(listenerFoodMulti);
	   StuffMulti.setOnPreferenceChangeListener(listenerStuffMulti);
	   TransportMulti.setOnPreferenceChangeListener(listenerTransportMulti);
	 }
	   }
	   catch (ClassCastException ignored) {
		   Log.i("EDSTAG", ">>> onCreate<<< " + "Exception");
       }	
	}
	
}
