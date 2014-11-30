package de.gebatzens.meteva.android;

import android.os.Debug;
import de.gebatzens.meteva.TraceInterface;

public class AndroidTrace implements TraceInterface {

	@Override
	public void beginTrace(String name) {
		Debug.startMethodTracing(name);

	}

	@Override
	public void endTrace() {
		Debug.stopMethodTracing();

	}

}
