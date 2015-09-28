package com.github.oryanmat.trellowidget.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.model.Card;
import com.github.oryanmat.trellowidget.util.DateTimeUtil;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.github.oryanmat.trellowidget.widget.TrelloWidgetProvider;

import in.uncod.android.bypass.Bypass;

import static com.github.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class CardActivity extends AppCompatActivity {
    Card card;
    Bypass bypass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bypass = new Bypass(this);

        setContentView(R.layout.activity_card);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.coordinatorLayout).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        get(this.getIntent().getStringExtra(TrelloWidgetProvider.CARD_EXTRA), new CardListener());
    }

    private void get(String id, CardListener listener) {
        TrelloAPIUtil.instance.getAsync(TrelloAPIUtil.instance.card(id), listener, listener);
    }

    class CardListener implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onResponse(final String response) {
            setCard(Json.tryParseJson(response, Card.class, new Card()));
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(T_WIDGET, error.toString());
            String text = String.format(getString(R.string.board_load_fail), error);
            Toast.makeText(CardActivity.this, text, Toast.LENGTH_LONG).show();
        }
    }

    private void setCard(Card card) {
        this.card = card;

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(card.name);

        TextView due = (TextView) findViewById(R.id.due);
        if (card.due != null) {
            due.setText(DateTimeUtil.parseDateTime(card.due));
        } else {
            due.setVisibility(View.GONE);
        }

        TextView desc = (TextView) findViewById(R.id.desc);
        desc.setText(bypass.markdownToSpannable(card.desc));
        desc.setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.coordinatorLayout).setVisibility(View.VISIBLE);
    }
}
