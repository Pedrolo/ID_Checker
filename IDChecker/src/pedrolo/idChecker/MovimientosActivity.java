package pedrolo.idChecker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pedrolo.idChecker.adapter.MovimientosAdapter;
import pedrolo.idChecker.data.FiltroMovimientos;
import pedrolo.idChecker.data.Movimiento;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class MovimientosActivity extends Activity{
	private ProgressDialog dialog=null;
	private ListView listaMovimientos=null;
	private DatePicker fechaDesde,fechaHasta=null;
	private Button botBuscar=null;
	
	private String cuentaActual,nombreCuentaActual=null;
	
	private Dialog dialogoFiltro=null;
	private FiltroMovimientos filtroMovimientos=null;
	private TextView labelNombreCuenta,labelDesde,labelHasta=null;
	private List<Movimiento> movimientos=null;
	
	private boolean ordenAscendente=false;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			String text=msg.getData().getString("mensaje");
			if(text==null){
				dialog.hide();
			}else{
				dialog.setMessage(text);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.movimientos);
		
		cuentaActual=getIntent().getStringExtra("numCuenta");
		nombreCuentaActual=getIntent().getStringExtra("nombreCuenta");
				
		listaMovimientos=(ListView)findViewById(R.id.list);
		labelNombreCuenta=(TextView)findViewById(R.id.labelCuenta);
		labelDesde=(TextView)findViewById(R.id.fechaDesde);
		labelHasta=(TextView)findViewById(R.id.fechaHasta);
		
		
		labelNombreCuenta.setText(nombreCuentaActual);
		filtroMovimientos=new FiltroMovimientos();
		initDialogoFiltro();
		labelDesde.setText(filtroMovimientos.getFechaDesde());
		labelHasta.setText(filtroMovimientos.getFechaHasta());
		datosCambiados();
	}
	
	private void datosCambiados(){
		filtroMovimientos.setCuenta(cuentaActual);		
		filtroMovimientos.setDiaDesde(fechaDesde.getDayOfMonth()+"");
		filtroMovimientos.setMesDesde((fechaDesde.getMonth()+1)+"");
		filtroMovimientos.setAñoDesde(fechaDesde.getYear()+"");
		
		filtroMovimientos.actualizaFechaDesde();
		
		filtroMovimientos.setDiaHasta(fechaHasta.getDayOfMonth()+"");
		filtroMovimientos.setMesHasta((fechaHasta.getMonth()+1)+"");
		filtroMovimientos.setAñoHasta(fechaHasta.getYear()+"");
		
		filtroMovimientos.actualizaFechaHasta();
		
		MovimientosAsyncTask task=new MovimientosAsyncTask(handler);
		dialog=ProgressDialog.show(MovimientosActivity.this, "", "Cargando");
		task.execute(filtroMovimientos);
	}
	
	private class MovimientosAsyncTask extends AsyncTask<FiltroMovimientos, Integer, Void>{
		public MovimientosAsyncTask(Handler h) {
			//super(getContentResolver(), h);
		}
		
		@Override
		protected Void doInBackground(FiltroMovimientos... params) {
			movimientos = RequestHandler.getInstance().getMovimientos(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.hide();
			listaMovimientos.setAdapter(new MovimientosAdapter(MovimientosActivity.this, android.R.layout.simple_list_item_2, movimientos));
			labelDesde.setText(filtroMovimientos.getFechaDesde());
			labelHasta.setText(filtroMovimientos.getFechaHasta());
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_movimientos, menu);
	    return true;
	}
	
	private void initDialogoFiltro(){
		if(dialogoFiltro==null){
			dialogoFiltro=new Dialog(MovimientosActivity.this);
			
			dialogoFiltro.setContentView(R.layout.filtro_movimientos);
			dialogoFiltro.setTitle(R.string.movimientos_menu_filtrar);
			
			fechaDesde=(DatePicker)dialogoFiltro.findViewById(R.id.fechaDesde);
			fechaHasta=(DatePicker)dialogoFiltro.findViewById(R.id.fechaHasta);		
			
			Calendar c=GregorianCalendar.getInstance();
			c.add(GregorianCalendar.WEEK_OF_YEAR, -1);
			
			fechaDesde.updateDate(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DAY_OF_MONTH));
			
			botBuscar=(Button)dialogoFiltro.findViewById(R.id.botBuscar);
			botBuscar.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					dialogoFiltro.hide();
					datosCambiados();
				}
			});			
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.recargar:
				dialogoFiltro.show();
				break;
			case R.id.ordenar:
				cambiaOrdenacion();
				break;
			default:
		        return super.onOptionsItemSelected(item);
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	private void cargaMovimientos(){
		dialog.show();
		if(ordenAscendente){
			//Invertimos el orden			
			List<Movimiento> listaOrdenada=new ArrayList<Movimiento>(movimientos.size());
			for(int i=movimientos.size()-1;i>=0;i--){
				listaOrdenada.add(movimientos.get(i));
			}
			listaMovimientos.setAdapter(new MovimientosAdapter(MovimientosActivity.this, android.R.layout.simple_list_item_2, listaOrdenada));
		}else{
			listaMovimientos.setAdapter(new MovimientosAdapter(MovimientosActivity.this, android.R.layout.simple_list_item_2, movimientos));
		}
		dialog.hide();
	}
	
	private void cambiaOrdenacion(){
		ordenAscendente=!ordenAscendente;
		
		cargaMovimientos();
	}
	
}
