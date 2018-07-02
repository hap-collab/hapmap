package com.hippotec.mapsapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.model.Comment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Avishay Peretz on 12/04/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    private ImageClickCallback imageClickCallback;

    final private int TYPE_COMMENT_WITH_IMAGE = 0;
    final private int TYPE_COMMENT_WITHOUT_IMAGE = 1;

    public CommentsAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.comments = list;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void registerCallback(ImageClickCallback imageClickCallback) {
        this.imageClickCallback = imageClickCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case TYPE_COMMENT_WITH_IMAGE:
                viewHolder = new CommentWithImageRecyclerViewHolder(layoutInflater.inflate(R.layout.item_comment_with_image, parent, false));
                break;
            case TYPE_COMMENT_WITHOUT_IMAGE:
                viewHolder = new CommentWithoutImageRecyclerViewHolder(layoutInflater.inflate(R.layout.item_comment_without_image, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Comment comment = comments.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_COMMENT_WITH_IMAGE:
                ((CommentWithImageRecyclerViewHolder)holder).bindCommentWithImage(comment);
                break;
            case TYPE_COMMENT_WITHOUT_IMAGE:
                ((CommentWithoutImageRecyclerViewHolder)holder).bindCommentWithoutImage(comment);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(comments.get(position).getImage())) {
            return TYPE_COMMENT_WITH_IMAGE;
        } else {
            return TYPE_COMMENT_WITHOUT_IMAGE;
        }
    }

    public class CommentWithImageRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_comment)
        TextView tvComment;

        @BindView(R.id.img_comment)
        ImageView img;

        private CommentWithImageRecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(CommentWithImageRecyclerViewHolder.this, view);
        }

        private void bindCommentWithImage(Comment comment) {

            tvComment.setText(comment.getComment());

            String imageUrl = comment.getImage();
            boolean shouldRemoveHostname = false;
            if (imageUrl.startsWith("http://127.0.0.1") || imageUrl.startsWith("http://localhost")) {
                imageUrl = imageUrl.substring("http://".length());
                shouldRemoveHostname = true;
            }
            if (shouldRemoveHostname) {
                imageUrl = URLs.BASE_URL + "/" + imageUrl.substring(imageUrl.indexOf('/') + 1);
            }

            Picasso.with(context).load(imageUrl)
                    //.placeholder( context.getResources().getDrawable(R.drawable.picture_placeholder))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            bitmap.setDensity(1); // this is needed so image isn't scaled down below boundries of layout
                            img.setImageBitmap(bitmap);
                            img.postInvalidate();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            img.setImageDrawable(errorDrawable);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            img.setImageDrawable(placeHolderDrawable);
                        }
                    });
            /*Picasso.with(context).load(imageUrl)
                    .placeholder( context.getResources().getDrawable(R.drawable.picture_placeholder))
                    .into(img);*/
            //img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            imageClickCallback.onImageClick(URLs.BASE_URL + "/" + comments.get(getLayoutPosition()).getImage()); // getAdapterPosition();
        }

    }

    public class CommentWithoutImageRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_comment)
        TextView tvComment;

        private CommentWithoutImageRecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(CommentWithoutImageRecyclerViewHolder.this, view);
        }

        private void bindCommentWithoutImage(Comment comment) {
            tvComment.setText(comment.getComment());
        }
    }

    public interface ImageClickCallback {
        void onImageClick(String commentImageUrlString);
    }


}

