package com.jamieadkins.yearn.utils;

import android.util.Log;

import com.google.maps.model.Photo;
import com.jamieadkins.yearn.secret.ApiKeys;

/**
 * Constructs a URL pointing to the photo that Glide can then fetch.
 */
public class PhotoUtils {
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 500;

    public static String constructUrl(String photoReference) {
        return "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxheight=" + MAX_HEIGHT +
                "&photoreference=" + photoReference +
                "&key=" + ApiKeys.GOOGLE_PLACES_API_WEB;
    }

    public static String getBestPhoto(Photo[] photos) {

        if (photos == null || photos.length == 0) {
            return null;
        }

        String bestPhotoReference = decideBestPhoto(photos);

        if (bestPhotoReference == null) {
            return null;
        } else {
            return constructUrl(bestPhotoReference);
        }
    }

    private static String decideBestPhoto(Photo[] photos) {
        for (Photo photo : photos) {
            double aspectRatio = (double) photo.width / (double) photo.height;
            if (aspectRatio < 2.5 && aspectRatio > 1.5) {
                return photo.photoReference;
            }
        }

        return null;
    }
}
