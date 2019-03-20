package tk.thedaviddelta.banco.vista;

import tk.thedaviddelta.banco.modelo.*;
import tk.thedaviddelta.banco.control.*;
import java.io.Console;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Consola {
    private static Scanner tec = new Scanner(System.in);
    private static Cliente currentUser;
    private static Cuenta currentAcc;
    
    public static void main(String[] args) {
        //Almacenar.get();
        //Almacenar.get_v2();
        Almacenar.get_v3();
        
        mainMenu();
        
        //Almacenar.save();
        //Almacenar.save_v2();
        Almacenar.save_v3();
    }
    
    private static void mainMenu(){
        int op;
        do {
            System.out.println("1.- Login\n2.- Sign up\n3.- Salir");
            op = Integer.parseInt(tec.nextLine());
            switch (op){
                case 1: // login
                    System.out.println();
                    login();
                    break;
                case 2: // sign up
                    System.out.println();
                    signup();
                    break;
                case 3: // salir
                    System.out.println("Se saldrá del programa");
                    break;
                default:
                    System.out.println("Valor no válido");
            }
            if (op != 3)
                System.out.println();
        } while (op != 3);
    }
    
    private static void signup(){
        System.out.println("*** Creando usuario ***");
        System.out.print("Nombre: ");
        String nombre = tec.nextLine();
        
        boolean exists = false;
        for (int i = 0; i < Banco.sizeClientes() && !exists; i++) 
            if (Banco.getCliente(i).getNombre().equals(nombre)) 
                exists = true;
        
        if (exists)
            System.out.println("El usuario ya existe");
        else{
            Console console = System.console();
            String pass;
            if (console == null) {
                System.out.print("Contraseña: ");
                pass = tec.nextLine();
            } else
                pass = new String(console.readPassword("Contraseña: "));
            pass = Encriptar.cipher(pass);
            Banco.addCliente(new Cliente(nombre, pass));
            System.out.println("Usuario creado correctamente");
        }
    }
    
    private static void login(){
        System.out.println("*** Introduce las credenciales ***");
        System.out.print("Nombre: ");
        String nombre = tec.nextLine();
        
        Console console = System.console();
        String pass;
        if (console == null) {
            System.out.print("Contraseña: ");
            pass = tec.nextLine();
        } else
            pass = new String(console.readPassword("Contraseña: "));
        
        boolean found = false;
        for (int i = 0; i < Banco.sizeClientes() && !found; i++) 
            if (Banco.getCliente(i).getNombre().equals(nombre) && Encriptar.compare(pass, Banco.getCliente(i).getPass())) {
                found = true;
                System.out.println("Logeado satisfactoriamente");
                currentUser = Banco.getCliente(i);
                System.out.println();
                menuCliente();
            }
        if (!found) {
            System.out.println("Credenciales erróneas");
        }
    }
    
    private static void menuCliente(){
        int op;
        do {
            System.out.printf("*** Logeado como %s ***\n", currentUser.getNombre());
            System.out.println("1.- Crear cuenta\n2.- Seleccionar cuenta existente\n3.- Deslogear");
            op = Integer.parseInt(tec.nextLine());
            switch (op) {
                case 1: // crear
                    System.out.println();
                    newCuenta();
                    break;
                case 2: // seleccionar
                    selectCuenta();
                    break;
                case 3: // deslogear
                    System.out.println("Deslogeado satisfactoriamente");
                    break;
                default:
                    System.out.println("Valor no válido");
            }
            if (op != 3)
                System.out.println();
        } while (op != 3);
    }
    
    private static void newCuenta(){
        System.out.println("*** Creando cuenta ***");
        char[] cod = Arrays.copyOf(new char []{'E', 'S', '2', '9'}, 20);
        Random rdm = new Random();
        for (int i = 4; i < 20; i++) 
            cod[i] = (char)(rdm.nextInt(10)+48);
        currentUser.addCuenta(new Cuenta(new String(cod)));
        System.out.printf("Cuenta creada correctamente con ISBN '%s'\n", new String(cod));
    }
    
    private static void selectCuenta(){
        if (currentUser.sizeCuentas() == 0) 
            System.out.println("Éste usuario no tiene cuentas");
        else {
            System.out.println();
            System.out.println("*** Selecciona la cuenta ***");
            for (int i = 0; i < currentUser.sizeCuentas(); i++) 
                System.out.printf("%d.- %s\n", i+1, currentUser.getCuenta(i).getIBAN());
            int op = Integer.parseInt(tec.nextLine());
            if (op > currentUser.sizeCuentas()) 
                System.out.println("Valor no válido");
            else{
                currentAcc = currentUser.getCuenta(op-1);
                System.out.println();
                menuCuenta();
            }
        }
    }
    
    private static void menuCuenta(){
        int op;
        BigDecimal d;
        boolean salir = false;
        do {
            System.out.printf("*** Cuenta actual: %s ***\n", currentAcc.getIBAN());
            System.out.println("1.- Ingresar\n2.- Reintegrar\n3.- Transferir\n4.- Cancelar cuenta\n5.- Listar movimientos\n6.- Cambiar cuenta");
            op = Integer.parseInt(tec.nextLine());
            switch (op) {
                case 1: // ingresar
                    System.out.print("Introduce la cantidad a ingresar: ");
                    d = new BigDecimal(tec.nextLine());
                    currentAcc.add(d);
                    currentAcc.addMov(String.format("Ingreso: +%.2f$", d));
                    System.out.printf("Se han ingresado %.2f$ correctamente\n", d);
                    break;
                case 2: // reintegro
                    System.out.print("Introduce la cantidad a reintegrar: ");
                    d = new BigDecimal(tec.nextLine());
                    if (currentAcc.subtract(d)) {
                        currentAcc.addMov(String.format("Reintegro: -%.2f$", d));
                        System.out.printf("Se han reintegrado %.2f$ correctamente\n", d);
                    } else
                        System.out.println("No hay suficiente cantidad en la cuenta");
                    break;
                case 3: // transferencia
                    System.out.printf("Introduce el IBAN de la cuenta a ingresar: ");
                    String iban = tec.nextLine();
                    
                    if (currentAcc.getIBAN().equals(iban)) 
                        System.out.println("No se puede realizar una transferencia desde una cuenta a ella misma");
                    else {
                        Cuenta c = null;
                        boolean found = false;
                        for (int i = 0; i < Banco.sizeClientes() && !found; i++) 
                            for (int j = 0; j < Banco.getCliente(i).sizeCuentas() && !found; j++) 
                                if (Banco.getCliente(i).getCuenta(j).getIBAN().equals(iban)) {
                                    c = Banco.getCliente(i).getCuenta(j);
                                    found = true;
                                }
                        if (!found) 
                            System.out.println("El IBAN no se corresponde con ninguna cuenta");
                        else {
                            System.out.print("Introduce la cantidad a transferir: ");
                            d = new BigDecimal(tec.nextLine());
                            if (currentAcc.subtract(d)) {
                                c.add(d);
                                currentAcc.addMov(String.format("Transferencia: -%.2f$ (a %s)", d, c.getIBAN()));
                                c.addMov(String.format("Transferencia: +%.2f$ (de %s)", d, currentAcc.getIBAN()));
                                System.out.printf("Se han transferido %.2f$ a %s correctamente\n", d, c.getIBAN());
                            } else
                                System.out.println("No hay suficiente cantidad en la cuenta");
                        }
                    }
                    break;
                case 4: // cancelar
                    System.out.print("¿Estás seguro? ('S'/'N') ");
                    if (tec.nextLine().charAt(0) == 'S') {
                        currentUser.remCuenta(currentAcc);
                        System.out.println("Cuenta cancelada correctamente");
                        salir = true;
                    } else
                        System.out.println("No se ha cancelado la cuenta");
                    break;
                case 5: // movimientos
                    ArrayList<String> movs = currentAcc.getMovs();
                    if (movs.size() <= 0) 
                        System.out.println("La cuenta no ha realizado movimientos");
                    else {
                        System.out.println("Movimientos de la cuenta:");
                        for (int i = movs.size()-1; i >= 0; i--) 
                            System.out.printf("%d.- %s\n", i+1, movs.get(i));
                    }
                    break;
                case 6: // cambiar
                    salir = true;
                    break;
                default:
                    System.out.println("Valor no válido");
            }
            if (!salir)
                System.out.println();
        } while (!salir);
    }
}
