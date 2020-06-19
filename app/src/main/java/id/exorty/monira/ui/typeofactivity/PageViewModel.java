package id.exorty.monira.ui.typeofactivity;

import android.app.Activity;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import id.exorty.monira.service.DataService;

public class PageViewModel extends ViewModel {
    private JsonObject mHoldData;
    private MutableLiveData<JsonObject> mIndex = new MutableLiveData<JsonObject>();
    private LiveData<JsonObject> mData = Transformations.map(mIndex, new Function<JsonObject, JsonObject>() {
        @Override
        public JsonObject apply(JsonObject data) {
            return data;
        }
    });

    public void setIndex(JsonObject param) {
        mIndex.setValue(param);
    }

    public LiveData<JsonObject> getData() {
        return mData;
    }

    public void loadData(Activity activity, String token, String id_type_of_activity, int year, final int index) {
        if (mHoldData == null) {
            DataService dataService = new DataService(activity, new DataService.DataServiceListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void OnSuccess(JsonObject jsonObject, String message) {
                    mHoldData = jsonObject;
                    JsonObject param = Json.object();
                    param.add("index", index);
                    param.add("data", jsonObject);
                    mIndex.setValue(param);
                }

                @Override
                public void OnSuccess(JsonArray jsonArray, String message) {
                    String a = message;
                }

                @Override
                public void OnFailed(String message, String fullMessage) {
                    String a = message;
                }
            }).GetActivityDetailData(id_type_of_activity, year);
        }else{
            JsonObject param = Json.object();
            param.add("index", index);
            param.add("data", mHoldData);
            mIndex.setValue(param);
        }

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Config.BASE_API_URL_S)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        final ApiEndpointInterface apiService =
//                retrofit.create(ApiEndpointInterface.class);
//        Call<ReturnDataReq> call = apiService.getActivityDetailData("application/json",
//                "Bearer " + Config.APP_BEARER_TOKEN, Config.APP_NAME, Config.DEVICE_ID, token, id_type_of_activity, year);
//
//        call.enqueue(new Callback<ReturnDataReq>() {
//            @Override
//            public void onResponse(Call<ReturnDataReq> call, Response<ReturnDataReq> response) {
//                mIndex.setValue(Json.parse(response.toString()).asObject());
//            }
//
//            @Override
//            public void onFailure(Call<ReturnDataReq> call, Throwable t) {
//                String a = "ss";
//            }
//        });

    };

}
