package com.oryanmat.trellowidget.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.TrelloWidget;
import com.oryanmat.trellowidget.model.BoardList;
import com.oryanmat.trellowidget.model.Card;
import com.oryanmat.trellowidget.util.TrelloAPIUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;
import static java.lang.Float.parseFloat;

public class CardRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String METHOD_SET_TEXT_SIZE = "setTextSize";
    static final String DATE_PARSE_ERROR = "Bad date";
    static final double IMAGE_SCALE = 1.5;

    static final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    static final SimpleDateFormat widgetFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

    private final Context context;
    int appWidgetId;
    BoardList list;
    List<Card> cards = new ArrayList<>();

    public CardRemoteViewFactory(Context context, int appWidgetId) {
        this.context = context;
        this.appWidgetId = appWidgetId;
        list = TrelloWidget.getList(context, appWidgetId);
        // it appears that trello returns dates in UTC time zone
        apiFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void onCreate() {
        Log.d(T_WIDGET, "Factory.onCreate");
    }

    @Override
    public void onDataSetChanged() {
        this.cards = Arrays.asList(TrelloAPIUtil.instance.getCards(list).cards);
        Log.d(T_WIDGET, "Factory.onDataSetChanged: " + cards.size());
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Card card = cards.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.card);

        views.setTextViewText(R.id.card_title, card.name);
        setTextSize(context, views, R.id.card_title, R.dimen.card_title);

        views.setViewVisibility(R.id.desc, card.badges.description ? View.VISIBLE : View.GONE);
        setImage(views, R.id.desc, R.drawable.ic_subject_white_24dp);

        setComments(views, card);
        setChecklist(views, card);
        setDueDate(views, card);
        setAttachments(views, card);

        return views;
    }

    void setDueDate(RemoteViews views, Card card) {
        boolean visible = card.badges.due != null;
        String text = visible ? parseDate(card.badges.due) : "";
        setBadge(views, R.id.due, R.id.due_string,
                R.drawable.ic_access_time_white_24dp, text, visible);
    }

    void setChecklist(RemoteViews views, Card card) {
        String text = String.format("%d/%d", card.badges.checkItemsChecked, card.badges.checkItems);
        boolean visible = card.badges.checkItems > 0;
        setBadge(views, R.id.checklist, R.id.checklist_count,
                R.drawable.ic_check_box_white_24dp, text, visible);
    }

    void setComments(RemoteViews views, Card card) {
        setIntBadge(views, R.id.comments, R.id.comment_count,
                R.drawable.ic_chat_bubble_outline_white_24dp, card.badges.comments);
    }

    void setAttachments(RemoteViews views, Card card) {
        setIntBadge(views, R.id.attachment, R.id.attachment_count,
                R.drawable.ic_attachment_white_24dp, card.badges.attachments);
    }

    void setIntBadge(RemoteViews views, int view, int textView, int imageId, int x) {
        setBadge(views, view, textView, imageId, String.valueOf(x), x > 0);
    }

    void setBadge(RemoteViews views, int view, int textView, int imageId,
                  String text, boolean visible) {
        if (visible) {
            views.setTextViewText(textView, text);
        }

        setVisibility(views, view, textView, visible);
        setTextSize(context, views, textView, R.dimen.card_badges_text);
        setImage(views, view, imageId);
    }

    private void setVisibility(RemoteViews views, int view, int textView, boolean visible) {
        views.setViewVisibility(view, visible ? View.VISIBLE : View.GONE);
        views.setViewVisibility(textView, visible ? View.VISIBLE : View.GONE);
    }

    void setImage(RemoteViews views, int viewId, int imageId) {
        Drawable drawable = ContextCompat.getDrawable(context, imageId);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        float density = context.getResources().getDisplayMetrics().density;
        float prefTextScale = getPrefTextScale(context);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * IMAGE_SCALE * prefTextScale / density),
                (int) (bitmap.getHeight() * IMAGE_SCALE * prefTextScale / density), true);
        views.setImageViewBitmap(viewId, scaledBitmap);
    }

    private String parseDate(String date) {
        try {
            return widgetFormat.format(apiFormat.parse(date));
        } catch (ParseException e) {
            return DATE_PARSE_ERROR;
        }
    }

    public static void setTextSize(Context context, RemoteViews rv, int viewId, int dimenId) {
        rv.setFloat(viewId, METHOD_SET_TEXT_SIZE, getScaledValue(context, dimenId));
    }

    private static float getScaledValue(Context context, int dimenId) {
        float dimension = context.getResources().getDimension(dimenId);
        float density = context.getResources().getDisplayMetrics().density;
        float prefTextScale = getPrefTextScale(context);
        return dimension * prefTextScale / density;
    }

    private static float getPrefTextScale(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return parseFloat(prefs.getString(
                context.getString(R.string.pref_text_size_key),
                context.getString(R.string.pref_text_size_default)));
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}