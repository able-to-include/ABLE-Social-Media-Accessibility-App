package com.smart.network;
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
public class Constants {
	   /**
     * The host IP Address
     */
    //public final static String HOST = "http://5.56.57.123";            // Gigas
    //public final static String HOST = "http://arwen.asi-soft.com";     // Arwen
    public final static String HOST = "http://al.abletoinclude.eu";            // Gigas
    
    /**
     * The Simplext webservice path
     */
 //   public final static String SIMPLEXT_URL = HOST
 //           + ":8080/ABLE_Webservice/ABLE_API/Simplext/";
       public final static String SIMPLEXT_URL = HOST
             + "/Simplext.php";  
    /**
     * The Text2Picto webservice path
     */
   // public final static String TEXT2PICTO_URL = HOST
   //         + ":8080/ABLE_Webservice/ABLE_API/Text2Picto/";
    public final static String TEXT2PICTO_URL = HOST
            + "/Text2Picto.php";
    /**
     * The Text2Speech webservice path
     */
   // public final static String TEXT2SPEECH_URL = HOST
   //         + ":8080/ABLE_Webservice/ABLE_API/Text2Speech/";
    public final static String TEXT2SPEECH_URL = HOST
            + "/Text2Speech.php";
    /**
     * Indicates the Simplext method
     */
    public final static int SIMPLEXT = 1;
    /**
     * Indicates the Text2Picto method
     */
    public final static int TEXT2PICTO = 2;
    /**
     * Indicates the Text2Speech method
     */
    public final static int TEXT2SPEECH = 3;
    public final static String service_error = "Service error";
    public final static String error408 = " service is not available at this time !";
    public final static String error451 = "You have not entered text";
    public final static String error452= "You have not entered language";
    public final static String error453= "You have not entered type of pictogram";
    public final static String error454= "You have not entered neither text nor language";
    public final static String error455= "You have not entered neither text nor language or type of pictogram";
    public final static String error456= "You have not entered neither language nor type of pictogram";
    public final static String error457= "You have not entered neither text nor type of pictogram";
    public final static String error458= "The language is not valid";
    public final static String error459= "The type of pictogram is not valid";

}
