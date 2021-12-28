package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;

import appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.R;


public class Utiilties {

	public Utiilties() {
		// TODO Auto-generated constructor stub
	}

	public static void ShowMessage(Context context, String Title, String Message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(Title);
		alertDialog.setMessage(Message);
		alertDialog.show();
	}

	public static void showAlert(final Context context)
	{

		if (Utiilties.isOnline(context) == false)
		{
			AlertDialog.Builder ab = new AlertDialog.Builder(context);
			ab.setCancelable(false);
			ab.setMessage(Html.fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..\nTo Turn ON Network Connection Press Yes Button else To Continue With Off-Line Mode Press No Button..</font>"));
			ab.setPositiveButton("Turn On Network Connection",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog,
											int whichButton)
						{
							//					GlobalVariables.isOffline = false;
							Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							context.startActivity(I);
						}
					});
			ab.setNegativeButton("Continue Offline",new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog,int whichButton)
				{
					//		GlobalVariables.isOffline = true;
				}
			});

			ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
			ab.show();
		}
		else
		{

			//GlobalVariables.isOffline = false;
			// new CheckUpdate().execute();
		}

	}

	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnected() == true);
	}

	public static Bitmap GenerateThumbnail(Bitmap imageBitmap,
										   int THUMBNAIL_HEIGHT, int THUMBNAIL_WIDTH) {

		Float width = new Float(imageBitmap.getWidth());
		Float height = new Float(imageBitmap.getHeight());
		Float ratio = width / height;
		Bitmap CompressedBitmap = Bitmap.createScaledBitmap(imageBitmap,
				(int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_HEIGHT, false);
		return CompressedBitmap;
	}

	public static Bitmap DrawText(Bitmap mBitmap, String displaytext1,
								  String displaytext2, String displaytext3, String displaytext4) {
		Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Bitmap.Config.ARGB_4444);
		// create a canvas on which to draw
		Canvas canvas = new Canvas(bmOverlay);

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(16);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setFakeBoldText(false);
		paint.setShadowLayer(1, 0, 0, Color.BLACK);

		// if the background image is defined in main.xml, omit this line
		canvas.drawBitmap(mBitmap, 0, 0, paint);

		canvas.drawText(displaytext1, 10, mBitmap.getHeight() - 30, paint);
		canvas.drawText(displaytext2, 10, mBitmap.getHeight() - 10, paint);

		canvas.drawText(displaytext3, 10, mBitmap.getHeight() - 50, paint);

		canvas.drawText(displaytext4, 10, mBitmap.getHeight() - 70, paint);
		// set the bitmap into the ImageView
		return bmOverlay;
	}

	public static Object deserialize(byte[] data) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String getDateString() {
		SimpleDateFormat postFormater = new SimpleDateFormat(
				"MMMM dd, yyyy hh:mm a");

		String newDateStr = postFormater.format(Calendar.getInstance()
				.getTime());
		return newDateStr;
	}

	public static String getDateString(String Formats) {
		SimpleDateFormat postFormater = new SimpleDateFormat(Formats);

		String newDateStr = postFormater.format(Calendar.getInstance()
				.getTime());
		return newDateStr;
	}

}
