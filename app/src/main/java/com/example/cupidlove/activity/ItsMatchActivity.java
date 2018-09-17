package com.example.cupidlove.activity;

import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewItsMatch;
import com.example.cupidlove.model.Notification;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.xmpp.MyXMPP;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ItsMatchActivity extends BaseActivity {

    //TODO : Bind all XML View With JAVA file

    @BindView(R.id.civMatchProfileImage)
    CircleImageView civMatchProfileImage;

    @BindView(R.id.civMyProfileImage)
    CircleImageView civMyProfileImage;

    @BindView(R.id.tvMatchName)
    TextViewItsMatch tvMatchName;

    //TODO : VAriable Declaration
    Notification.Body notificationBodyRider;
    private String Tag = "xmpp";

    Roster roster = null;

    String fname, lname, friendId, profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_its_match);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        if (MyXMPP.connection != null) {
            roster = Roster.getInstanceFor(MyXMPP.connection);
        }

//        getRosterListner();
        fname = getIntent().getStringExtra(RequestParamUtils.FRIEND_FIRST_NAME);
        lname = getIntent().getStringExtra(RequestParamUtils.FRIEND_LAST_NAME);
        friendId = getIntent().getStringExtra(RequestParamUtils.FRIEND_USER_ID);
        profile_picture = getIntent().getStringExtra(RequestParamUtils.FRIEND_PROFILE_PICTURE);
        Constant.EJUSERID = getIntent().getStringExtra(RequestParamUtils.FRIEND_EJABBERED_ID);
        Constant.FRIEND_FIRST_NAME = fname;
        Constant.FRIEND_ID = friendId;
        Constant.FRIEND_LAST_NAME = lname;
        Constant.FRIEND_PROFILE_PICTURE = profile_picture;

        Picasso.with(this).load(new URLS().UPLOAD_URL + profile_picture).error(R.drawable.side_menu_logo).into(civMatchProfileImage);
        Picasso.with(this).load(new URLS().UPLOAD_URL + getPreferences().getString(RequestParamUtils.PROFILE_IMAGE, "")).error(R.drawable.image_not_found).into(civMyProfileImage);

        tvMatchName.setText(fname + "");

        if (roster != null) {
            createRosterEntry();
        }


    }


    //TODO: Click On Keep Playing
    @OnClick(R.id.llKeepPlaying)
    public void llKeepPlayingClicked() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //TODO: Click On Send Message
    @OnClick(R.id.llSendMessage)
    public void llSendMessageClicked() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(RequestParamUtils.DATA, "chat");
        startActivity(intent);
    }

    //TODO: Create Roster For send And Receive Message
    private void createRosterEntry() {


        if (!roster.isLoaded()) {
            try {
//                roster.getPresence()

                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                Log.e(Tag + "SmackException", e.getMessage());
            } catch (SmackException.NotConnectedException e) {
                Log.e(Tag + "NotConnectedException", e.getMessage());
            } catch (InterruptedException e) {
                Log.e(Tag + "InterruptedException", e.getMessage());
            }
        }
        try {
            roster.createEntry(JidCreate.bareFrom(friendId + "_" + fname + Constant.NAME_POSTFIX), fname, null);
            Toast.makeText(ItsMatchActivity.this, "Roster Created", Toast.LENGTH_LONG).show();
            Log.e(Tag + "appRoster", "roster created");
        } catch (SmackException.NotLoggedInException e) {
            Log.e(Tag + "NotLoggedInException", e.getMessage());
        } catch (SmackException.NoResponseException e) {
            Log.e(Tag + "NoResponseException", e.getMessage());
        } catch (XMPPException.XMPPErrorException e) {
            Log.e(Tag + "XMPPErrorException", e.getMessage());
        } catch (SmackException.NotConnectedException e) {
            Log.e(Tag + "NotConnectedException", e.getMessage());
        } catch (InterruptedException e) {
            Log.e(Tag + "InterruptedException", e.getMessage());
        } catch (XmppStringprepException e) {
            Log.e(Tag + "XmppStringprepException", e.getMessage());
        }

        Collection<RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {
            Log.e("Roster Entry is ", entry.getName() + " ");
        }

    }


}
