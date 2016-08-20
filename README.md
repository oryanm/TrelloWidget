# Widget for Trello
Widget for Trello is an android home screen widget.  
Choose and display lists from your Trello boards on your home screen for a quick glance at your cards.

<a href='https://play.google.com/store/apps/details?id=com.oryanmat.trellowidget'>
  <img alt='Get it on Google Play'  width="256" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/>
</a>

## Tools and Tech
* [Trello's API](http://trello.com/docs)
* [Volly](https://android.googlesource.com/platform/frameworks/volley.git) - http handling
* [Gson](http://sites.google.com/site/gson/) - json parsing
* [Holo ColorPicker](http://github.com/LarsWerkman/HoloColorPicker) - color picker dialog
* [Calendar Widget](http://github.com/plusonelabs/calendar-widget) - inspiration

## How to Build
Building should be smooth using android studio and gradle, 
but since Volley is not (officially) available as a gradle dependency we need to [clone and import it as a separate module] (https://developer.android.com/training/volley/index.html).