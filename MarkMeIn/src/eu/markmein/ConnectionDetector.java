package eu.markmein;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context){
		this._context = context;
	}

	/**
	 * Checking for all possible Internet providers
	 * **/
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.e("conn", connectivity.toString());
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			Log.e("info", info.toString());
			if (info != null){
				for (int i = 0; i < info.length; i++){
					Log.e("info-state", info[i].getState().toString());
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
