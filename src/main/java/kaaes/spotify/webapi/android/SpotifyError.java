package kaaes.spotify.webapi.android;

import kaaes.spotify.webapi.android.models.*;
import retrofit.RetrofitError;

/**
 * This object wraps error responses from the Web API
 * and provides access to details returned by the request that are usually more
 * descriptive than default Retrofit error messages.
 * <p>
 * To use with asynchronous requests pass {@link SpotifyCallback}
 * instead of {@link retrofit.Callback} when making the request:
 * {code <pre>
 * spotify.getMySavedTracks(new SpotifyCallback&lt;Pager&lt;SavedTrack&gt;&gt;() {
 *     {@literal @}Override
 *     public void success(Pager&lt;SavedTrack&gt; savedTrackPager, Response response) {
 *         // handle successful response
 *     }
 *
 *     {@literal @}Override
 *     public void failure(SpotifyError error) {
 *         // handle error
 *     }
 * });
 * </pre>}
 * <p>
 * To use with synchronous requests:
 * {@code <pre>
 * try {
 *     Pager&lt;SavedTrack&gt; mySavedTracks = spotify.getMySavedTracks();
 * } catch (RetrofitError error) {
 *     SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
 * }
 * </pre>}
 */
public class SpotifyError extends Exception {

    private final RetrofitError mRetrofitError;
    private final ErrorDetails mErrorDetails;

    public static SpotifyError fromRetrofitError(RetrofitError error) {
        ErrorResponse errorResponse = (ErrorResponse) error.getBodyAs(ErrorResponse.class);

        if (errorResponse != null && errorResponse.error != null) {
            String message = errorResponse.error.status + " " + errorResponse.error.message;
            return new SpotifyError(error, errorResponse.error, message);
        } else {
            return new SpotifyError(error);
        }
    }

    public SpotifyError(RetrofitError retrofitError, ErrorDetails errorDetails, String message) {
        super(message, retrofitError);
        mRetrofitError = retrofitError;
        mErrorDetails = errorDetails;
    }

    public SpotifyError(RetrofitError retrofitError) {
        super(retrofitError);
        mRetrofitError = retrofitError;
        mErrorDetails = null;
    }

    /**
     * @return the original {@link retrofit.RetrofitError} that was returned for this request.
     */
    public RetrofitError getRetrofitError() {
        return mRetrofitError;
    }

    /**
     * @return true id there are {@link kaaes.spotify.webapi.android.models.ErrorDetails}
     * associated with this error. False otherwise.
     */
    public boolean hasErrorDetails() {
        return mErrorDetails != null;
    }

    /**
     * @return Details returned from the Web API associated with this error idf present.
     */
    public ErrorDetails getErrorDetails() {
        return mErrorDetails;
    }
}
