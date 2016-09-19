package com.jamieadkins.yearn.utils;

import com.google.maps.model.Photo;
import com.jamieadkins.yearn.secret.ApiKeys;

/**
 * Constructs a URL pointing to the photo that Glide can then fetch.
 */
public class PhotoUtils {
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 500;

    /**
     * Construct a url based on the photo reference.
     * @param photoReference id of the photo
     * @return url pointing to correct photo, to be handed to Glide
     */
    public static String constructUrl(String photoReference) {
        return "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxheight=" + MAX_HEIGHT +
                "&photoreference=" + photoReference +
                "&key=" + ApiKeys.GOOGLE_PLACES_API_WEB;
    }

    /**
     * Get the url of the 'best' available photo.
     * @param photos list of photos provided by Google Places search result
     * @return url of the best photo to use.
     */
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

    /**
     * Decide which photo is best from a list of photos.
     * @param photos list of photos provided by Google Places search result
     * @return photo reference of the best photo
     */
    private static String decideBestPhoto(Photo[] photos) {
        for (Photo photo : photos) {
            double aspectRatio = (double) photo.width / (double) photo.height;
            if (aspectRatio < 2.5 && aspectRatio > 1) {
                return photo.photoReference;
            }
        }

        return null;
    }
}
