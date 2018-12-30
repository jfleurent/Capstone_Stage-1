package com.example.jeffr.capstone_stage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantId {
    @SerializedName("results")
    List<Candidate> candidates;

    public RestaurantId(
            List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(
            List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {

        @SerializedName("place_id")
        String place_id;

        public Candidate(String place_id) {
            this.place_id = place_id;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

    }

}
