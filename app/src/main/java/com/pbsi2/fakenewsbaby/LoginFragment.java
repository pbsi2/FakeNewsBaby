package com.pbsi2.fakenewsbaby;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import static com.pbsi2.fakenewsbaby.R.color.background_material_light;
import static com.pbsi2.fakenewsbaby.R.color.colorAccent;

public class LoginFragment extends Fragment {
    private Intent NewsIntent;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText keyText = view.findViewById(R.id.password_edit_text);
        keyText.setEnabled(true);
        Button nextButton = view.findViewById(R.id.next_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        final CheckBox defaultKeyBox = view.findViewById(R.id.checkbox);
        defaultKeyBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (defaultKeyBox.isChecked()) {
                    MainActivity.okeyText = getString(R.string.default_key);
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("content.guardianapis.com")
                            .appendPath("search")
                            .appendQueryParameter("from-date", MainActivity.startDate)
                            .appendQueryParameter("to-date", MainActivity.endDate)
                            .appendQueryParameter("show-tags", "contributor")
                            .appendQueryParameter("q", "trump")
                            .appendQueryParameter("api-key", MainActivity.okeyText);
                    MainActivity.guardianUrl = builder.build().toString();
                    keyText.setText("");
                    keyText.setEnabled(false);
                    passwordTextInput.setBackgroundColor(getResources().getColor(R.color.interdit, null));
                } else {
                    keyText.setEnabled(true);
                    passwordTextInput.setBackgroundColor(getResources().getColor(background_material_light, null));

                    MainActivity.okeyText = keyText.getText().toString();
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("content.guardianapis.com")
                            .appendPath("search")
                            .appendQueryParameter("from-date", MainActivity.startDate)
                            .appendQueryParameter("to-date", MainActivity.endDate)
                            .appendQueryParameter("show-tags", "contributor")
                            .appendQueryParameter("api-key", MainActivity.okeyText);
                    MainActivity.guardianUrl = builder.build().toString();
                }
            }
        });
        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultKeyBox.isChecked()) {
                    NewsIntent = new Intent(getActivity(), NewsActivity.class);
                    Toast.makeText(getContext(), "Link: " + MainActivity.guardianUrl,
                            Toast.LENGTH_LONG).show();
                    // Start the new activity
                    startActivity(NewsIntent);
                } else {
                    passwordTextInput.setBackgroundColor(getResources().getColor(colorAccent, null));
                    MainActivity.okeyText = keyText.getText().toString();
                    if (MainActivity.okeyText.isEmpty()) {
                        Toast.makeText(getContext(), "You need to enter a valid KEY",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("https")
                                .authority("content.guardianapis.com")
                                .appendPath("search")
                                .appendQueryParameter("from-date", MainActivity.startDate)
                                .appendQueryParameter("to-date", MainActivity.endDate)
                                .appendQueryParameter("show-tags", "contributor")
                                .appendQueryParameter("api-key", MainActivity.okeyText);
                        MainActivity.guardianUrl = builder.build().toString();
                        NewsIntent = new Intent(getActivity(), NewsActivity.class);
                        Toast.makeText(getContext(), "Link: " + MainActivity.guardianUrl,
                                Toast.LENGTH_LONG).show();
                        // Start the new activity
                        startActivity(NewsIntent);
                    }
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    getFragmentManager().popBackStackImmediate();
                } else {
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }
}