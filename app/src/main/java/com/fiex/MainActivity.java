package com.fiex;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener2 {
	public int REQ_EXTERNAL_STORAGE = 12;
	public RecyclerView rec;
	public FiAdapter adp;
	List<File> list;
	static int position;
	public String action, cPath;
	AlertDialog.Builder ad, rf;
	TextView txtHead, txtMsg, txtFnm;
	ImageButton iOk, iCancel;
	MainActivity ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rec = findViewById(R.id.recyclerView);
		list = new ArrayList<>();
		rec.setLayoutManager(new LinearLayoutManager(this));
		if (this.getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
				this.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
			cPath = Environment.getExternalStorageDirectory().toString();
			list = getAll();
			ma = MainActivity.this;
			adp = new FiAdapter(list, MainActivity.this);
			rec.setAdapter(adp);
		} else {
			requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQ_EXTERNAL_STORAGE);
		}
	}

	public List<File> getAll() {
		List<File> list;
		File director = new File(cPath);
		File[] files = director.listFiles();
		list = Arrays.asList(files);
		return list;
	}

	@Override
	public boolean onContextItemSelected(MenuItem arg0) {
		action = arg0.getTitle().toString();
		Toast.makeText(this, position + "-?", Toast.LENGTH_SHORT).show();
		if (action.equals("Delete")) {
			ad = new AlertDialog.Builder(this);
			View c = LayoutInflater.from(this).inflate(R.layout.layout_alert_dialog_custom, null);
			txtHead = c.findViewById(R.id.ad_header);
			txtMsg = c.findViewById(R.id.ad_msg);
			txtHead.setText("Are you sure to " + action);
			txtMsg.setText(list.get(position).getAbsolutePath());
			ad.setView(c);
			final int pos = position;
			ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
					File df = new File(list.get(pos).getAbsolutePath());
					if (df.isFile()) {
						if (df.delete()) {
							List<File> newList = getAll();
							adp = new FiAdapter(newList, ma);
							rec.setAdapter(adp);
							adp.notifyDataSetChanged();
						}
					}
				}

			});
			ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			ad.show();
		} else {
			Toast.makeText(this, position + "<-", Toast.LENGTH_SHORT).show();
			rf = new AlertDialog.Builder(this);
			View cc = LayoutInflater.from(this).inflate(R.layout.layout_rename_file, null);
			txtFnm = cc.findViewById(R.id.txt_file_name);
			iCancel = cc.findViewById(R.id.id_cancel);
			iOk = cc.findViewById(R.id.id_ok);
			final int pos = position;
			txtFnm.setText(list.get(pos).getName());
			rf.setView(cc);
			final AlertDialog aa = rf.show();
			iOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final File file1 = new File(list.get(pos).getAbsolutePath());
					final String nNm = txtFnm.getText().toString();
					final File file2 = new File(list.get(pos).getParent() + "/" + nNm);
					if (file1.isFile() && nNm != null && nNm.length() > 0 && !nNm.equals(file1.getName())) {
						if (file1.renameTo(file2)) {
							aa.dismiss();
							List<File> newList = getAll();
							adp = new FiAdapter(newList, ma);
							rec.setAdapter(adp);
							adp.notifyDataSetChanged();

							//	adp.notifyItemChanged(pos);
						}
					}
				}
			});
			iCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					aa.dismiss();
				}
			});

		}
		return super.onContextItemSelected(arg0);
	}

	@Override
	public void onClick2(View v, int ps) {
		position = ps;

		//	Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();

	}
}