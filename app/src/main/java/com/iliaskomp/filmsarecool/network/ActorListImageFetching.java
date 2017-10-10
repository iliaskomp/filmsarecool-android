package com.iliaskomp.filmsarecool.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.iliaskomp.filmsarecool.R;
import com.iliaskomp.filmsarecool.config.TmdbConfig;
import com.iliaskomp.filmsarecool.models.credits.Actor;

import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_IMAGE_BASE_URL;

/**
 * Created by IliasKomp on 10/10/17.
 */

public class ActorListImageFetching {
    public static void setActorImage(final Actor actor, final ImageView imageView, final Context context) {
        String actorImagePath = actor.getProfilePath();
        String imageRequestUrl = API_IMAGE_BASE_URL + TmdbConfig.PosterSize.w45 + actorImagePath;

        if (actorImagePath == null) {
            setDefaultPoster(imageView, context);
        } else {
            ImageRequest imageRequest = new ImageRequest(
                    imageRequestUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
//                            actor.setPosterImage(response);
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
            mPosterImageView.setImageDrawable(context.getDrawable(R.drawable.actor_no_image_w45));
        } else {
            mPosterImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.actor_no_image_w45));
        }
    }
}
