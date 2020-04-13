package capstone.spring20.tscc_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import capstone.spring20.tscc_mobile.Api.ApiController;
import capstone.spring20.tscc_mobile.Api.TSCCClient;
import capstone.spring20.tscc_mobile.Entity.CitizenRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText mName, mEmail, mPhone, mPass, mPassConfirm;
    Button mSubmit;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupBasic();
    }

    private void setupBasic() {
        mName = findViewById(R.id.txtName);
        mEmail = findViewById(R.id.txtEmail);
        mPhone = findViewById(R.id.txtPhone);
        mPass = findViewById(R.id.txtPass);
        mPassConfirm = findViewById(R.id.txtPassConfirm);
        mSubmit = findViewById(R.id.btnSubmit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check all textedit not empty
                if (!checkEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All field must not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check password match
                if (!mPass.getText().toString().equals(mPassConfirm.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Password confirm isn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                //submit
                CitizenRequest citizen = new CitizenRequest();
                citizen.setName(mName.getText().toString());
                citizen.setEmail(mEmail.getText().toString());
                citizen.setPhone(mPhone.getText().toString());
                citizen.setPassword(mPass.getText().toString());

                TSCCClient client = ApiController.getTsccClient();
                Call<String> call = client.register(citizen);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (!result.equals("success")) {
                            Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean checkEmpty() {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        String pass = mPass.getText().toString();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty())
            return false;

        return true;
    }
}
