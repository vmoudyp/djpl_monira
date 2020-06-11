package id.exorty.monira.service;

import android.app.Activity;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import id.exorty.monira.helper.Config;

import static id.exorty.monira.helper.Config.RETURN_STATUS_OK;
import static id.exorty.monira.helper.Util.GetSharedPreferences;
import static id.exorty.monira.helper.Util.SaveSharedPreferences;

public class DataService {
    private Activity mActivity;
    private DataServiceListener mDataServiceListener;

    public interface DataServiceListener {
        void onStart();
        void OnSuccess(JsonObject jsonObject, String message);
        void OnSuccess(JsonArray jsonArray, String message);
        void OnFailed(String message);
    }

    public DataService(Activity activity, DataServiceListener dataServiceListener){
        this.mActivity = activity;
        this.mDataServiceListener = dataServiceListener;
    }

    public DataService Login(final String userName, final String password){
        mDataServiceListener.onStart();

        String url = Config.BASE_API_URL + "api/v1/login?msisdn=" + userName + "&password=" + password + "&remember_me=1";

        Ion.with(mActivity)
                .load("POST", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    SaveSharedPreferences(mActivity, "token", joResult.get("result").asObject().getString("token",""));
                                    SaveSharedPreferences(mActivity, "full_name", joResult.get("result").asObject().getString("full_name",""));
                                    int level = joResult.get("result").asObject().get("level").asObject().get("id").asInt();
                                    SaveSharedPreferences(mActivity, "level", level);
                                    if (level == 3) {
                                        SaveSharedPreferences(mActivity, "id_satker", joResult.get("result").asObject().get("satker_data").asObject().get("id").asString());
                                        SaveSharedPreferences(mActivity, "satker_name", joResult.get("result").asObject().get("satker_data").asObject().get("name").asString());
                                    }
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetNationalData(final int year) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");
        String id_satker = GetSharedPreferences(mActivity, "id_satker", "");

        String url = "";
        if (id_satker.equals(""))
            url = Config.BASE_API_URL + "api/v1/get_national_data?TA=" + year;
        else
            url = Config.BASE_API_URL + "api/v1/get_national_data?TA=" + year + "&satker_id=" + id_satker;

        Ion.with(mActivity)
            .load("GET", url)
            .setHeader("Content-Type","application/json")
            .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
            .setHeader("app_name", Config.APP_NAME)
            .setHeader("device_id", Config.DEVICE_ID)
            .setHeader("token", token)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if (e == null) {
                        try {
                            JsonObject joResult = Json.parse(result).asObject();
                            if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                            } else {
                                mDataServiceListener.OnFailed(joResult.get("message").asString());
                            }
                        } catch (Exception e1) {
                            mDataServiceListener.OnFailed(e1.getMessage());
                        }
                    } else {
                        mDataServiceListener.OnFailed(e.getMessage());
                    }
                }
            });
        return this;
    }

    public DataService GetActivityDetailData(final String activity_id, final int year) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_activity_detail_data/" + activity_id + "?TA=" + year;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetListOfSatker() {
        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_list_of_satker";

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asArray(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetSatkerData(final String satker_id, final int year) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_satker_data/" + satker_id + "?TA=" + year;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetSatkerInfo(final String satker_id) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_satker_info/" + satker_id;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetSatkerRanking(int year) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_list_of_ranking_satker?TA=" + year;;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetSatkerRanking(int year, String satker_id) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_ranking_satker/" + satker_id + "?TA=" + year;;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asObject(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }

    public DataService GetNotificationList(int year, String group_by) {
        mDataServiceListener.onStart();

        String token = GetSharedPreferences(mActivity, "token", "");

        String url = Config.BASE_API_URL + "api/v1/get_list_of_notification_satker?TA=" + year + "&group_by=" + group_by;;

        Ion.with(mActivity)
                .load("GET", url)
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization", "Bearer " + Config.APP_BEARER_TOKEN)
                .setHeader("app_name", Config.APP_NAME)
                .setHeader("device_id", Config.DEVICE_ID)
                .setHeader("token", token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JsonObject joResult = Json.parse(result).asObject();
                                if (joResult.get("status").asString().equals(RETURN_STATUS_OK)) {
                                    mDataServiceListener.OnSuccess(joResult.get("result").asArray(), joResult.get("message").asString());
                                } else {
                                    mDataServiceListener.OnFailed(joResult.get("message").asString());
                                }
                            } catch (Exception e1) {
                                mDataServiceListener.OnFailed(e1.getMessage());
                            }
                        } else {
                            mDataServiceListener.OnFailed(e.getMessage());
                        }
                    }
                });
        return this;
    }
}
