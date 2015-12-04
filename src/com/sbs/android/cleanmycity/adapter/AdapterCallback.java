package com.sbs.android.cleanmycity.adapter;

public interface AdapterCallback {
	public void callTask(String task, Object[] parameter);
	public boolean checkStatus(String task, String[] parameter);
}
