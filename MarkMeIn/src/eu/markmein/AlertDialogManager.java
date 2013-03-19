package eu.markmein;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 *               - pass null if you don't want icon
	 * */
	public void showAlertDialog(Context context, String title, String message, Boolean status) {AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	alertDialog.setTitle(title);
	alertDialog.setMessage(message);
	
	if(status != null)
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	alertDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		}
	});
	alertDialog.show();
	}
}