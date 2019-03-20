package tk.thedaviddelta.banco.control;

import tk.thedaviddelta.banco.modelo.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class Almacenar {
    private static Path path_xml;
    private static Path path_ser;
    private static Properties props = new Properties(); //unused
    private static XStream xst = new XStream(new DomDriver());
    
    private static void correctPath(){
        //String exDir = System.getProperty("user.dir");
        /*
        Path exDir = Paths.get(".").toAbsolutePath();
        if (exDir.endsWith("build/classes/.")){
            path_xml = Paths.get("../../src/Banco/control/data.xml");
            path_ser = Paths.get("../../src/Banco/control/data.out");
        }else{
            path_xml = Paths.get("src/Banco/control/data.xml");
            path_ser = Paths.get("src/Banco/control/data.out");
        }
        */
        path_xml = Paths.get(System.getProperty("user.home").concat("/banco_app.xml"));
        path_ser = Paths.get(System.getProperty("user.home").concat("/banco_app.out"));
    }
    
    public static void save(){ //unused
        props.setProperty("sizeClientes", Integer.toString(Banco.sizeClientes()));
        for (int i = 0; i < Banco.sizeClientes(); i++) {
            Cliente c = Banco.getCliente(i);
            props.setProperty(String.format("cliente%d.nombre", i), c.getNombre());
            props.setProperty(String.format("cliente%d.pass", i), c.getPass());
            props.setProperty(String.format("cliente%d.sizeCuentas", i), Integer.toString(c.sizeCuentas()));
            for (int j = 0; j < c.sizeCuentas(); j++) {
                Cuenta cu = c.getCuenta(j);
                props.setProperty(String.format("cliente%d.cuenta%d.IBAN", i, j), cu.getIBAN());
                props.setProperty(String.format("cliente%d.cuenta%d.dinero", i, j), cu.getDinero().toString());
                props.setProperty(String.format("cliente%d.cuenta%d.sizeMovs", i, j), Integer.toString(cu.getMovs().size()));
                for (int k = 0; k < cu.getMovs().size(); k++) 
                    props.setProperty(String.format("cliente%d.cuenta%d.mov%d", i, j, k), cu.getMovs().get(k));
            }
        }
        try{
            OutputStream out = Files.newOutputStream(path_xml);
            props.storeToXML(Files.newOutputStream(path_xml), "Datos del banco");
            out.close();
        } catch (IOException e){
            System.err.println("Error al guardar los datos");
        }
    }
    
    public static void get(){ //unused
        correctPath();
        try{
            if (Files.exists(path_xml)){
                InputStream in = Files.newInputStream(path_xml);
                props.loadFromXML(Files.newInputStream(path_xml));
                in.close();
            }
        } catch (IOException e){
            System.err.println("Error al leer los datos");
        }
        if (props.getProperty("sizeClientes") != null) 
            for (int i = 0; i < Integer.parseInt(props.getProperty("sizeClientes")); i++) {
                String nombre = props.getProperty(String.format("cliente%d.nombre", i));
                String pass = props.getProperty(String.format("cliente%d.pass", i));
                Cliente c = new Cliente(nombre, pass);
                Banco.addCliente(c);
                for (int j = 0; j < Integer.parseInt(props.getProperty(String.format("cliente%d.sizeCuentas", i))); j++) {
                    String iban = props.getProperty(String.format("cliente%d.cuenta%d.IBAN", i, j));
                    BigDecimal dinero = new BigDecimal(props.getProperty(String.format("cliente%d.cuenta%d.dinero", i, j)));
                    Cuenta cu = new Cuenta(iban);
                    cu.add(dinero);
                    c.addCuenta(cu);
                    for (int k = 0; k < Integer.parseInt(props.getProperty(String.format("cliente%d.cuenta%d.sizeMovs", i, j))); k++) {
                        String mov = props.getProperty(String.format("cliente%d.cuenta%d.mov%d", i, j, k));
                        cu.addMov(mov);
                    }
                }
            }
    }
    
    public static void save_v2(){ //unused
        xst.alias("cliente", Cliente.class);
        xst.alias("cuenta", Cuenta.class);
        try{
            OutputStream out = Files.newOutputStream(path_xml);
            xst.toXML(Banco.getClientes(), out);
            out.close();
        } catch (IOException e){
            System.err.println("Error al guardar los datos");
        }
    }
    
    public static void get_v2(){ //unused
        xst.alias("cliente", Cliente.class);
        xst.alias("cuenta", Cuenta.class);
        correctPath();
        
        XStream.setupDefaultSecurity(xst);
        //xst.addPermission(AnyTypePermission.ANY);
        xst.allowTypes(new Class[]{ArrayList.class, String.class, Cliente.class, Cuenta.class});
        try{
            if (Files.exists(path_xml)){
                InputStream in = Files.newInputStream(path_xml);
                Banco.setClientes( (ArrayList<Cliente>)xst.fromXML(in) );
                in.close();
            }
        } catch (IOException e){
            System.err.println("Error al leer los datos");
        }
    }
    
    public static void save_v3(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path_ser));
            out.writeObject(Banco.getClientes());
            out.close();
        } catch (IOException e){
            System.err.println("Error al guardar los datos");
        }
    }
    
    public static void get_v3(){
        correctPath();
        try{
            if (Files.exists(path_ser)){
                ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path_ser));
                Banco.setClientes((ArrayList<Cliente>) in.readObject());
                in.close();
            }
        } catch (IOException | ClassNotFoundException e){
            System.err.println("Error al leer los datos");
        }
    }
}
