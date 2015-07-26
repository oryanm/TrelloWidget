package com.oryanmat.trellowidget.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;
import static java.lang.Float.parseFloat;

public class CardRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String METHOD_SET_TEXT_SIZE = "setTextSize";

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
        views.setViewVisibility(R.id.desc, card.badges.description ? View.VISIBLE : View.GONE);
        setComments(card, views);
        setChecklist(card, views);
        setDueDate(card, views);
        setAttachments(card, views);
        setTextSize(context, views, R.id.card_title, R.dimen.card_title);
        setTextSize(context, views, R.id.comment_count, R.dimen.card_badges);
        setTextSize(context, views, R.id.checklist_count, R.dimen.card_badges);
        setTextSize(context, views, R.id.due_string, R.dimen.card_badges);
        setTextSize(context, views, R.id.attachment_count, R.dimen.card_badges);
        return views;
    }

    private void setAttachments(Card card, RemoteViews views) {
        if (card.badges.attachments > 0) {
            views.setViewVisibility(R.id.attachment, View.VISIBLE);
            views.setTextViewText(R.id.attachment_count, String.valueOf(card.badges.attachments));
            views.setViewVisibility(R.id.attachment_count, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.attachment, View.GONE);
            views.setViewVisibility(R.id.attachment_count, View.GONE);
        }
    }

    private void setDueDate(Card card, RemoteViews views) {
        if (card.badges.due != null) {
            views.setViewVisibility(R.id.due, View.VISIBLE);
            Date date = Calendar.getInstance().getTime();
            try {
                date = apiFormat.parse(card.badges.due);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            views.setTextViewText(R.id.due_string, widgetFormat.format(date));
            views.setViewVisibility(R.id.due_string, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.due, View.GONE);
            views.setViewVisibility(R.id.due_string, View.GONE);
        }
    }

    private void setComments(Card card, RemoteViews views) {
        if (card.badges.comments > 0) {
            views.setViewVisibility(R.id.comments, View.VISIBLE);
            views.setTextViewText(R.id.comment_count, String.valueOf(card.badges.comments));
            views.setViewVisibility(R.id.comment_count, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.comments, View.GONE);
            views.setViewVisibility(R.id.comment_count, View.GONE);
        }
    }

    private void setChecklist(Card card, RemoteViews views) {
        if (card.badges.checkItems > 0) {
            views.setViewVisibility(R.id.checklist, View.VISIBLE);
            views.setTextViewText(R.id.checklist_count,
                    String.format("%d/%d", card.badges.checkItemsChecked, card.badges.checkItems));
            views.setViewVisibility(R.id.checklist_count, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.checklist, View.GONE);
            views.setViewVisibility(R.id.checklist_count, View.GONE);
        }
    }

    public static void setTextSize(Context context, RemoteViews rv, int viewId, int dimenId) {
        rv.setFloat(viewId, METHOD_SET_TEXT_SIZE, getScaledValue(context, dimenId));
    }

    private static float getScaledValue(Context context, int dimenId) {
        float resValue = getDimension(context, dimenId);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        float density = context.getResources().getDisplayMetrics().density;
        float prefTextScale = parseFloat(prefs.getString(
                context.getString(R.string.pref_text_size_key),
                context.getString(R.string.pref_text_size_default)));
        return resValue * prefTextScale / density;
    }

    private static float getDimension(Context context, int dimenId) {
        try {
            return context.getResources().getDimension(dimenId);
        } catch (Resources.NotFoundException e) {
            // resource might not exist
            return 0f;
        }
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