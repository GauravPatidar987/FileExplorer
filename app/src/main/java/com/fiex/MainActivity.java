package com.fiex;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fiex.adapter.FiAdapter;
import com.fiex.interfaces.OnClickListener2;
import com.fiex.model.Fi;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener2 {
	public int REQ_EXTERNAL_STORAGE = 12;
	RecyclerView rec;
	List<File> list;
	int position;
	AlertDialog.Builder ad;
	TextView txtHead, txtMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rec = findViewById(R.id.recyclerView);
		list = new ArrayList<>();
		rec.setLayoutManager(new LinearLayoutManager(this));
		if (this.getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
				this.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
			String path = Environment.getExternalStorageDirectory().toString();
			File directory = new File(path);
			File[] files = directory.listFiles();
			list = Arrays.asList(files);
			rec.setAdapter(new FiAdapter(list, this));
		} else {
			requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQ_EXTERNAL_STORAGE);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem arg0) {
		txtHead.setText("Are you sure to " + arg0.getTitle());
		ad.show();
		return super.onContextItemSelected(arg0);
	}

	@Override
	public void onClick2(View v, int position) {
		position = position;
		{
			Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
			ad = new AlertDialog.Builder(this);
			View c = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_alert_dialog_custom, null);
			txtHead = c.findViewById(R.id.ad_header);
			txtMsg = c.findViewById(R.id.ad_msg);
			txtMsg.setText(list.get(position).getAbsolutePath());
			ad.setView(c);
			ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
		}

	}
}