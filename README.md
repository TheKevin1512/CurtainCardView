# CustomCardView
More material and dynamic than ever before... The CardView of the future!

![](http://i.imgur.com/r6UJ90o.png?1) ![](http://i.imgur.com/hRn7ZIw.png?1) ![](http://i.imgur.com/xE7GmCN.gif)

## Inject your own curtain layout!
```
CurtainCardView curtainCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        this.curtainCard = (CurtainCardView) findViewById(R.id.curtainCardView);


        View curtain = ViewGroup.inflate(this, R.layout.my_layout, null);
        curtainCard.setCurtain(curtain);
    }
```

## Style it the way you want!
Define the 'app' namespace and you're ready to go
```
<dom.studios.view.CurtainCardView
        android:layout_width="match_parent"
        android:layout_height="300dp"
        app:buttonIcon="@drawable/ic_social"
        app:buttonColor="#9E8C37">
    ...
</dom.studios.view.CurtainCardView>

```
## Gradle dependency
Add the following in your **project** builde.gradle:
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
Add the following in your **app** build.gradle:
```
dependencies {
    ...
    compile 'com.github.TheKevin1512:CurtainCardView:1.1.1'
}
```
