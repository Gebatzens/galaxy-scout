package de.gebatzens.meteva.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import de.gebatzens.meteva.ConfirmDialog;
import de.gebatzens.meteva.GScout;

public class ConfirmDialogAndroid implements ConfirmDialog {

	Activity ac;
	
	public ConfirmDialogAndroid(Activity a) {
		ac = a;
	}
	
	@Override
	public void startConfirmProcess(final ConfirmHandler handler) {
		ac.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				new AlertDialog.Builder(ac)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(GScout.getString("delete_title"))
					.setMessage(GScout.getString("delete_message"))
					.setPositiveButton(GScout.getString("yes"), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.yes();
						}

					})
        .setNegativeButton(GScout.getString("no"), null)
        .show();
			}
			
		});
		
		
	}

	
	
}
