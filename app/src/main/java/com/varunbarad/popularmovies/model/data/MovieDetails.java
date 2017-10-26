package com.varunbarad.popularmovies.model.data;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.varunbarad.popularmovies.util.data.MovieContract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creator: vbarad
 * Date: 2016-12-08
 * Project: PopularMovies
 */

public final class MovieDetails {
  @Expose
  @SerializedName("adult")
  private boolean adult;
  @Expose
  @SerializedName("backdrop_path")
  private String backdropPath;
  @Expose
  @SerializedName("belongs_to_collection")
  private CollectionStub collection;
  @Expose
  @SerializedName("budget")
  private long budget;
  @Expose
  @SerializedName("genres")
  private ArrayList<Genre> genres;
  @Expose
  @SerializedName("homepage")
  private String homepage;
  @Expose
  @SerializedName("id")
  private long id;
  @Expose
  @SerializedName("imdb_id")
  private String idIMDB;
  @Expose
  @SerializedName("original_language")
  private String originalLanguage;
  @Expose
  @SerializedName("original_title")
  private String originalTitle;
  @Expose
  @SerializedName("overview")
  private String overview;
  @Expose
  @SerializedName("popularity")
  private double popularity;
  @Expose
  @SerializedName("poster_path")
  private String posterPath;
  @Expose
  @SerializedName("production_companies")
  private ArrayList<CompanyStub> productionCompanies;
  @Expose
  @SerializedName("production_countries")
  private ArrayList<Country> productionCountries;
  @Expose
  @SerializedName("release_date")
  private String releaseDate;
  @Expose
  @SerializedName("revenue")
  private long revenue;
  @Expose
  @SerializedName("runtime")
  private int runtime;
  @Expose
  @SerializedName("spoken_languages")
  private ArrayList<Language> spokenLanguages;
  @Expose
  @SerializedName("status")
  private String status;
  @Expose
  @SerializedName("tagline")
  private String tagLine;
  @Expose
  @SerializedName("title")
  private String title;
  @Expose
  @SerializedName("video")
  private boolean video;
  @Expose
  @SerializedName("vote_average")
  private double averageVote;
  @Expose
  @SerializedName("vote_count")
  private long numberOfVotes;
  @Expose
  @SerializedName("videos")
  private VideoList videos;
  @Expose
  @SerializedName("images")
  private ImageList images;
  @Expose
  @SerializedName("reviews")
  private ReviewList reviews;
  @Expose
  @SerializedName("recommendations")
  private MovieList recommendations;
  @Expose
  @SerializedName("similar_movies")
  private MovieList similarMovies;
  
  /**
   * Public no-args constructor for serialization
   */
  public MovieDetails() {
  
  }
  
  /**
   * @param adult
   * @param backdropPath
   * @param collection
   * @param budget
   * @param genres
   * @param homepage
   * @param id
   * @param idIMDB
   * @param originalLanguage
   * @param originalTitle
   * @param overview
   * @param popularity
   * @param posterPath
   * @param productionCompanies
   * @param productionCountries
   * @param releaseDate
   * @param revenue
   * @param runtime
   * @param spokenLanguages
   * @param status
   * @param tagLine
   * @param title
   * @param video
   * @param averageVote
   * @param numberOfVotes
   * @param videos
   * @param images
   * @param reviews
   * @param recommendations
   * @param similarMovies
   */
  public MovieDetails(final boolean adult, final String backdropPath, final CollectionStub collection, final long budget, final ArrayList<Genre> genres, final String homepage, final long id, final String idIMDB, final String originalLanguage, final String originalTitle, final String overview, final double popularity, final String posterPath, final ArrayList<CompanyStub> productionCompanies, final ArrayList<Country> productionCountries, final String releaseDate, final long revenue, final int runtime, final ArrayList<Language> spokenLanguages, final String status, final String tagLine, final String title, final boolean video, final double averageVote, final long numberOfVotes, final VideoList videos, final ImageList images, final ReviewList reviews, final MovieList recommendations, final MovieList similarMovies) {
    this.adult = adult;
    this.backdropPath = backdropPath;
    this.collection = collection;
    this.budget = budget;
    this.genres = genres;
    this.homepage = homepage;
    this.id = id;
    this.idIMDB = idIMDB;
    this.originalLanguage = originalLanguage;
    this.originalTitle = originalTitle;
    this.overview = overview;
    this.popularity = popularity;
    this.posterPath = posterPath;
    this.productionCompanies = productionCompanies;
    this.productionCountries = productionCountries;
    this.releaseDate = releaseDate;
    this.revenue = revenue;
    this.runtime = runtime;
    this.spokenLanguages = spokenLanguages;
    this.status = status;
    this.tagLine = tagLine;
    this.title = title;
    this.video = video;
    this.averageVote = averageVote;
    this.numberOfVotes = numberOfVotes;
    this.videos = videos;
    this.images = images;
    this.reviews = reviews;
    this.recommendations = recommendations;
    this.similarMovies = similarMovies;
  }
  
  public static MovieDetails initFromContentValues(ContentValues values) {
    Gson gson = new Gson();
    
    long movieId = values.getAsLong(MovieContract.Movie.COLUMN_MOVIE_ID);
    boolean isAdult = (values.getAsInteger(MovieContract.Movie.COLUMN_ADULT) == 1);
    String backdropPath = values.getAsString(MovieContract.Movie.COLUMN_BACKDROP_PATH);
    CollectionStub collection = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_COLLECTION), CollectionStub.class);
    long budget = values.getAsLong(MovieContract.Movie.COLUMN_BUDGET);
    ArrayList<Genre> genres = new ArrayList<>(Arrays.asList(gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_GENRES), Genre[].class)));
    String homepage = values.getAsString(MovieContract.Movie.COLUMN_HOMEPAGE);
    String imdbId = values.getAsString(MovieContract.Movie.COLUMN_IMDB_ID);
    String originalLanguage = values.getAsString(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE);
    String originalTitle = values.getAsString(MovieContract.Movie.COLUMN_ORIGINAL_TITLE);
    String overview = values.getAsString(MovieContract.Movie.COLUMN_OVERVIEW);
    double popularity = values.getAsDouble(MovieContract.Movie.COLUMN_POPULARITY);
    String posterPath = values.getAsString(MovieContract.Movie.COLUMN_POSTER_PATH);
    ArrayList<CompanyStub> productionCompanies = new ArrayList<>(Arrays.asList(gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES), CompanyStub[].class)));
    ArrayList<Country> productionCountries = new ArrayList<>(Arrays.asList(gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES), Country[].class)));
    String releaseDate = values.getAsString(MovieContract.Movie.COLUMN_RELEASE_DATE);
    long revenue = values.getAsLong(MovieContract.Movie.COLUMN_REVENUE);
    int runtime = values.getAsInteger(MovieContract.Movie.COLUMN_RUNTIME);
    ArrayList<Language> spokenLanguages = new ArrayList<>(Arrays.asList(gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES), Language[].class)));
    String status = values.getAsString(MovieContract.Movie.COLUMN_STATUS);
    String tagline = values.getAsString(MovieContract.Movie.COLUMN_TAGLINE);
    String title = values.getAsString(MovieContract.Movie.COLUMN_TITLE);
    boolean isVideo = (values.getAsInteger(MovieContract.Movie.COLUMN_IS_VIDEO) == 1);
    double voteAverage = values.getAsDouble(MovieContract.Movie.COLUMN_VOTE_AVERAGE);
    long numberOfVotes = values.getAsLong(MovieContract.Movie.COLUMN_VOTE_COUNT);
    VideoList videos = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_VIDEOS), VideoList.class);
    ImageList images = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_IMAGES), ImageList.class);
    ReviewList reviews = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_REVIEWS), ReviewList.class);
    MovieList recommendations = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES), MovieList.class);
    MovieList similarMovies = gson.fromJson(values.getAsString(MovieContract.Movie.COLUMN_SIMILAR_MOVIES), MovieList.class);
    
    return new MovieDetails(
        isAdult,
        backdropPath,
        collection,
        budget,
        genres,
        homepage,
        movieId,
        imdbId,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        productionCompanies,
        productionCountries,
        releaseDate,
        revenue,
        runtime,
        spokenLanguages,
        status,
        tagline,
        title,
        isVideo,
        voteAverage,
        numberOfVotes,
        videos,
        images,
        reviews,
        recommendations,
        similarMovies
    );
  }
  
  public boolean isAdult() {
    return adult;
  }
  
  public String getBackdropPath() {
    return backdropPath;
  }
  
  public CollectionStub getCollection() {
    return collection;
  }
  
  public long getBudget() {
    return budget;
  }
  
  public ArrayList<Genre> getGenres() {
    return genres;
  }
  
  public String getHomepage() {
    return homepage;
  }
  
  public long getId() {
    return id;
  }
  
  public String getIdIMDB() {
    return idIMDB;
  }
  
  public String getOriginalLanguage() {
    return originalLanguage;
  }
  
  public String getOriginalTitle() {
    return originalTitle;
  }
  
  public String getOverview() {
    return overview;
  }
  
  public double getPopularity() {
    return popularity;
  }
  
  public String getPosterPath() {
    return posterPath;
  }
  
  public ArrayList<CompanyStub> getProductionCompanies() {
    return productionCompanies;
  }
  
  public ArrayList<Country> getProductionCountries() {
    return productionCountries;
  }
  
  public String getReleaseDate() {
    return releaseDate;
  }
  
  public long getRevenue() {
    return revenue;
  }
  
  public int getRuntime() {
    return runtime;
  }
  
  public ArrayList<Language> getSpokenLanguages() {
    return spokenLanguages;
  }
  
  public String getStatus() {
    return status;
  }
  
  public String getTagLine() {
    return tagLine;
  }
  
  public String getTitle() {
    return title;
  }
  
  public boolean isVideo() {
    return video;
  }
  
  public double getAverageVote() {
    return averageVote;
  }
  
  public long getNumberOfVotes() {
    return numberOfVotes;
  }
  
  public VideoList getVideos() {
    return videos;
  }
  
  public ImageList getImages() {
    return images;
  }
  
  public ReviewList getReviews() {
    return reviews;
  }
  
  public MovieList getRecommendations() {
    return recommendations;
  }
  
  public MovieList getSimilarMovies() {
    return similarMovies;
  }
  
  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
  
  public ContentValues toContentValues(boolean isFavorite) {
    ContentValues movieValues = new ContentValues();
    
    Gson gson = new Gson();
    
    movieValues.put(MovieContract.Movie.COLUMN_MOVIE_ID, this.getId());
    movieValues.put(MovieContract.Movie.COLUMN_ADULT, (this.isAdult() ? 1 : 0));
    movieValues.put(MovieContract.Movie.COLUMN_BACKDROP_PATH, this.getBackdropPath());
    movieValues.put(MovieContract.Movie.COLUMN_COLLECTION, gson.toJson(this.getCollection()));
    movieValues.put(MovieContract.Movie.COLUMN_BUDGET, this.getBudget());
    movieValues.put(MovieContract.Movie.COLUMN_GENRES, gson.toJson(this.getGenres()));
    movieValues.put(MovieContract.Movie.COLUMN_HOMEPAGE, this.getHomepage());
    movieValues.put(MovieContract.Movie.COLUMN_IMDB_ID, this.getIdIMDB());
    movieValues.put(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE, this.getOriginalLanguage());
    movieValues.put(MovieContract.Movie.COLUMN_ORIGINAL_TITLE, this.getOriginalTitle());
    movieValues.put(MovieContract.Movie.COLUMN_OVERVIEW, this.getOverview());
    movieValues.put(MovieContract.Movie.COLUMN_POPULARITY, this.getPopularity());
    movieValues.put(MovieContract.Movie.COLUMN_POSTER_PATH, this.getPosterPath());
    movieValues.put(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES, gson.toJson(this.getProductionCompanies()));
    movieValues.put(MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES, gson.toJson(this.getProductionCountries()));
    movieValues.put(MovieContract.Movie.COLUMN_RELEASE_DATE, this.getReleaseDate());
    movieValues.put(MovieContract.Movie.COLUMN_REVENUE, this.getRevenue());
    movieValues.put(MovieContract.Movie.COLUMN_RUNTIME, this.getRuntime());
    movieValues.put(MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES, gson.toJson(this.getSpokenLanguages()));
    movieValues.put(MovieContract.Movie.COLUMN_STATUS, this.getStatus());
    movieValues.put(MovieContract.Movie.COLUMN_TAGLINE, this.getTagLine());
    movieValues.put(MovieContract.Movie.COLUMN_TITLE, this.getTitle());
    movieValues.put(MovieContract.Movie.COLUMN_IS_VIDEO, (this.isVideo() ? 1 : 0));
    movieValues.put(MovieContract.Movie.COLUMN_VOTE_AVERAGE, this.getAverageVote());
    movieValues.put(MovieContract.Movie.COLUMN_VOTE_COUNT, this.getNumberOfVotes());
    movieValues.put(MovieContract.Movie.COLUMN_VIDEOS, gson.toJson(this.getVideos()));
    movieValues.put(MovieContract.Movie.COLUMN_IMAGES, gson.toJson(this.getImages()));
    movieValues.put(MovieContract.Movie.COLUMN_REVIEWS, gson.toJson(this.getReviews()));
    movieValues.put(MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES, gson.toJson(this.getRecommendations()));
    movieValues.put(MovieContract.Movie.COLUMN_SIMILAR_MOVIES, gson.toJson(this.getSimilarMovies()));
    movieValues.put(MovieContract.Movie.COLUMN_IS_FAVORITE, (isFavorite ? 1 : 0));
    
    return movieValues;
  }
}
