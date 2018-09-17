package com.example.cupidlove.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cupidlove.R;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kaushal on 04-01-2018.
 */

public class BlockedByAdminFragment extends Fragment {
    //TODO : Variable Declaration
    View view;
    LayoutInflater inflater;
    ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO : Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blocked_by_admin, container, false);

        ButterKnife.bind(this,view);
        ((BaseActivity)getActivity()).settvTitle(getResources().getString(R.string.blocked));
        ((BaseActivity)getActivity()).hideSearch();

        this.inflater = inflater;
        this.container = container;

        return view;
    }

    //TODO: contact us click
    @OnClick(R.id.llContactUs)
    public void llContactUsClicked() {
        String to = Constant.EMAIL;
        String subject = "Blocked Status";
        String message = "Application for UnBlock User.";

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);

        // TODO : need this to prompts email client only
        email.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
//        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
