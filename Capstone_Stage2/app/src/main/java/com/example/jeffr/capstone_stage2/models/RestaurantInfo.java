package com.example.jeffr.capstone_stage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantInfo {

    @SerializedName("result")
    Candidate candidates;

    public RestaurantInfo(Candidate candidates) {
        this.candidates = candidates;
    }

    public Candidate getCandidates() {
        return candidates;
    }

    public void setCandidates(Candidate candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {

        @SerializedName("photos")
        List<PhotoReference> photos;

        @SerializedName("place_id")
        String place_id;

        @SerializedName("reviews")
        List<Review> mReviews;

        public Candidate(
                List<PhotoReference> photos, String place_id,
                List<Review> reviews) {
            this.photos = photos;
            this.place_id = place_id;
            mReviews = reviews;
        }

        public List<Review> getReviews() {
            return mReviews;
        }

        public void setReviews(List<Review> reviews) {
            mReviews = reviews;
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

        public String getPhotoReference() {
            return photo_reference;
        }

        public void setPhotoReference(String photo_reference) {
            this.photo_reference = photo_reference;
        }
    }
}
