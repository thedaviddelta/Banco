package tk.thedaviddelta.banco.control;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encriptar {
    public static String cipher(String pass){
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 2048, 512);
        
        try{
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] encrypted = skf.generateSecret(spec).getEncoded();
            
            String pass64 = Base64.getEncoder().encodeToString(encrypted);
            String salt64 = Base64.getEncoder().encodeToString(salt);
            
            return salt64.concat("$".concat(pass64));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            System.err.println("Error al encriptar la contraseña");
            return null;
        } finally {
            spec.clearPassword();
        }
    }
    
    public static boolean compare(String pass, String userPass){
        String[] splitPass = userPass.split("\\$");
        byte[] salt = Base64.getDecoder().decode(splitPass[0]);
        
        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 2048, 512);
        
        try{
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] encrypted = skf.generateSecret(spec).getEncoded();
            String pass64 = Base64.getEncoder().encodeToString(encrypted);
            
            if (pass64.equals(splitPass[1])) 
                return true;
            return false;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            System.err.println("Error al encriptar la contraseña");
            return false;
        } finally {
            spec.clearPassword();
        }
    }
}
