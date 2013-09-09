/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.utils.Constants.UserStatus;

public class Utils {
    private static String TAG = "BuddySDK";

    public static int parseInt(String str) {
    	int result = 0;
    	try {
    		result = parseInt(str, false);
    	} catch (Exception ex) {
    	}
    	
        return result;
    }

    public static int parseInt(String str, Boolean throwException) throws Exception {
        int result = 0;
        if (!Utils.isNullOrEmpty(str)) {
            str = str.trim();
            try {
                result = Integer.parseInt(str);
            } catch (Exception ex) {
            	if(throwException)
            		throw ex;
            } 
        }

        return result;
    }

    public static double parseDouble(String str) {
        double result = 0;
        if (!Utils.isNullOrEmpty(str)) {
            str = str.trim();
            try {
                result = Double.parseDouble(str);
            } catch (NullPointerException npe) {
            } catch (NumberFormatException nfe) {
            }
        }
        return result;
    }

    public static BigDecimal parseDecimal(String str) {
        BigDecimal result = null;
        if (!Utils.isNullOrEmpty(str)) {
            str = str.trim();
            try {
                result = new BigDecimal(str);
            } catch (NullPointerException npe) {
            } catch (NumberFormatException nfe) {
            }
        }
        return result;
    }

    public static String convertStringDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

        String dateString = "";
        
        if(date != null) {            
            try {
                dateString = dateFormat.format(date);
            } catch (Exception e) {
                Log.d(TAG, "ConvertStringDate - Exception - " + e.getMessage());
    
                return "";
            }
        }

        return dateString;
    }

    public static String convertStringDate(Date date) {
        String dateString = Utils.convertStringDate(date, Constants.dateFormat);
        return dateString;
    }

    public static Date convertDateString(String dateString) {
        Date date = Utils.convertDateString(dateString, Constants.dateFormat);
        return date;
    }

    public static Date convertDateString(String dateString, String format) {
        Date date = null;
        try {
        	date = convertDateString(dateString, format, false);
        } catch (Exception ex) {
        }

        return date;
    }

    public static Date convertDateString(String dateString, String format, Boolean throwException) throws Exception {
        Date date = null;
        if (!Utils.isNullOrEmpty(dateString)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                Log.d(TAG, "ConvertStringDate - Exception - " + e.getMessage());
            	if(throwException)
            		throw e;
            }
        }

        return date;
    }

    public static Date convertMinDateStringTo1950(Date date){
    	return date == Constants.MinDate ? Utils.convertDateString("1/1/1950", "MM/dd/yyyy") : date;        
    }

    public static UserStatus getUserStatus(int statusId) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getValue() == statusId) {
                return status;
            }
        }

        return UserStatus.Any;
    }

    public static boolean isNullOrEmpty(String param) {
        return param == null || param.trim().length() == 0;
    }

    public static boolean isNumeric(String str) {
        boolean result = true;
        if (!Utils.isNullOrEmpty(str)) {
            str = str.trim();
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }

        return result;
    }

    public static Exception ProcessStandardErrors(String result) {
        List<String> errorList = Constants.getErrorList();
        if (errorList.contains(result)) {
            return new BuddyServiceException(result);
        }

        return null;
    }
}
