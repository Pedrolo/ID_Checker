package pedrolo.idChecker.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.text.format.DateFormat;

public class FiltroMovimientos {
	private String cuenta;
	private String fechaDesde;
	private String fechaHasta;
	private String diaDesde,diaHasta,mesDesde,mesHasta,a�oDesde,a�oHasta;
	
	public FiltroMovimientos(){
		Calendar gc=GregorianCalendar.getInstance();		
		fechaHasta=DateFormat.format("dd/MM/yyyy",gc.getTime()).toString();
		gc.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		fechaDesde=DateFormat.format("dd/MM/yyyy",gc.getTime()).toString();
		
		String[] partesDesde=fechaDesde.split("/");
		setDiaDesde(partesDesde[0]);
		setMesDesde(partesDesde[1]);
		setA�oHasta(partesDesde[2]);

		String[] partesHasta=fechaHasta.split("/");
		setDiaHasta(partesHasta[0]);
		setMesHasta(partesHasta[1]);
		setA�oHasta(partesHasta[2]);
	}
	
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public void actualizaFechaDesde(){
		if(diaDesde!=""&&mesDesde!=""&&a�oDesde!=""){
			setFechaDesde(diaDesde+"/"+mesDesde+"/"+a�oDesde);
		}
	}
	
	public void actualizaFechaHasta(){
		if(diaHasta!=""&&mesHasta!=""&&a�oHasta!=""){
			setFechaHasta(diaHasta+"/"+mesHasta+"/"+a�oHasta);
		}
	}
	
	public String getFechaDesde() {
		return fechaDesde;
	}
	
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getDiaDesde() {
		return diaDesde;
	}
	public void setDiaDesde(String diaDesde) {
		this.diaDesde = diaDesde;
	}
	public String getDiaHasta() {
		return diaHasta;
	}
	public void setDiaHasta(String diaHasta) {
		this.diaHasta = diaHasta;
	}
	public String getMesDesde() {
		return mesDesde;
	}
	public void setMesDesde(String mesDesde) {
		this.mesDesde = mesDesde;
	}
	public String getMesHasta() {
		return mesHasta;
	}
	public void setMesHasta(String mesHasta) {
		this.mesHasta = mesHasta;
	}
	public String getA�oDesde() {
		return a�oDesde;
	}
	public void setA�oDesde(String a�oDesde) {
		this.a�oDesde = a�oDesde;
	}
	public String getA�oHasta() {
		return a�oHasta;
	}
	public void setA�oHasta(String a�oHasta) {
		this.a�oHasta = a�oHasta;
	}
	
	
}
