package shafir.irena.locationaware;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);


    }

//    @OnTextChanged(R.id.etName)
//    public void etChanged(CharSequence text){
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//    }



   @OnEditorAction(R.id.etName)
    public boolean etAction(TextView textView, int actionCode, KeyEvent e) {
       if (etName.getText().length() < 2) {
           etName.setError("Invalid name...");
           return true;
       }
           FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()){
                      //save the user name and go to
                      String userName = etName.getText().toString();

                      // 1. ref to users table  - > uid current user
                      String uid = task.getResult().getUser().getUid();
                        // another option to do it is through the FirebaseAuth
                      //FirebaseAuth.getInstance().getCurrentUser().getUid();

                      // 2. ref. setValue(userName)
                      DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
                      ref.setValue(userName);

                      //go to maps activity
                      Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                  }
                  else {
                      // get the exception
                      Exception e = task.getException();
                      // test that the exception is not null
                      if (e != null){

                          // show error
                          Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
                      }
                  }
               }
           });
           return true;

   }


}
