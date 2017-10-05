package com.iliaskomp.filmsarecool;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import static com.iliaskomp.filmsarecool.TmdbConfig.API_IMAGE_BASE_URL;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmPosterFetching {

    public static void setPosterImage(final FilmShortInfo film, final ImageView imageView, final Context context) {
        String posterPath = film.getPosterPath();
        String imageRequestUrl = API_IMAGE_BASE_URL + TmdbConfig.PosterSize.selected + posterPath;

        if (posterPath == null) {
            setDefaultPoster(imageView, context);
        } else {
            ImageRequest imageRequest = new ImageRequest(
                    imageRequestUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            film.setPosterImage(response);
                            imageView.setImageBitmap(response);
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setDefaultPoster(imageView, context);
                        }
                    });
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
            requestQueue.add(imageRequest);
        }
    }

    private static void setDefaultPoster(ImageView mPosterImageView, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mPosterImageView.setImageDrawable(context.getDrawable(R.drawable.noposter_w92));
        } else {
            mPosterImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noposter_w92));
        }
    }
}
