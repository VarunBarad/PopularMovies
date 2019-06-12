package com.varunbarad.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.model.data.Review;
import com.varunbarad.popularmovies.util.UrlHelper;

import java.util.List;

/**
 * Creator: Varun Barad
 * Date: 23-10-2017
 * Project: PopularMovies
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_dialog_box_movie_details_review, parent, false);
        return new ReviewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.reviewAuthor.setText(this.reviews.get(position).getAuthor());
        holder.reviewContent.setText(this.reviews.get(position).getShortenedContent());
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView reviewAuthor;
        private AppCompatTextView reviewContent;
        private AppCompatTextView reviewShowMore;

        public ViewHolder(View itemView) {
            super(itemView);

            this.reviewAuthor = itemView.findViewById(R.id.textView_listItemMovieDetailsReview_author);
            this.reviewContent = itemView.findViewById(R.id.textView_listItemMovieDetailsReview_content);
            this.reviewShowMore = itemView.findViewById(R.id.textView_listItemMovieDetailsReview_showMore);

            this.reviewShowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UrlHelper.openUrlInBrowser(
                            context,
                            reviews.get(getAdapterPosition()).getUrl()
                    );
                }
            });
        }
    }
}
