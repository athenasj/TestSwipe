package com.example.makati.testswipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsActivity extends Fragment /*implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener*/ {

    // Empty public constructor, required by the system
    public ContactsActivity() {}

    // A UI Fragment must inflate its View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        /*Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);*/
        return inflater.inflate(R.layout.activity_contacts,
                container, false);
    }


}
