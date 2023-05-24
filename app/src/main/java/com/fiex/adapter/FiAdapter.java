package com.fiex.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.MainThread;
import com.fiex.MainActivity;
import com.fiex.R;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.fiex.adapter.FiAdapter.FiVH;
import com.fiex.model.Fi;
import java.io.File;
import java.util.List;

public class FiAdapter extends RecyclerView.Adapter<FiAdapter.FiVH> {
	List<File> list;
	MainActivity act;

	public FiAdapter(List<File> list, MainActivity act) {
		this.list = list;
		this.act = act;
	}

	public class FiVH extends RecyclerView.ViewHolder
			implements View.OnLongClickListener, View.OnCreateContextMenuListener {
		TextView txtFiName;
		ImageView imgFi;

		public FiVH(View v) {
			super(v);
			txtFiName = v.findViewById(R.id.txtFiName);
			imgFi = v.findViewById(R.id.imgFi);
			v.setOnLongClickListener(this);
			v.setOnCreateContextMenuListener(this);
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			View vv = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_custom_context_menu_header, null);
			menu.setHeaderView(vv);
			MenuItem mItem = menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title
			MenuItem mItem2 = menu.add(0, v.getId(), 0, "Rename");
		}

		@Override
		public boolean onLongClick(View arg0) {
			act.onClick(arg0, getAdapterPosition());
			arg0.showContextMenu();
			return true;
		}

	}

	@Override
	public FiVH onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.layout_fi_item, arg0, false);
		return new FiVH(v);
	}

	@Override
	public void onBindViewHolder(FiVH arg0, int arg1) {
		arg0.txtFiName.setText(list.get(arg1).getName());

		if (list.get(arg1).isDirectory())
			arg0.imgFi.setImageResource(R.drawable.ic_folder);
		else
			arg0.imgFi.setImageResource(R.drawable.ic_anonymous_file);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}