package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 28-12-2017.
 */

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.ChatMessage;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.stringprep.XmppStringprepException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 17-11-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //TODO : Variable Declaration
    private List<ChatMessage> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    String photo;

    public ChatAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<ChatMessage> list) {
        this.list = list;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }


    public void addMessage(ChatMessage chatMessage) {
        this.list.add(chatMessage);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(list.size());
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View senderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_sender, parent, false);
                return new SenderChatHolder(senderView);
            case 2:
                View receiverView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_receiver, parent, false);
                return new ReceiverChatHolder(receiverView);

            case 3:
                View dateSenderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_date_sender, parent, false);
                return new DateSenderChatHolder(dateSenderView);

            case 4:
                View dateReceiverView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_date_receiver, parent, false);
                return new DateReceiverChatHolder(dateReceiverView);

        }
        View senderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_sender, parent, false);
        return new SenderChatHolder(senderView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 1:
                SenderChatHolder senderChatHolder = (SenderChatHolder) holder;

                if (list.get(position).body.contains("Request Accepted")) {
                    String bodyData = "<html>Request  <font color=\"#60A727\">Accepted</font></html>";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                        ((SenderChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((SenderChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData));
                    }
                } else if (list.get(position).body.contains("Request Declined")) {
                    String bodyData = "<html>Request  <font color=\"#FF5029\"> Declined</font></html>";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                        ((SenderChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((SenderChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData));
                    }
                } else {
                    senderChatHolder.tvChat.setText(list.get(position).body);
                }

                senderChatHolder.tvLastSeen.setText(list.get(position).mtime);
                break;

            case 2:
                ReceiverChatHolder receiverChatHolder = (ReceiverChatHolder) holder;
                receiverChatHolder.tvLastSeen.setText(list.get(position).mtime);

                if (list.get(position).body.contains("Request Accepted")) {
                    String bodyData = "<html>Request  <font color=\"#60A727\">Accepted</font></html>";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                        ((ReceiverChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((ReceiverChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData));
                    }
                } else if (list.get(position).body.contains("Request Declined")) {
                    String bodyData = "<html>Request  <font color=\"#FF5029\"> Declined</font></html>";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                        ((ReceiverChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((ReceiverChatHolder) holder).tvChat.setText(Html.fromHtml(bodyData));
                    }
                } else {
                    receiverChatHolder.tvChat.setText(list.get(position).body);
                }

                break;

            case 3:
                DateSenderChatHolder dateSenderChatHolder = (DateSenderChatHolder) holder;
                String message = list.get(position).body;

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    dateSenderChatHolder.tvRandomColor.setBackgroundColor(color);

                    dateSenderChatHolder.tvPlaceTitle.setText(jsonObject.getString("Title"));
                    dateSenderChatHolder.tvDistance.setText(jsonObject.getString("Distance"));
                    dateSenderChatHolder.tvDateTime.setText(jsonObject.getString("TimeOfDate").replace("\\", ""));
                    dateSenderChatHolder.tvLastSeen.setText(list.get(position).mtime);
                    if (jsonObject.getString("Request").equals("1")) {
                        String bodyData = "<html>Request  </br><font color=\"#60A727\">Accepted</font></html>";
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//
//                            ((DateSenderChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
//                        } else {
//                            ((DateSenderChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData));
//                        }
                    } else if (jsonObject.getString("Request").equals("0")) {
                        String bodyData = "<html>Request  <font color=\"#FF5029\">Declined</font></html>";
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//
//                            ((DateSenderChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
//                        } else {
//                            ((DateSenderChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData));
//                        }
                    } else {
//                        dateSenderChatHolder.tvRequest.setText(jsonObject.getString("Request"));
                    }
                    if (jsonObject.getString("imgPlace") != null && !jsonObject.getString("imgPlace").equals("")) {
                        Picasso.with(activity).load(jsonObject.getString("imgPlace")).error(R.drawable.image_not_found).into(((DateSenderChatHolder) holder).ivImage);
                    }
                } catch (JSONException e) {
                    Log.e("JsonException Adapter", e.getMessage());
                }


                break;

            case 4:
                DateReceiverChatHolder receiverChatHolder1 = (DateReceiverChatHolder) holder;
                String receive = list.get(position).body;


                try {
                    final JSONObject jsonObject = new JSONObject(receive);

                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    receiverChatHolder1.tvRandomColor.setBackgroundColor(color);

                    if (jsonObject.getString("Request").equals("1")) {

                        String bodyData = "<html>Request  <font color=\"#60A727\">Accepted</font></html>";
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//
//
//                            ((DateReceiverChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
//                        } else {
//                            ((DateReceiverChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData));
//                        }
                        ((DateReceiverChatHolder) holder).llAcceptDecline.setVisibility(View.GONE);
//                        ((DateReceiverChatHolder) holder).tvRequest.setVisibility(View.VISIBLE);
                    } else if (jsonObject.getString("Request").equals("0")) {
                        String bodyData = "<html>Request  <font color='#FF5029'>Declined</font></html>";
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//
//                            ((DateReceiverChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
//                        } else {
//                            ((DateReceiverChatHolder) holder).tvRequest.setText(Html.fromHtml(bodyData));
//                        }
                        ((DateReceiverChatHolder) holder).llAcceptDecline.setVisibility(View.GONE);
//                        ((DateReceiverChatHolder) holder).tvRequest.setVisibility(View.VISIBLE);
                    } else {
                        ((DateReceiverChatHolder) holder).llAcceptDecline.setVisibility(View.VISIBLE);
//                        ((DateReceiverChatHolder) holder).tvRequest.setVisibility(View.GONE);
                    }

                    receiverChatHolder1.tvPlaceTitle.setText(jsonObject.getString("Title"));
                    receiverChatHolder1.tvDistance.setText(jsonObject.getString("Distance"));
                    receiverChatHolder1.tvDateTime.setText(jsonObject.getString("TimeOfDate").replace("\\", ""));
                    receiverChatHolder1.tvLastSeen.setText(list.get(position).mtime);
                    photo = jsonObject.getString("imgPlace");
                    if (!photo.isEmpty()) {
                        Picasso.with(activity)
                                .load(photo)
                                .error(R.drawable.image_not_found)
                                .into(((DateReceiverChatHolder) holder).ivImage);
                    }

                    ((DateReceiverChatHolder) holder).tvAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("StarRating", 3);
                                obj.put("imgPlace", jsonObject.getString("imgPlace"));
                                obj.put("TimeOfDate", ((DateReceiverChatHolder) holder).tvDateTime.getText().toString());
                                obj.put("Title", ((DateReceiverChatHolder) holder).tvPlaceTitle.getText().toString());
                                obj.put("Distance", ((DateReceiverChatHolder) holder).tvDistance.getText().toString());
                                obj.put("PlaceId", list.get(position).dateid);
                                obj.put("Request", "1");
                                obj.put("isUpdate", true);
                            } catch (JSONException e) {
                                Log.e("JsonException is ", e.getMessage());
                            }

                            sendMessage(obj.toString(), 4, list.get(position).sender);
                        }
                    });


                    ((DateReceiverChatHolder) holder).tvDecline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("StarRating", 3);
                                obj.put("imgPlace", jsonObject.getString("imgPlace"));

                                obj.put("TimeOfDate", ((DateReceiverChatHolder) holder).tvDateTime.getText().toString());
                                obj.put("Title", ((DateReceiverChatHolder) holder).tvPlaceTitle.getText().toString());
                                obj.put("Distance", ((DateReceiverChatHolder) holder).tvDistance.getText().toString());
                                obj.put("PlaceId", list.get(position).dateid);
                                obj.put("Request", "0");
                                obj.put("isUpdate", true);
                            } catch (JSONException e) {
                                Log.e("JsonException is ", e.getMessage());
                            }

                            sendMessage(obj.toString(), 4, list.get(position).sender);
                        }
                    });

                } catch (JSONException e) {
                    Log.e("JsonException Adapter", e.getMessage());
                }

                break;
        }

    }

    //TODO: Send Message
    public void sendMessage(String message, int type, String toName) {
        String mtime = (String) DateFormat.format("hh:mm aa", Calendar.getInstance().getTime());

        String mdate = (new SimpleDateFormat("MMM d,yyyy")).format(new Date());

        ChatMessage chatMessage = new ChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName,
                message, type, mdate, mtime, null, Prefs.getString(RequestParamUtils.USER_ID, ""), true);
        try {
            ((HomeActivity) activity).getmService().xmpp.sendMessage(chatMessage);
        } catch (XmppStringprepException e) {

            Log.e("XmppStringprepException", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).messagetype == 1) {
            if (list.get(position).sender.equals(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX)) {
                return 1;
            } else {
                return 2;
            }
        } else if (list.get(position).messagetype == 2) {
            if (list.get(position).receiver.equals(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX)) {
                return 2;
            } else {
                return 1;
            }
        }
        if (list.get(position).messagetype == 3) {
            if (list.get(position).sender.equals(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX)) {
                return 3;
            } else {
                return 4;
            }
        } else if (list.get(position).messagetype == 4) {
            if (list.get(position).receiver.equals(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX)) {
                return 4;
            } else {
                return 3;
            }
        }
        return list.get(position).messagetype;
    }


    public class SenderChatHolder extends RecyclerView.ViewHolder {
//
        //TODO: Bind all XML View With JAVA File

        @BindView(R.id.tvChat)
        TextViewRegular tvChat;

        @BindView(R.id.tvLastSeen)
        TextViewRegular tvLastSeen;

        @BindView(R.id.tvRight)
        TextView tvRight;

        @BindView(R.id.ivRight)
        ImageView ivRight;

        public SenderChatHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (Constant.isRtl == 1) {
                tvRight.setBackground(activity.getResources().getDrawable(R.drawable.left_corner_radius));
                ivRight.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_left));
            } else {
                tvRight.setBackground(activity.getResources().getDrawable(R.drawable.right_corner_radius));
                ivRight.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_right));
            }
        }
    }

    //TODO: set receiver view holder
    public class ReceiverChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvChat)
        TextViewRegular tvChat;

        @BindView(R.id.tvLastSeen)
        TextViewRegular tvLastSeen;

        @BindView(R.id.tvleft)
        TextView tvleft;

        @BindView(R.id.ivleft)
        ImageView ivleft;


        public ReceiverChatHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (Constant.isRtl == 1) {
                tvleft.setBackground(activity.getResources().getDrawable(R.drawable.right_corner_radius));
                ivleft.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_right));

            } else {
                tvleft.setBackground(activity.getResources().getDrawable(R.drawable.left_corner_radius));
                ivleft.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_left));
            }
        }

    }

    //TODO : receiver chat holder
    public class DateReceiverChatHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvPlaceTitle)
        TextViewRegular tvPlaceTitle;

        @BindView(R.id.tvDistance)
        TextViewRegular tvDistance;

        @BindView(R.id.tvDateTime)
        TextViewRegular tvDateTime;

        @BindView(R.id.tvLastSeen)
        TextViewRegular tvLastSeen;

        @BindView(R.id.tvAccept)
        TextViewRegular tvAccept;

        @BindView(R.id.tvDecline)
        TextViewRegular tvDecline;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.llAcceptDecline)
        LinearLayout llAcceptDecline;

        @BindView(R.id.tvRandomColor)
        TextView tvRandomColor;

        @BindView(R.id.tvleft)
        TextView tvleft;

        @BindView(R.id.ivleft)
        ImageView ivleft;

        public DateReceiverChatHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (Constant.isRtl == 1) {
                tvleft.setBackground(activity.getResources().getDrawable(R.drawable.right_corner_radius));
                ivleft.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_right));
            } else {
                tvleft.setBackground(activity.getResources().getDrawable(R.drawable.left_corner_radius));
                ivleft.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_left));
            }
        }
    }

    //TODO: sender chat class
    public class DateSenderChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPlaceTitle)
        TextViewRegular tvPlaceTitle;

        @BindView(R.id.tvDistance)
        TextViewRegular tvDistance;

        @BindView(R.id.tvDateTime)
        TextViewRegular tvDateTime;

        @BindView(R.id.tvLastSeen)
        TextViewRegular tvLastSeen;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvRandomColor)
        TextView tvRandomColor;

        @BindView(R.id.tvRight)
        TextView tvRight;

        @BindView(R.id.ivRight)
        ImageView ivRight;

        public DateSenderChatHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (Constant.isRtl == 1) {
                tvRight.setBackground(activity.getResources().getDrawable(R.drawable.left_corner_radius));
                ivRight.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_left));
            } else {
                tvRight.setBackground(activity.getResources().getDrawable(R.drawable.right_corner_radius));
                ivRight.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow_chat_right));
            }

        }
    }


}
