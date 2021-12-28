package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util.Utiilties;


public class HomeActivity extends Activity {
    ImageView slidingimage;
    RelativeLayout relativeLayout;
    Button btnskip;
    private ArrayList<String> mediatype = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        btnskip=(Button)findViewById(R.id.buttonskip);

        LoadPosterForAnimation();
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utiilties.isOnline(HomeActivity.this) == false)
                {
                    Intent mainIntent = new Intent(HomeActivity.this, GalleryWebActivity.class);
                    startActivity(mainIntent);
                    HomeActivity.this.finish();
                }
                else {

                    AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this);
                    ab.setCancelable(false);
                    ab.setMessage(Html
                            .fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection</font>"));
                    ab.setPositiveButton("Turn On Network Connection",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    //GlobalVariables.isOffline = false;
                                    Intent I = new Intent(
                                            android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(I);
                                }
                            });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }

            }
        });

    }

    public void LoadPosterForAnimation() {
        final Handler mHandler = new Handler();
        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                AnimateandSlideShow();
            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 30000; // repeat every 8 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);
            }
        }, delay, period);

    }


    private void AnimateandSlideShow() {
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        slidingimage = (ImageView) findViewById(R.id.imgHome);

       // relativeLayout = (RelativeLayout) findViewById(R.id.rel_home_main);
       // relativeLayout.startAnimation(rotateimage);

        slidingimage.startAnimation(rotateimage);
    }
}
