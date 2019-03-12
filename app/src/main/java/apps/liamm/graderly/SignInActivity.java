package apps.liamm.graderly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static apps.liamm.graderly.utilities.FormValidation.isValidEmail;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private EditText mEmailAddressEditText;
    private EditText mPasswordEditText;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEmailAddressEditText = findViewById(R.id.sign_in_email_input);
        mPasswordEditText = findViewById(R.id.sign_in_password_input);
        mProgressBar = findViewById(R.id.sign_in_progress_bar);
        findViewById(R.id.sign_in_forgot_password).setOnClickListener(this);
        findViewById(R.id.sign_in_sign_up).setOnClickListener(this);
        findViewById(R.id.sign_in_send_request).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            Log.d(TAG, "User is already signed in.");
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_send_request
                && validateInfo()) {
            Log.d(TAG, "Attempting to sign in.");
            attemptSignIn();
        } else if (v.getId() == R.id.sign_in_sign_up) {
            Log.d(TAG, "Attempting to move to sign up activity.");
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            intent.putExtra("email_address", mEmailAddressEditText.getText().toString());
            intent.putExtra("password", mPasswordEditText.getText().toString());
            startActivity(intent);
        } else if (v.getId() == R.id.sign_in_forgot_password) {
            Log.d(TAG, "Attempting to move to forgot password activity.");
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            intent.putExtra("email_address", mEmailAddressEditText.getText().toString());
            startActivity(intent);
        }
    }

    private void attemptSignIn() {
        Log.d(TAG, "Attempting to sign in.");
        mProgressBar.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth.signInWithEmailAndPassword(mEmailAddressEditText.getText().toString(),
                mPasswordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "Successfuly signed in.");
                    Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Log.w(TAG, "Failed to sign in.");
                    Toast.makeText(SignInActivity.this,
                                "Failed to sign in, please try again later.",
                                Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private boolean validateInfo() {
        Log.d(TAG, "Attempting to validate email and password fields.");
        final String email = mEmailAddressEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        boolean valid = true;

        if (!isValidEmail(email)) {
            Log.d(TAG, "Email address is not valid.");
            mEmailAddressEditText.setError("Enter a valid email address.");
            valid = false;
        } else {
            Log.d(TAG, "Email address is valid.");
            mEmailAddressEditText.setError(null);
        }

        if (password.isEmpty()) {
            Log.d(TAG, "Password field is empty.");
            mPasswordEditText.setError("Enter a valid password.");
            valid = false;
        } else {
            Log.d(TAG, "Password is valid.");
            mPasswordEditText.setError(null);
        }

        return valid;
    }
}
