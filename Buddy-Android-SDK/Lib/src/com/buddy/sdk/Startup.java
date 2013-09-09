/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

import java.math.BigDecimal;

import com.buddy.sdk.json.responses.StartupDataResponse.StartupData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single, named startup in the Buddy system.
 * 
 */
public class Startup {
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private double centerLat;
    private double centerLong;
    private String city;
    private String crunchBaseUrl;
    private String customData;
    private double distanceInKilometers;
    private double distanceInMeters;
    private double distanceInMiles;
    private double distanceInYards;
    private int employeeCount;
    private String facebookUrl;
    private String fundingSource;
    private String homePageUrl;
    private String industry;
    private String linkedInUrl;
    private String logoUrl;
    private String metroLocation;
    private String phoneNumber;
    private long startupID;
    private String startupName;
    private String state;
    private String streetAddress;
    private BigDecimal totalFundingRaised;
    private String twitterUrl;
    private String zipPostal;

    Startup(BuddyClient client, AuthenticatedUser user, StartupData startupData) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null");
        if (user == null)
            throw new IllegalArgumentException("user can't be null");
        this.client = client;
        this.user = user;

        this.centerLat = Utils.parseDouble(startupData.centerLat);
        this.centerLong = Utils.parseDouble(startupData.centerLong);
        this.city = startupData.city;
        this.crunchBaseUrl = startupData.crunchBaseUrl;
        this.customData = startupData.customData;
        this.distanceInKilometers = Utils.parseDouble(startupData.distanceInKilometers);
        this.distanceInMeters = Utils.parseDouble(startupData.distanceInMeters);
        this.distanceInMiles = Utils.parseDouble(startupData.distanceInMiles);
        this.distanceInYards = Utils.parseDouble(startupData.distanceInYards);
        this.employeeCount = Utils.parseInt(startupData.employeeCount);
        this.facebookUrl = startupData.facebookURL;
        this.fundingSource = startupData.fundingSource;
        this.homePageUrl = startupData.homePageURL;
        this.industry = startupData.industry;
        this.linkedInUrl = startupData.linkedinURL;
        this.logoUrl = startupData.logoURL;
        this.metroLocation = startupData.metroLocation;
        this.startupID = Utils.parseInt(startupData.startupID);
        this.startupName = startupData.startupName;
        this.state = startupData.state;
        this.streetAddress = startupData.streetAddress;
        this.totalFundingRaised = Utils.isNullOrEmpty(startupData.employeeCount) ?
                new BigDecimal("0") : Utils.parseDecimal(startupData.totalFundingRaised);
        this.twitterUrl = startupData.twitterURL;
        this.zipPostal = startupData.zipPostal;
    }

    /**
     * Gets the latitude of the center of the specified metro area.
     */
    public double getCenterLat() {
        return centerLat;
    }

    /**
     * Gets the longitude of the center of the specified metro area.
     */
    public double getCenterLong() {
        return centerLong;
    }

    /**
     * Gets the city in which the startup is located.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the crunchbase.com URL of the startup.
     */
    public String getCrunchBaseUrl() {
        return crunchBaseUrl;
    }

    /**
     * Gets the custom data of the startup.
     */
    public String getCustomData() {
        return customData;
    }

    /**
     * Gets the distance in kilometers from the center of the specified metro
     * area that the startup is located.
     */
    public double getDistanceInKilometers() {
        return distanceInKilometers;
    }

    /**
     * Gets the distance in meters from the center of the specified metro area
     * that the startup is located.
     */
    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    /**
     * Gets the distance in miles from the center of the specified metro area
     * that the startup is located.
     */
    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    /**
     * Gets the distance in yards from the center of the specified metro area
     * that the startup is located.
     */
    public double getDistanceInYards() {
        return distanceInYards;
    }

    /**
     * Gets the number of employees employed by the startup.
     */
    public int getEmployeeCount() {
        return employeeCount;
    }

    /**
     * Gets URL of the startup's Facebook page.
     */
    public String getFacebookUrl() {
        return facebookUrl;
    }

    /**
     * Gets the source of the funds raised by the startup.
     */
    public String getFundingSource() {
        return fundingSource;
    }

    /**
     * Gets the URL of the statup's home page.
     */
    public String getHomePageUrl() {
        return homePageUrl;
    }

    /**
     * Gets the industry of the startup.
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * Gets the URL of the startup's LinkedIn page.
     */
    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    /**
     * Gets the logo URL of the startup.
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * Gets the metro area in which the startup is located.
     */
    public String getMetroLocation() {
        return metroLocation;
    }

    /**
     * Gets the phone number of the startup.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the unique ID assigned to the startup.
     */
    public long getStartupID() {
        return startupID;
    }

    /**
     * Gets the company name of the startup.
     */
    public String getStartupName() {
        return startupName;
    }

    /**
     * Gets the state in which the startup is located.
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the address of the startup.
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Gets the amount of money that the startup has raised.
     */
    public BigDecimal getTotalFundingRaised() {
        return totalFundingRaised;
    }

    /**
     * Gets the startup's Twitter URL.
     */
    public String getTwitterUrl() {
        return twitterUrl;
    }

    /**
     * Gets the zip or postal code of the startup.
     */
    public String getZipPostal() {
        return zipPostal;
    }
}
