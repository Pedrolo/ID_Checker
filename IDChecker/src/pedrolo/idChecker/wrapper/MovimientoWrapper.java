package pedrolo.idChecker.wrapper;

import java.text.NumberFormat;

import pedrolo.idChecker.R;
import pedrolo.idChecker.data.Movimiento;
import android.view.View;
import android.widget.TextView;

public class MovimientoWrapper {
	private TextView importe,concepto,fechaOperacion,fechaValor,saldo;
	private View base=null;
	
	public MovimientoWrapper(View base) {
		this.base=base;
	}
	
	public void fillFromData(Movimiento m){
		getImporte().setText(NumberFormat.getCurrencyInstance().format(m.getImporte()));
		getSaldo().setText(NumberFormat.getCurrencyInstance().format(m.getSaldo()));
		getConcepto().setText(m.getConcepto());
		getFechaOperacion().setText(m.getFechaOperacion());
		getFechaValor().setText(m.getFechaValor());
	}
	
	public TextView getImporte() {
		if(importe==null){
			importe=(TextView)base.findViewById(R.id.importe);
		}
		return importe;
	}
	public TextView getConcepto() {
		if(concepto==null){
			concepto=(TextView)base.findViewById(R.id.concepto);
		}
		return concepto;
	}
	public TextView getFechaOperacion() {
		if(fechaOperacion==null){
			fechaOperacion=(TextView)base.findViewById(R.id.fechaOperacion);
		}
		return fechaOperacion;
	}
	public TextView getFechaValor() {
		if(fechaValor==null){
			fechaValor=(TextView)base.findViewById(R.id.fechaValor);
		}
		return fechaValor;
	}
	public TextView getSaldo() {
		if(saldo==null){
			saldo=(TextView)base.findViewById(R.id.saldo);
		}
		return saldo;
	}
	
}
