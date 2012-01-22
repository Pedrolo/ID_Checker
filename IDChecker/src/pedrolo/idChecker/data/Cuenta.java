package pedrolo.idChecker.data;

public class Cuenta {
	private String nombre;
	private Double saldo;
	private String numero;
	
	public Cuenta(String nombre, String sald) {
		this.nombre=nombre;
		this.saldo=Double.valueOf(sald);
		this.numero=nombre.split("-")[0].replaceAll("/", "");
	}
	
	public String getNombre() {
		return nombre;
	}
	public Double getSaldo() {
		return saldo;
	}

	public String getNumero() {
		return numero;
	}
}
