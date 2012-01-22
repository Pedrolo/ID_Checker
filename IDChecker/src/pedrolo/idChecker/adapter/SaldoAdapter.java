package pedrolo.idChecker.adapter;

import java.util.List;

import pedrolo.idChecker.R;
import pedrolo.idChecker.data.Cuenta;
import pedrolo.idChecker.wrapper.CuentaWrapper;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SaldoAdapter extends ArrayAdapter{
	private Activity context;
	private List<Cuenta> list=null; 
	
	public SaldoAdapter(Activity context, int textViewResourceId,
			List<Cuenta> objects) {
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
		CuentaWrapper wrapper=null;
		if(row==null){
			LayoutInflater inflater= context.getLayoutInflater();
			row=inflater.inflate(R.layout.saldo_cuenta, null);
			wrapper=new CuentaWrapper(row);
			row.setTag(wrapper);
		}else{
			wrapper=(CuentaWrapper)row.getTag();
		}
		Cuenta c=(Cuenta)list.get(position);
		wrapper.fillFromData(c);
		return row;
	}
	
	public List<Cuenta> getCuentas(){
		return list;
	}
	
	public String[] getNombresCuentas(){
		String[] nombres=new String[list.size()];
		for(int i=0;i<list.size();i++){
			nombres[i]=list.get(i).getNombre();
		}
		return nombres;
	}
	
	public String[] getNumerosCuentas(){
		String[] numeros=new String[list.size()];
		for(int i=0;i<list.size();i++){
			numeros[i]=list.get(i).getNumero();
		}
		return numeros;
	}
}
