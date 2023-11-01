package com.github.oryanmat.trellowidget.util

import java.util.concurrent.TimeUnit

object Constants {

    const val T_WIDGET_TAG = "TWidget"

    // Login Config Constants
    const val MAX_LOGIN_FAIL = 3
    private const val DELAY_IN_SEC = 1L
    val DELAY = TimeUnit.SECONDS.toMillis(DELAY_IN_SEC)

    // API Constants
    const val BASE_URL = "https://api.trello.com/"
    const val API_VERSION = "1/"
    private const val APP_KEY = "b250ef70ccf79ea5e107279a91045e6e"
    const val KEY = "&key=$APP_KEY"
    const val AUTH_URL = "$BASE_URL$API_VERSION" + "authorize" +
            "?name=TrelloWidget" +
            KEY +
            "&expiration=never" +
            "&callback_method=fragment" +
            "&return_url=trello-widget://callback"
    const val USER_PATH = "members/me?fields=fullName,username"
    const val BOARDS_PATH = "members/me/boards?filter=open&fields=id,name,url&lists=open"
    const val LIST_CARDS_PATH = "lists/%s?cards=open&card_fields=name,badges,labels,url"

    // Preferences Constants
    const val INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs"
    const val TOKEN_PREF_KEY = "com.oryanmat.trellowidget.usertoken"
    const val LIST_KEY = ""
    const val BOARD_KEY = ".board"

    // Trello Constants
    const val TRELLO_PACKAGE_NAME = "com.trello"
    const val TRELLO_URL = "https://www.trello.com"
}

