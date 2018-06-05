package bit.basemodule.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonParser {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'kk:mm:ss").create();

    public static <T> T parseStringToObject(final String json, final Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
