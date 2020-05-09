package id.exorty.snpk.monira.ui.typeofactivity;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import id.exorty.snpk.monira.helper.Config;
import id.exorty.snpk.monira.service.ApiEndpointInterface;
import id.exorty.snpk.monira.ui.model.ReturnDataReq;
import id.exorty.snpk.monira.ui.model.ViewModelParam;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PageViewModel extends ViewModel {
    private MutableLiveData<Integer> mIndex = new MutableLiveData<Integer>();
    private LiveData<Integer> mData = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer param) {
            return param;
        }
    });

    public void setIndex(Integer param) {
        mIndex.setValue(param);
    }

    public LiveData<Integer> getData() {
        return mData;
    }

    public JsonObject loadData(String token, int id_type_of_activity, int year) {
        JsonObject[] data = {null};

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Call<ReturnDataReq> call = apiService.getActivityDetailData("application/json",
                "Bearer " + Config.APP_BEARER_TOKEN, Config.APP_NAME, Config.DEVICE_ID, token, id_type_of_activity, year);

        call.enqueue(new Callback<ReturnDataReq>() {
            @Override
            public void onResponse(Call<ReturnDataReq> call, Response<ReturnDataReq> response) {
                data[0] = Json.parse(response.toString()).asObject();
            }

            @Override
            public void onFailure(Call<ReturnDataReq> call, Throwable t) {
                String a = "ss";
            }
        });

        return data[0];
    };


}