package id.exorty.monira.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.model.User;
import top.wefor.circularanim.CircularAnim;

import static id.exorty.monira.helper.Util.SaveSharedPreferences;

public class LoginActivity extends Activity {
    private User mUser = new User();

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mLoginStatusMessageView;
    private ProgressBar mProgressBar;

    private TextView mAppVersion;

    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        mLoginStatusMessageView = findViewById(R.id.login_status_message);
        mProgressBar = findViewById(R.id.progressBar);
        mAppVersion = findViewById(R.id.appversion);

        mBtnLogin = findViewById(R.id.sign_in_button);
        mBtnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLoginStatusMessageView.setVisibility(View.INVISIBLE);
                                        mBtnLogin.setVisibility(View.INVISIBLE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                        attemptLogin();
//                        CircularAnim.hide(mBtnLogin)
//                                .endRadius(mProgressBar.getHeight() / 2)
//                                .deployAnimator(new CircularAnim.OnAnimatorDeployListener() {
//                                    @Override
//                                    public void deployAnimator(Animator animator) {
//                                    }
//                                }).go();
                    }
                });

        mAppVersion.setText("Version : " + Util.GetAppVersion(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        if (TextUtils.isEmpty(mEmailView.getText())){
            resetLogin(mEmailView, getString(R.string.error_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(mPasswordView.getText())) {
            resetLogin(mPasswordView, getString(R.string.error_field_required));
            return;
        } else if (mPasswordView.getText().length() < 4) {
            resetLogin(mPasswordView, getString(R.string.error_invalid_password));
            return;
        }

        mUser.email = mEmailView.getText().toString();
        mUser.password = mPasswordView.getText().toString();

        if (mUser.status.equals("OK")) {
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CircularAnim.fullActivity(LoginActivity.this, mProgressBar)
                            .go(new CircularAnim.OnAnimationEndListener() {
                                @Override
                                public void onAnimationEnd() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
            }, 3000);
        } else {
            if (Util.HasConnection(this)) {
                Login();
            } else {
                resetLogin(null, "Invalid User Name or Password");
            }
        }
    }

    private void resetLogin(View viewFocus, String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.VISIBLE);

        if (viewFocus != null)
            viewFocus.requestFocus();

        mLoginStatusMessageView.setVisibility(View.VISIBLE);
        mLoginStatusMessageView.setText(errorMessage);
    }

    private void Login(){
        DataService dataService = new DataService(LoginActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                SaveSharedPreferences(LoginActivity.this, "full_name", jsonObject.get("full_name").asString());
                //SaveSharedPreferences(LoginActivity.this, "password", jsonObject.get("password").asString());
                //SaveSharedPreferences(LoginActivity.this, "avatar", jsonObject.get("avatar").asString());
                //SaveSharedPreferences(LoginActivity.this, "role", jsonObject.get("role").asString());

                User user = new User();
                user.email = jsonObject.get("email").asString();
                //user.password = jsonObject.get("password").asString();
                //user.fullName = jsonObject.get("full_name").asString();
                //user.roleID = jsonObject.get("role_id").asInt();

                mUser = user;

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {

            }

            @Override
            public void OnFailed(String message) {
                resetLogin(null, "Invalid User Name or Password");
            }
        }).Login(mUser.email, mUser.password);
    }
}
