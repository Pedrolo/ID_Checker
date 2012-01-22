package pedrolo.idChecker;

import pedrolo.idChecker.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipalActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button botSaldo, botMovimientos=null;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        
        botSaldo=(Button)findViewById(R.id.botSaldo);
        botMovimientos=(Button)findViewById(R.id.botMovimientos);
        
        botSaldo.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				abreSaldo();
			}
		});
        
        botMovimientos.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				abreMovimientos();
			}
		});
    }
    
    private void abreSaldo(){
    	Intent intent=null;
    	intent = new Intent(Constants.SALDO);
        startActivity(intent);
    }
    
    private void abreMovimientos(){
    	Intent intent=null;
    	intent = new Intent(Constants.MOVIMIENTOS);
        startActivity(intent);
    }
}