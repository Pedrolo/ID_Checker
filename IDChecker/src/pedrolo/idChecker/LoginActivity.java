package pedrolo.idChecker;

import pedrolo.idChecker.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button botLogin=null;
	private EditText campoDNI,campoPIN=null;
	private ProgressDialog dialog=null;
	
	private RequestHandler requestHandler=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        botLogin=(Button)findViewById(R.id.botLogin);
        campoDNI=(EditText)findViewById(R.id.textDNI);
        campoPIN=(EditText)findViewById(R.id.textPIN);
        
        botLogin.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
    }
    
    private void login(){
    	try{
	    	if(requestHandler==null){
	    		requestHandler=RequestHandler.getInstance();
	    	}
	    	requestHandler.listCuentas();
	    	dialog = ProgressDialog.show(this, "","Autenticando", true);
	    	new Thread(){
	    		public void run(){
	    			//if(requestHandler.login(campoDNI.getText().toString(), campoPIN.getText().toString())){
	    	    		Intent intent=null;
	    	        	intent = new Intent(Constants.SALDO);
	    	            startActivity(intent);
	    	        //}
	    		}
	    	}.start();
	    	
    	}catch(Exception e){
    		Log.e("LoginActivity", "Error en login()",e);
    	}
    }
}