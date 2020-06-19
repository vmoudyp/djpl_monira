package id.exorty.monira.ui;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.Calendar;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.model.User;
import top.wefor.circularanim.CircularAnim;

import static id.exorty.monira.helper.Util.GetSharedPreferences;
import static id.exorty.monira.helper.Util.SaveSharedPreferences;

public class LoginActivity extends Activity {
    private User mUser = new User();

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mLoginStatusMessageView;
    private ProgressBar mProgressBar;

    private TextView mAppVersion;

    private Button mBtnLogin;

    private int mLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        mLoginStatusMessageView = findViewById(R.id.login_status_message);
        mLoginStatusMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(LoginActivity.this);
                editText.setText(v.getTag().toString());
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login Error")
                        .setView(editText)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
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
            resetLogin(mEmailView, getString(R.string.error_invalid_email), "");
            return;
        }

        if (TextUtils.isEmpty(mPasswordView.getText())) {
            resetLogin(mPasswordView, getString(R.string.error_field_required), "");
            return;
        } else if (mPasswordView.getText().length() < 4) {
            resetLogin(mPasswordView, getString(R.string.error_invalid_password), "");
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
                resetLogin(null, "Invalid User Name or Password", "");
            }
        }
    }

    private void resetLogin(View viewFocus, String errorMessage, String detailMessage) {
        mProgressBar.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.VISIBLE);

        if (viewFocus != null)
            viewFocus.requestFocus();

        mLoginStatusMessageView.setVisibility(View.VISIBLE);
        mLoginStatusMessageView.setTag(detailMessage);
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

                initApp();
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {

            }

            @Override
            public void OnFailed(String message, String fullMessage) {
                resetLogin(null, message, fullMessage);
            }

        }).Login(mUser.email, mUser.password);
    }

    private void initApp(){
        DataService dataService = new DataService(LoginActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void OnFailed(String message, String fullMessage) {
                if (mLoop < 3) {
                    mLoop++;
                    initApp();
                } else {
                    mLoop = 0;
                    Toast.makeText(LoginActivity.this,
                            "Failed to get data from server. Please contact admin : " + message, Toast.LENGTH_SHORT).show();
                }
            }
        }).GetListOfSatker();
    }
}
