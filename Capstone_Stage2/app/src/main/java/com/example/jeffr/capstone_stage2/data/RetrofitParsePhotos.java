package com.example.jeffr.capstone_stage2.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitParsePhotos {

    @SerializedName("results")
    List<Candidate> candidates;

    public RetrofitParsePhotos(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {

        @SerializedName("photos")
        List<PhotoReference> photos;

        //TODO implement code to see reviews
        @SerializedName("place_id")
        String place_id;

        public Candidate(
                List<PhotoReference> photos, String place_id) {
            this.photos = photos;
            this.place_id = place_id;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<PhotoReference> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotoReference> photos) {
            this.photos = photos;
        }
    }

    public static class PhotoReference {

        @SerializedName("photo_reference")
        String photo_reference;

        public PhotoReference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }
    }
}
