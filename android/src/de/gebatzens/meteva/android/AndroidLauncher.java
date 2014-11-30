package de.gebatzens.meteva.android;

import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.gebatzens.meteva.GScout;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		GScout m = new GScout();
		m.tracei = new AndroidTrace();
		GScout.confDia = new ConfirmDialogAndroid(this);
		initialize(m, config);
	}
}
