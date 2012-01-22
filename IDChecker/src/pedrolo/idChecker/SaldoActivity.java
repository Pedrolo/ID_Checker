package pedrolo.idChecker;

import java.util.List;

import pedrolo.idChecker.adapter.SaldoAdapter;
import pedrolo.idChecker.data.Cuenta;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SaldoActivity extends ListActivity {
	private ProgressDialog dialog=null;
	private RequestHandler requestHandler=null;
	private TextView empty=null;
	private Integer clickedPosition=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.saldo_cuentas);
			
			this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			this.getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					clickedPosition=arg2;
					abreMovimientos(clickedPosition);
				}
			});
	
			if(requestHandler==null){
				requestHandler=RequestHandler.getInstance();
			}
			try{
				dialog = ProgressDialog.show(this, "","Cargando", true);
				//Cargamos
				new CuentasParser().execute(null);			
			}catch(Exception e){
				
			}
		}catch(Exception e){
			Log.e("SaldoActivity", "onCreate",e);
		}
	}
	
	private void abreMovimientos(int pos){
		Cuenta c=(Cuenta)((SaldoAdapter)getListAdapter()).getItem(pos);
		Intent intent=null;
    	intent = new Intent(Constants.MOVIMIENTOS);
    	intent.putExtra("numCuenta", c.getNumero());
    	intent.putExtra("nombreCuenta", c.getNombre());
    	    	
    	String[] nombresCuentas=((SaldoAdapter)getListAdapter()).getNombresCuentas();
    	String[] numerosCuentas=((SaldoAdapter)getListAdapter()).getNumerosCuentas();
    	intent.putExtra("nombresCuentas",nombresCuentas);
    	intent.putExtra("numerosCuentas",numerosCuentas);
        startActivity(intent);
	}
	
	class CuentasParser extends AsyncTask<Void, Void, Void>{
		private List<Cuenta> cuentas = null;

		@Override
		protected Void doInBackground(Void... params) {
			this.cuentas=RequestHandler.getInstance().listCuentas();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(new SaldoAdapter(SaldoActivity.this, android.R.layout.simple_list_item_2,cuentas));
			dialog.hide();
		}
	}
}
