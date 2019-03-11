package apps.liamm.graderly;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static apps.liamm.graderly.utilities.FormValidation.isValidEmail;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    private EditText mEmailEditView;
    private ProgressBar mProgressBar;
    private TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.forgot_password_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.forgot_password_send_request).setOnClickListener(this);
        mEmailEditView = findViewById(R.id.forgot_password_email_input);
        mProgressBar = findViewById(R.id.forgot_password_progress_bar);
        mStatusTextView = findViewById(R.id.forgot_password_status);

        mEmailEditView.setText(getIntent().getStringExtra("email_address"));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forgot_password_send_request
                && validateEmail()) {
                requestPasswordRecovery();
        }
    }

    private boolean validateEmail() {
        Log.d(TAG, "Validating email address.");

        final String email = mEmailEditView.getText().toString();

        // Checks to make sure that the email address is neither empty and matches a valid email.
        if (!isValidEmail(email)) {
            Log.d(TAG, "Email address is not valid.");
            mEmailEditView.setError("Enter a valid email address.");
            return false;
        }

        Log.d(TAG, "Email address is valid.");
        mEmailEditView.setError(null);
        return true;
    }

    private void requestPasswordRecovery() {
        Log.d(TAG, "Attempting to send recovery email.");
        mStatusTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FirebaseAuth.getInstance().sendPasswordResetEmail(mEmailEditView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "Successfuly sent recovery email.");
                    mStatusTextView.setText(R.string.forgot_password_success);
                } else {
                    Log.w(TAG, "Failed to send recovery email.");
                    mStatusTextView.setText(R.string.forgot_password_failure);
                }
                mStatusTextView.setVisibility(View.VISIBLE);
            }
        });
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
}
