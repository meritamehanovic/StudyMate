package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SettingsActivity extends AppCompatActivity {
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private ImageView chatButton , matchingButton , calendarButton, friendsListButton;
    private User user;

    private ConstraintLayout mainLayout;
    private ImageView changeBackgroundButton;
    private int currentBackgroundIndex = 0;
    private int[] backgroundResources = {
            R.drawable.background_gradient,
            R.drawable.background_gradient_other,
            R.drawable.background_gradient_second,
            R.drawable.background_gradient_third
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        mainLayout = findViewById(R.id.main_layout);
        changeBackgroundButton = findViewById(R.id.changeBackgroundButton);


        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();

        changeBackgroundButton.setOnClickListener(v -> {
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundResources.length;
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putInt("backgroundIndex", currentBackgroundIndex).apply();
            applyBackground();
        });






        // getting user
        user = getUserFromIntent();
        // TODO: When the other views are finished, the user needs to be send to the other
        // TODO: views. Otherwise, user object is lost.
        chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        calendarButton = findViewById(R.id.calendarButton);
        //Hey, I've changed it so that it would lead to calendar
        // hope, you don't mind
        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CalendarView.class);
            intent.putExtra(USER_MATCHING_ALGO_STRING, user);
            startActivity(intent);
        });

        matchingButton = findViewById(R.id.matchingButton);
        matchingButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MatchingAlgorithm.class);
            startActivity(intent);
        });

        friendsListButton = findViewById(R.id.friendsListButton);
        friendsListButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, FriendListActivity.class);
            startActivity(intent);
        });



    }

private void applyBackground() {
    Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
    mainLayout.setBackground(background);
}
    private User getUserFromIntent() {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra(MainActivity.USER_MATCHING_ALGO_STRING);
        if (user == null) {
            user = setFailSafeUser();
        }
        return user;
    }

    private User setFailSafeUser() {
        String name = "failSafeUser";
        Collection<Tag> tags = new ArrayList<>(Arrays.asList(Tag.ERSTI, Tag.HCI));
        University uni = University.UNI_WIEN;
        String email = "failsafe@example.com";
        return new User(name, uni, tags, email);
    }
}

