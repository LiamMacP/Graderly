package apps.liamm.graderly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import apps.liamm.graderly.storage.FirebaseHelper;

import java.util.Objects;

import static apps.liamm.graderly.utilities.FormValidation.isValidEmail;
import static apps.liamm.graderly.utilities.FormValidation.isValidMobileNumber;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText mForenameEditText;
    private EditText mSurnameEditText;
    private EditText mEmailEditText;
    private EditText mMobileNumberEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mForenameEditText = findViewById(R.id.sign_up_forename_input);
        mSurnameEditText = findViewById(R.id.sign_up_surname_input);
        mEmailEditText = findViewById(R.id.sign_up_email_input);
        mMobileNumberEditText = findViewById(R.id.sign_up_mobile_input);
        mPasswordEditText = findViewById(R.id.sign_up_password_input);
        mConfirmPasswordEditText = findViewById(R.id.sign_up_confirm_password_input);
        mProgressBar = findViewById(R.id.sign_up_progress_bar);

        findViewById(R.id.sign_up_send_request).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mHelper = new FirebaseHelper();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up_send_request) {
            if (verifyUserInformation()
                    && isValidPassword()) {
                signUp();
            }
        }
    }

    private boolean verifyUserInformation() {
        final String forename = mForenameEditText.getText().toString().trim();
        final String surname = mSurnameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();

        if (forename.isEmpty()) {
            mForenameEditText.setError("Forename field cannot be empty.");
            return false;
        } else {
            mForenameEditText.setError(null);
        }

        if (surname.isEmpty()) {
            mSurnameEditText.setError("Surname field cannot be empty.");
            return false;
        } else {
            mSurnameEditText.setError(null);
        }

        if (email.isEmpty()) {
            mEmailEditText.setError("Email field cannot be empty.");
            return false;
        } else if (!isValidEmail(email)) {
            mEmailEditText.setError("Email is not valid.");
            return false;

        } else {
            mEmailEditText.setError(null);
        }

        return true;
    }

    private boolean isValidPassword() {
        final String password = mPasswordEditText.getText().toString().trim();
        final String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if (password.isEmpty()) {
            mPasswordEditText.setError("Password field cannot be empty.");
            return false;
        } else if (password.length() < 6) {
            mPasswordEditText.setError("Password has to be at least 6 characters long.");
            return false;
        } else {
            mPasswordEditText.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            mConfirmPasswordEditText.setError("Confirm password field cannot be empty");
            return false;
        } else {
            mConfirmPasswordEditText.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            mConfirmPasswordEditText.setError("Passwords do not match.");
            return false;
        } else {
            mConfirmPasswordEditText.setError(null);
        }

        return true;
    }

    private void signUp() {
        final String forename = mForenameEditText.getText().toString().trim();
        final String surname = mSurnameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();
        final String mobile = mMobileNumberEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();

        mProgressBar.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (task.isSuccessful()) {
                            if (isValidMobileNumber(mobile)) {
                                mHelper.addUserInfoToDatabase(forename, surname, mobile);
                            } else {
                                mHelper.addUserInfoToDatabase(forename, surname);
                            }

                            Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Failed to create an account, please try again later.", task.getException());

                            Toast.makeText(SignUpActivity.this, "Failed to create an account, please try again later.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
