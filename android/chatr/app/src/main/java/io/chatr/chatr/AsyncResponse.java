package io.chatr.chatr;

/**
 * Created by Daniel on 2018-03-24.
 */

public interface AsyncResponse {
    void processFinish(boolean success, int code, String message);
}
