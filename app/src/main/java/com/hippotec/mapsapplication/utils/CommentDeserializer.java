package com.hippotec.mapsapplication.utils;

import com.hippotec.mapsapplication.api.JsonKeys;
import com.hippotec.mapsapplication.model.Comment;
import com.hippotec.mapsapplication.model.EventDetails;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yariv on 07/02/2018.
 */

public class CommentDeserializer {
    private static final String KEY_COMMENT_ID = "commentid";
    private static final String KEY_COMMENT_TEXT = "text";
    private static final String KEY_COMMENT_IMAGE_URL = "image";

    public static Comment fromJson(EventDetails event, int version, JSONObject json) throws JSONException {
        String commentId = json.getString(KEY_COMMENT_ID);
        String uid = json.getString(JsonKeys.KEY_USER_ID);
        String comment = json.optString(KEY_COMMENT_TEXT);
        String imageUrl = json.optString(KEY_COMMENT_IMAGE_URL);

        return new Comment(commentId, uid, event.getId(), comment, imageUrl);
    }
}
