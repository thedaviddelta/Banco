package tk.thedaviddelta.banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Cuenta implements Serializable {
    private final String IBAN;
    private BigDecimal dinero;
    private ArrayList<String> movs;

    public Cuenta(String iban) {
        this.IBAN = iban;
        this.dinero = new BigDecimal(0);
        this.movs = new ArrayList<>();
    }

    public String getIBAN() {
        return IBAN;
    }

    public BigDecimal getDinero() {
        return dinero;
    }
    
    public void add(BigDecimal dinero) {
        this.dinero = this.dinero.add(dinero);
    }
    
    public boolean subtract(BigDecimal dinero) {
        if ( (this.dinero.subtract(dinero)).compareTo(new BigDecimal(0)) < 0 ) 
            return false;
        this.dinero = this.dinero.subtract(dinero);
        return true;
    }
    
    public void addMov(String s){
        this.movs.add(s);
    }
    
    public ArrayList<String> getMovs(){
        return this.movs;
    }
}
