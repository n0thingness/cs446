package io.chatr.chatr.data.remote;

/**
 * Created by Daniel on 2018-03-22.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://uw-chatr-api.herokuapp.com/api/v1/";

    public static chatrAPI getChatrService() {
        return RetrofitClient.getClient(BASE_URL).create(chatrAPI.class);
    }
}
