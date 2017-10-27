package com.restfb.voyans;

import com.restfb.types.User;

public class UserVoyans extends User {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    // -----------------------------------
    public UserVoyans() {
        
        super();
    }
    
    // -----------------------------------
    public UserVoyans(String id, String name, Picture picture) {
        
        setId(id);
        setName(name);
        setPicture(picture);
    }

    
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append("\n");
        sb.append("  getId = " + getId() + "\n");
        sb.append("  getName = " + getName() + "\n");
        sb.append("  getFirstName = " + getFirstName() + "\n");
        sb.append("  getMiddleName = " + getMiddleName() + "\n");
        sb.append("  getNameFormat = " + getNameFormat() + "\n");
        sb.append("  getPaymentPricepoints = " + getPaymentPricepoints() + "\n");
        sb.append("  getLastName = " + getLastName() + "\n");
        sb.append("  getShortName = " + getShortName() + "\n");
        sb.append("  getRelationshipStatus = " + getRelationshipStatus() + "\n");
        sb.append("  getAgeRange = " + getAgeRange() + "\n");
        sb.append("  getPublicKey = " + getPublicKey() + "\n");
        sb.append("  getPicture = " + getPicture() + "\n");
        sb.append("  getHometownName = " + getHometownName() + "\n");
        sb.append("  getSignificantOther = " + getSignificantOther() + "\n");
        sb.append("  getIsVerified = " + getIsVerified() + "\n");
        sb.append("  getInterestedIn = " + getInterestedIn() + "\n");
        sb.append("  getMeetingFor = " + getMeetingFor() + "\n");
        sb.append("  getFavoriteTeams = " + getFavoriteTeams() + "\n");
        sb.append("  getFavoriteAthletes = " + getFavoriteAthletes() + "\n");
        sb.append("  getInspirationalPeople = " + getInspirationalPeople() + "\n");
      
        return sb.toString();
    }

    
    
    
    // about,band_interests,bio,birthday,genre,hometown,culinary_team,verification_status,website
    
    public String toString2() {

        StringBuffer sb = new StringBuffer();

        sb.append("\n");
        sb.append("  getId = " + getId() + "\n");
        sb.append("  getName = " + getName() + "\n");
        
        sb.append("  getAbout = " + getAbout() + "\n");
        
        
        sb.append("  getMiddleName = " + getMiddleName() + "\n");
        sb.append("  getNameFormat = " + getNameFormat() + "\n");
        sb.append("  getPaymentPricepoints = " + getPaymentPricepoints() + "\n");
        sb.append("  getLastName = " + getLastName() + "\n");
        sb.append("  getShortName = " + getShortName() + "\n");
        sb.append("  getRelationshipStatus = " + getRelationshipStatus() + "\n");
        sb.append("  getAgeRange = " + getAgeRange() + "\n");
        sb.append("  getPublicKey = " + getPublicKey() + "\n");
        sb.append("  getPicture = " + getPicture() + "\n");
        sb.append("  getHometownName = " + getHometownName() + "\n");
        sb.append("  getSignificantOther = " + getSignificantOther() + "\n");
        sb.append("  getIsVerified = " + getIsVerified() + "\n");
        sb.append("  getInterestedIn = " + getInterestedIn() + "\n");
        sb.append("  getMeetingFor = " + getMeetingFor() + "\n");
        sb.append("  getFavoriteTeams = " + getFavoriteTeams() + "\n");
        sb.append("  getFavoriteAthletes = " + getFavoriteAthletes() + "\n");
        sb.append("  getInspirationalPeople = " + getInspirationalPeople() + "\n");
      
        return sb.toString();
    }

}
