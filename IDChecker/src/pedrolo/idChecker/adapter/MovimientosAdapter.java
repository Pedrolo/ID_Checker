package pedrolo.idChecker.adapter;

import java.util.List;

import pedrolo.idChecker.R;
import pedrolo.idChecker.data.Movimiento;
import pedrolo.idChecker.wrapper.MovimientoWrapper;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MovimientosAdapter extends ArrayAdapter{
	Activity context;
	private List<Movimiento> list=null; 
	
	public MovimientosAdapter(Activity context, int textViewResourceId,
			List<Movimiento> objects) {
		super(context,  textViewResourceId, objects);
		this.context=context;
		this.list=objects;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row=convertView;
		MovimientoWrapper wrapper=null;
		if(row==null){
			LayoutInflater inflater= context.getLayoutInflater();
			row=inflater.inflate(R.layout.movimiento, null);
			wrapper=new MovimientoWrapper(row);
			row.setTag(wrapper);
		}else{
			wrapper=(MovimientoWrapper)row.getTag();
		}
		Movimiento m=(Movimiento)list.get(position);
		wrapper.fillFromData(m);
		return row;
	}

	public List<Movimiento> getList() {
		return list;
	}

	public void setList(List<Movimiento> list) {
		this.list = list;
	}
}
