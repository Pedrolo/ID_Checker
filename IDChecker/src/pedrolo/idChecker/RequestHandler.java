package pedrolo.idChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pedrolo.idChecker.data.Cuenta;
import pedrolo.idChecker.data.FiltroMovimientos;
import pedrolo.idChecker.data.Movimiento;

import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

public class RequestHandler {
	private static final String PRE_LOGIN_URL="https://www1.ibercajadirecto.com/ibercaja/asp/Login.asp";
	private static final String LOGIN_URL="https://www1.ibercajadirecto.com/ibercaja/asp/modulodirector.asp";	
	private static final String CABECERA_URL="https://www1.ibercajadirecto.com/ibercaja/asp/VersionTarjetas/EntradaVT.asp";
	private static final String FRAME_URL="https://www1.ibercajadirecto.com/ibercaja/asp/ModuloDirector.asp";	
	
	private static final String CODIGO_CUENTAS="52_0";
	private static final String CODIGO_MOVIMIENTOS="53_0";
	
	private DefaultHttpClient httpClient = null;
    private HttpContext localContext = null;
    private String MSCSAuth=null;
    
    private static RequestHandler instance=null;
    
    public static RequestHandler getInstance(){
    	if(instance==null){
    		instance=new RequestHandler();
    	}
    	return instance;
    }
    
    private RequestHandler(){
    	httpClient = new DefaultHttpClient();
    	httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
    	localContext = new BasicHttpContext();
    }
	
	protected boolean login(String dni,String pin){
		try{
			// Create a new HttpClient and Post Header
			
			List<NameValuePair> params= initParams();
			params.add(new BasicNameValuePair("codidentific",""));
			params.add(new BasicNameValuePair("codidentific2",dni));
			params.add(new BasicNameValuePair("PIN",pin));
	        
	        HttpPost httppost = new HttpPost(URLUtil.guessUrl(LOGIN_URL));
	
	        httppost.setHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux " + "i686; en-US; rv:1.8.1.6) Gecko/20061201 Firefox/2.0.0.6 (Ubuntu-feisty)");
	        httppost.setHeader("Accept", "text/html,application/xml," + "application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
	        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	
	        httppost.setEntity(new UrlEncodedFormEntity(params));
	        
	        // Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httppost);
	
	        int StatusCode= response.getStatusLine().getStatusCode();
	        if(StatusCode==200){
	        	HttpEntity r_entity = response.getEntity();
				if(r_entity!=null){
					Document doc = Jsoup.parse(r_entity.getContent(),"UTF-8",CABECERA_URL);
					
					
					Elements campos=doc.getElementsByAttributeValue("name", "MSCSAuth");
					if(campos!=null&&campos.size()==1){
						Element campoAuth=campos.get(0);
						MSCSAuth=campoAuth.attr("value");
					}
				}
	        }
	
			return true;
		}catch(Exception e){
			Log.e("RequestHandler", "Error en initParams",e);
		}
		return false;
	}
	
	private void parseCabecera(Document doc){
		try{
			Elements els=doc.children();
			for(Element e:els){
				
			}
		}catch(Exception e){
			Log.e("RequestHandler", "Error en parseCabecera",e);
		}
	}
	
	private void relogin(){
		//A decidir si se hace relogeo o tira al login(que sería lo correcto)
	}
	
	private Document parseUrl(String url){
		try{
			HttpGet get=new HttpGet(url);
			HttpResponse response=httpClient.execute(get, localContext);
			
			int StatusCode= response.getStatusLine().getStatusCode();
			if(StatusCode==200){
				HttpEntity r_entity = response.getEntity();
				if(r_entity!=null){
					Document doc = Jsoup.parse(r_entity.getContent(),"UTF-8",url);
					return doc;
				}
			}else{
				relogin();
			}
		}catch(Exception e){
			Log.e("RequestHandler", "Error en parseUrl ("+url+")",e);
		}
		return null;
	}
	
	private List<NameValuePair> initParams(){
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		try{
			Document doc = parseUrl(PRE_LOGIN_URL);
			Elements campos=doc.getElementsByAttributeValue("type", "hidden");
			
			for(Element e:campos){
				String nombreCampo=e.attr("name");
				if("PIN".equals(nombreCampo)){
					continue;
				}
				if("IdOperacion".equals(nombreCampo)){
					params.add(new BasicNameValuePair(nombreCampo,"8000_0"));
					continue;
				}
				if("Canal".equals(nombreCampo)){
					params.add(new BasicNameValuePair(nombreCampo,"TAR"));
					continue;
				}
				params.add(new BasicNameValuePair(e.attr("name"),e.attr("value")));
			}
			params.add(new BasicNameValuePair("codidentific",""));
		}catch(Exception e){
			Log.e("RequestHandler", "Error en initParams",e);
		}
		return params;
	}
	
	private Document getDocument(String url,String idOperacion){
		try{
			List<NameValuePair> params= initParams();
			params.add(new BasicNameValuePair("IdOperacion",idOperacion));
			params.add(new BasicNameValuePair("Entidad","2085"));
			params.add(new BasicNameValuePair("Canal","TAR"));
			params.add(new BasicNameValuePair("Dispositivo","INTR"));
			params.add(new BasicNameValuePair("Idioma","ES"));
			
			HttpPost httppost = new HttpPost(URLUtil.guessUrl(url));
			
	        httppost.setHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux " + "i686; en-US; rv:1.8.1.6) Gecko/20061201 Firefox/2.0.0.6 (Ubuntu-feisty)");
	        httppost.setHeader("Accept", "text/html,application/xml," + "application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
	        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	
	        httppost.setEntity(new UrlEncodedFormEntity(params));
	        
	        // Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httppost);
	
	        int StatusCode= response.getStatusLine().getStatusCode();
	        if(StatusCode==200){
	        	HttpEntity r_entity = response.getEntity();
				if(r_entity!=null){
					return Jsoup.parse(r_entity.getContent(),"UTF-8",url);
				}
	        }
		}catch(Exception e){
			Log.e("RequestHandler", "Error en getDocument",e);
		}
		return null;
	}
	
	public List<Cuenta> listCuentas(){
		List<Cuenta> cuentas =  new ArrayList<Cuenta>();
		try{
			//Document doc=getDocument(FRAME_URL, CODIGO_CUENTAS);
			Document doc=Jsoup.parse(new File(Environment.getExternalStorageDirectory(),"saldo.html"), "UTF-8");
			
			Element tabla=doc.getElementsByAttributeValue("cellpadding", "3").get(0);
			
			Elements filas=tabla.getElementsByTag("tr");
			for(int i=1;i<filas.size();i++){
				Elements columnas=filas.get(i).getElementsByTag("td");
				String cuenta=columnas.get(0).text();
				String saldo=columnas.get(1).text().split(" ")[0];
				saldo=saldo.replace(".", "");
				saldo=saldo.replace(",", ".");
				
				cuentas.add(new Cuenta(cuenta, saldo));
			}			
		}catch(Exception e){
			Log.e("RequestHandler", "Error en listCuentas",e);
		}
		return cuentas;
	}
	
	public List<Movimiento> getMovimientos(FiltroMovimientos filtro){
		List<Movimiento> movimientos=new ArrayList<Movimiento>();
		try{
			Document doc=Jsoup.parse(new File(Environment.getExternalStorageDirectory(),"movimientos.html"), "UTF-8");
			
			Element tabla=doc.getElementsByAttributeValue("cellpadding", "3").get(0);
			
			Elements filas=tabla.getElementsByTag("tr");
			for(int i=2;i<filas.size();i++){
				try{
					Elements columnas=filas.get(i).getElementsByTag("td");
					Movimiento m=new Movimiento();
					m.setConcepto(columnas.get(0).text());
					m.setFechaOperacion(columnas.get(1).text());
					m.setFechaValor(columnas.get(2).text());
					
					String importe=columnas.get(4).text();
					importe=importe.replace(".", "").replace(",", ".");
					String operador=importe.substring(importe.length()-1);					
					importe=importe.substring(0,importe.length()-1);					
					m.setImporte(Double.parseDouble(importe)*("-".equals(operador)?-1:1));
					
					String saldo=columnas.get(5).text();
					saldo=saldo.replace(".", "").replace(",", ".");	
					m.setSaldo(Double.parseDouble(saldo));
					
					m.setDivisa(columnas.get(6).text());
					
					String document=columnas.get(6).text();
					m.setDocumento(document.trim().length()!=0);
					
					movimientos.add(m);
				}catch(Exception e){
					Log.e("RequestHandler", "Error en getMovimientos("+filtro.getCuenta()+")",e);
				}
			}
			return movimientos;
		}catch(Exception e){
			Log.e("RequestHandler","Error en getMovimientos("+filtro.getCuenta()+")",e);
		}
		return null;
	}
	
}
