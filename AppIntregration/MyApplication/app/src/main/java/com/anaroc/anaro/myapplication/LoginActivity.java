package com.anaroc.anaro.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import contenedores.Parametro;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>, SignUpFragment.OnFragmentInteractionListener {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String email, password;
    private CheckBox checkBox, checkBoxAutoLogIn;
    private boolean auto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.user);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.checkBoxRecordar);
        checkBoxAutoLogIn = (CheckBox) findViewById(R.id.checkBoxAutoLogin);

        SQLite sqlu = new SQLite(this, "BDUsuarios", null, 1);
        SQLiteDatabase db = sqlu.getReadableDatabase();
		/*Se comprueba si hay alguna -una- entrada en la base de datos usada para recordar
		las credenciales del usuario a nivel local*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.color_verde));
        if(db != null)
        {
			/*Si hay un usuario recordado, se introducen sus credenciales
			en los campos de texto correspondientes*/
            String[] columns = {"user", "pass", "flag"};
            Cursor c = db.query("Usuarios", columns, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                mEmailView.setText(c.getString(0));
                mPasswordView.setText(c.getString(1));
                if (c.getInt(2) == 1) auto = true;
                Log.d("aaa", String.valueOf(c.getInt(2)));
                checkBox.setChecked(true);
            }
            c.close();
        }
        assert db != null;
        db.close();


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (isOnline()) attemptLogin();
                    else Toast.makeText(getApplicationContext(), "No hay conectividad de red.",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) attemptLogin();
                else Toast.makeText(getApplicationContext(), "No hay conectividad de red.",Toast.LENGTH_SHORT).show();
            }
        });

        TextView signUp = (TextView) findViewById(R.id.textViewSignUp);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    findViewById(R.id.login_form).setVisibility(View.GONE);
                    findViewById(R.id.login_progress).setVisibility(View.GONE);
                    //setContentView(R.layout.fragment_sign_up);
                    SignUpFragment fragment = SignUpFragment.newInstance("", "");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(android.R.id.content, fragment);
                    ft.commit();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (auto) {
            if (isOnline()) attemptLogin();
            else Toast.makeText(getApplicationContext(), "No hay conectividad de red.",Toast.LENGTH_SHORT).show();
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            new UserLoginTask().execute(new Parametro("consulta", "login"), new Parametro("myName", email),  new Parametro("myPass", password));

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void remember(String user, String pass, int flag) {

        SQLite sqlu = new SQLite(this, "BDUsuarios", null, 1);
        SQLiteDatabase db = sqlu.getWritableDatabase();

		/*Antes de nada, se elimina cualquier registro de la base de datos
		 * para garantizar que solo se recuerda al ultimo usuario
		 */
        db.delete("Usuarios", "ID > 0", null);
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("user", user);
        nuevoRegistro.put("pass", pass);
        nuevoRegistro.put("flag", flag);

        //Insertamos el registro en la base de datos
        db.insert("Usuarios", null, nuevoRegistro);
        db.close();
    }

    //Metodo para dejar de recordar a un usuario
    private void forget() {
        SQLite sqlu = new SQLite(this, "BDUsuarios", null, 1);
        SQLiteDatabase db = sqlu.getWritableDatabase();
        db.delete("Usuarios", "ID > 0", null);
        db.close();

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Parametro, Void, String> {




        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;

            // TODO: register the new account here.

            return respuesta;
           // return false;
        }

        @Override
        protected void onPostExecute(String success) {
            mAuthTask = null;
            showProgress(false);

            if (Integer.parseInt(success.replaceAll("\n",""))>0) {

                if (checkBox.isChecked() || checkBoxAutoLogIn.isChecked()) {
                    if (checkBox.isChecked()) {
                        remember(email, password, 0);
                        Log.d("uno", "uno");

                    }
                    if (checkBoxAutoLogIn.isChecked()){
                        remember(email, password, 1);
                        Log.d("dos", "dos");

                    }
                }
                else forget();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", email);
                intent.putExtra("pass", password);
                intent.putExtra("id", success);


                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



