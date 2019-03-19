package tk.thedaviddelta.banco.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Cliente implements Serializable {
    private String nombre;
    private String pass;
    private ArrayList<Cuenta> cuentas;

    public Cliente(String nombre, String pass) {
        this.nombre = nombre;
        this.pass = pass;
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getPass() {
        return pass;
    }

    public void addCuenta(Cuenta c) {
        this.cuentas.add(c);
    }

    public Cuenta getCuenta(int n) {
        return cuentas.get(n);
    }
    
    public void remCuenta(Cuenta c) {
        cuentas.remove(c);
    }
    
    public int sizeCuentas() {
        return cuentas.size();
    }

    @Override
    public int hashCode() { //unused
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.nombre);
        hash = 47 * hash + Objects.hashCode(this.pass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) { //unused
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.nombre, other.nombre) || !Objects.equals(this.pass, other.pass)) {
            return false;
        }
        return true;
    }
    
}
