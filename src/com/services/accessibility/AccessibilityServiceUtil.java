package com.services.accessibility;
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
import java.util.ArrayList;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class AccessibilityServiceUtil {

	public static ArrayList<String> getAllAccessibilityServices(Context context) {
		TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
		ArrayList<String> allAccessibilityServices = new ArrayList<String>();

		String settingValue = Settings.Secure.getString(
				context.getApplicationContext().getContentResolver(),
				Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

		if (settingValue != null) {
			colonSplitter.setString(settingValue);
			while (colonSplitter.hasNext()) {
				String accessabilityService = colonSplitter.next();
				allAccessibilityServices.add(accessabilityService);
			}
		}
		return allAccessibilityServices;
	}

	public static boolean isAccessibilityServiceOn(Context context, String packageName, String className) {
		ArrayList<String> allAccessibilityServices = getAllAccessibilityServices(context);
		StringBuffer concat = new StringBuffer();
		concat.append(packageName);
		concat.append('/');
		concat.append(className);

		return allAccessibilityServices.contains(concat.toString());
	}
}
