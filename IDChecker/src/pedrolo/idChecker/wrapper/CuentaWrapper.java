package pedrolo.idChecker.wrapper;

import java.text.NumberFormat;

import android.view.View;
import android.widget.TextView;
import pedrolo.idChecker.R;
import pedrolo.idChecker.data.Cuenta;

public class CuentaWrapper {
	private TextView cuenta,saldo;
	private View base=null;
	
	public CuentaWrapper(View base) {
		this.base=base;
	}
	
	public void fillFromData(Cuenta c){
		getCuenta().setText(c.getNombre());		
		getSaldo().setText(NumberFormat.getCurrencyInstance().format(c.getSaldo())+" €");
	}
	
	private TextView getCuenta(){
		if(cuenta==null){
			cuenta=(TextView)base.findViewById(R.id.labelCuenta);
		}
		return cuenta;
	}
	
	private TextView getSaldo(){
		if(saldo==null){
			saldo=(TextView)base.findViewById(R.id.labelSaldo);
		}
		return saldo;
	}
}
