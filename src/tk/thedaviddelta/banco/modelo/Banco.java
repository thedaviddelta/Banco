package tk.thedaviddelta.banco.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Banco implements Serializable {
    private static ArrayList<Cliente> clientes = new ArrayList<>();
    
    public static void addCliente(Cliente c){
        clientes.add(c);
    }
    
    public static Cliente getCliente(int n) {
        return clientes.get(n);
    }
    
    public static int sizeClientes() {
        return clientes.size();
    }

    public static ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public static void setClientes(ArrayList<Cliente> clientes) {
        Banco.clientes = clientes;
    }
}
