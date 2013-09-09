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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Constants {
    public static Date MinDate = new Date(0);
    public static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";

    /**
     * Represents the gender of a user.
     */
    public enum UserGender {
        male, female, any
    }

    /**
     * Represents the status of the user.
     */
    public enum UserStatus {
        Single(1), Dating(2), Engaged(3), Married(4), Divorced(5), Widowed(6), OnTheProwl(7), Any(
                -1);

        private Integer value;

        private UserStatus(Integer val) {
            this.value = val;
        }

        public Integer getValue() {
            return this.value;
        }
    }

    private final static List<String> validErrors = Arrays.asList("WrongSocketLoginOrPass",
            "SecurityTokenInvalidPleaseRenew", "SecurityTokenRenewed",
            "SecurityTokenCouldNotBeRenewed", "SecurityFailedBannedDeviceID",
            "SecurityFailedBadUserNameOrPassword", "SecurityFailedBadUserName",
            "SecurityFailedBadUserPassword", "DeviceIDAlreadyInSystem", "UserNameAlreadyInUse",
            "UserNameAvailble", "UserAccountNotFound", "UserInvalidAccountSetting",
            "UserAccountErrorSettingMetaValue", "UserErrorUpdatingAccount", "UserEmailTaken",
            "UserEmailAvailable", "UserProfileIDEmpty", "IdentityValueEmpty", "DeviceIDNotFound",
            "DateTimeFormatWasIncorrect", "LatLongFormatWasIncorrect",
            "GeoLocationCategoryIncorrect", "BadGeoLocationName", "GeoLocationIDIncorrect",
            "BadParameter", "PhotoUploadGenericError", "CouldNotFindPhotoTodelete",
            "CouldNotDeleteFileGenericError", "PhotoAlbumDoesNotExist", "AlbumNamesCannotBeBlank",
            "PhotoIDDoesNotExistInContext", "dupelocation", "invalidflagreason", "EmptyDeviceURI",
            "EmptyGroupName", "EmptyImageURI", "EmptyMessageCount", "EmptyMessageTitle",
            "EmptyRawMessage", "EmptyToastTitle", "EmptyToastSubTitle", "EmptyToastParameter",
            "GroupNameCannotBeEmpty", "GroupSecurityCanOnlyBy0or1", "GroupAlreadyExists",
            "GroupChatIDEmpty", "GroupChatNotFound", "GroupOwnerSecurityError",
            "ApplicationAPICallDisabledByDeveloper");

    public static List<String> getErrorList() {
        return validErrors;
    }
}
