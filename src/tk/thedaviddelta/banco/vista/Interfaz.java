package tk.thedaviddelta.banco.vista;

import tk.thedaviddelta.banco.modelo.*;
import tk.thedaviddelta.banco.control.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class Interfaz extends Application {
    private static Stage primaryStage;
    private static Cliente currentUser;
    private static Cuenta currentAcc;
    
    @Override
    public void start(Stage primStage) {
        //Almacenar.get();
        //Almacenar.get_v2();
        Almacenar.get_v3();
        
        primaryStage = primStage;
        
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Interfaz.class.getResourceAsStream("Icono.png")));
        
        mainMenu();
        
        primaryStage.show();
    }
    
    @Override
    public void stop(){
        //Almacenar.save();
        //Almacenar.save_v2();
        Almacenar.save_v3();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private static void mainMenu(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menú principal");
        
        Text titleText = new Text("Bienvenido");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 46));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);

        Button[] op = new Button[3];
        
        op[0] = new Button("Login");
        op[0].setOnAction((ActionEvent event) -> {
            login();
        });
        
        op[1] = new Button("Sign up");
        op[1].setOnAction((ActionEvent event) -> {
            signup();
        });
        
        op[2] = new Button("Salir");
        op[2].setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        for (Button option : op) {
            option.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 26));
            vb.getChildren().add(option);
        }
    }
    
    private static void signup(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(25);
        
        Scene scene = new Scene(grid, 640, 480);
        primaryStage.setTitle("Creando usuario");
        primaryStage.setScene(scene);
        
        Text txt = new Text("Introduce los datos del usuario:");
        txt.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        grid.add(txt, 0, 0, 2, 1);
        
        Label labNom = new Label("\tNombre");
        TextField nom = new TextField();
        
        Label labPass = new Label("Contraseña");
        PasswordField pass = new PasswordField();
        
        labNom.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        labNom.setAlignment(Pos.CENTER_RIGHT);
        labNom.setMaxWidth(250);
        grid.add(labNom, 0, 2);
        nom.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 22));
        nom.setAlignment(Pos.CENTER);
        nom.setMaxWidth(250);
        grid.add(nom, 1, 2);
        
        labPass.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        labPass.setAlignment(Pos.CENTER_RIGHT);
        labPass.setMaxWidth(250);
        grid.add(labPass, 0, 3);
        pass.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 22));
        pass.setAlignment(Pos.CENTER);
        pass.setMaxWidth(250);
        grid.add(pass, 1, 3);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
        grid.add(target, 1, 4);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        hbBtn.getChildren().add(back);
        Button enter = new Button("Crear");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        hbBtn.getChildren().add(enter);
        grid.add(hbBtn, 1, 5);
        
        back.setOnAction((ActionEvent event) -> {
            mainMenu();
        });
        
        enter.setOnAction((ActionEvent event) -> {
            back.setDisable(true);
            enter.setDisable(true);
            
            target.setFill(Color.BLUE);
            target.setText("Creando usuario");
            
            Timeline tl = new Timeline(new KeyFrame(
                    Duration.seconds(0.75),
                    tlEvent -> {
                        target.setText(target.getText().concat("."));
                    }
            ));
            tl.setCycleCount(4);
            tl.setOnFinished((tlFin) -> {
                back.setDisable(false);
                enter.setDisable(false);
                
                boolean exists = false;
                for (int i = 0; i < Banco.sizeClientes() && !exists; i++) 
                    if (Banco.getCliente(i).getNombre().equals(nom.getText())) 
                        exists = true;

                if (exists){
                    target.setFill(Color.FIREBRICK);
                    target.setText("El usuario ya existe");
                }else{
                    String ciphered = Encriptar.cipher(pass.getText());
                    Banco.addCliente(new Cliente(nom.getText(), ciphered));
                    target.setFill(Color.GREEN);
                    target.setText("Usuario creado correctamente");
                }
            });
            tl.play();
            
//            boolean exists = false;
//            for (int i = 0; i < Banco.sizeClientes() && !exists; i++) 
//                if (Banco.getCliente(i).getNombre().equals(nom.getText())) 
//                    exists = true;
//
//            if (exists){
//                target.setFill(Color.FIREBRICK);
//                target.setText("El usuario ya existe");
//            }else{
//                String ciphered = Encriptar.cipher(pass.getText());
//                Banco.addCliente(new Cliente(nom.getText(), ciphered));
//                target.setFill(Color.GREEN);
//                target.setText("Usuario creado correctamente");
//            }
        });
    }
    
    private static void login(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(25);
        
        Scene scene = new Scene(grid, 640, 480);
        primaryStage.setTitle("Logeando");
        primaryStage.setScene(scene);
        
        Text txt = new Text("Introduce las credenciales:");
        txt.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 34));
        grid.add(txt, 0, 0, 2, 1);
        
        Label labNom = new Label("\tNombre");
        TextField nom = new TextField();
        
        Label labPass = new Label("Contraseña");
        PasswordField pass = new PasswordField();
        
        labNom.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        labNom.setAlignment(Pos.CENTER_RIGHT);
        labNom.setMaxWidth(250);
        grid.add(labNom, 0, 2);
        nom.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 22));
        nom.setAlignment(Pos.CENTER);
        nom.setMaxWidth(250);
        grid.add(nom, 1, 2);
        
        labPass.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        labPass.setAlignment(Pos.CENTER_RIGHT);
        labPass.setMaxWidth(250);
        grid.add(labPass, 0, 3);
        pass.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 22));
        pass.setAlignment(Pos.CENTER);
        pass.setMaxWidth(250);
        grid.add(pass, 1, 3);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
        grid.add(target, 1, 4);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        hbBtn.getChildren().add(back);
        Button enter = new Button("Entrar");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        hbBtn.getChildren().add(enter);
        grid.add(hbBtn, 1, 5);
        
        back.setOnAction((ActionEvent event) -> {
            mainMenu();
        });
        
        enter.setOnAction((ActionEvent event) -> {
            back.setDisable(true);
            enter.setDisable(true);
            
            target.setFill(Color.BLUE);
            target.setText("Logeando");
            
            Timeline tl = new Timeline(new KeyFrame(
                    Duration.seconds(0.75),
                    tlEvent -> {
                        target.setText(target.getText().concat("."));
                    }
            ));
            tl.setCycleCount(4);
            tl.setOnFinished((tlFin) -> {boolean found = false;
                for (int i = 0; i < Banco.sizeClientes() && !found; i++) 
                    if (Banco.getCliente(i).getNombre().equals(nom.getText()) && Encriptar.compare(pass.getText(), Banco.getCliente(i).getPass())) {
                        found = true;
                        target.setFill(Color.GREEN);
                        target.setText("Logeado satisfactoriamente");
                        currentUser = Banco.getCliente(i);
                        
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished((pauseFin) -> {
                            menuCliente();
                        });
                        pause.play();
                    }
                if (!found) {
                    back.setDisable(false);
                    enter.setDisable(false);
                    
                    target.setFill(Color.FIREBRICK);
                    target.setText("Credenciales erróneas");
                }
            });
            tl.play();
            
//            boolean found = false;
//            for (int i = 0; i < Banco.sizeClientes() && !found; i++) 
//                if (Banco.getCliente(i).getNombre().equals(nom.getText()) && Encriptar.compare(pass.getText(), Banco.getCliente(i).getPass())) {
//                    found = true;
//                    target.setFill(Color.GREEN);
//                    target.setText("Logeado satisfactoriamente");
//                    currentUser = Banco.getCliente(i);
//                    menuCliente();
//                }
//            if (!found) {
//                target.setFill(Color.FIREBRICK);
//                target.setText("Credenciales erróneas");
//            }
        });
    }
    
    private static void menuCliente(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Logeado como %s", currentUser.getNombre()));
        
        Text titleText = new Text(String.format("Bienvenido, %s", currentUser.getNombre()));
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 46));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);

        Button[] op = new Button[3];
        
        op[0] = new Button("Crear cuenta");
        op[0].setOnAction((ActionEvent event) -> {
            newCuenta();
        });
        
        op[1] = new Button("Seleccionar cuenta existente");
        op[1].setOnAction((ActionEvent event) -> {
            selectCuenta();
        });
        
        op[2] = new Button("Deslogear");
        op[2].setOnAction((ActionEvent event) -> {
            mainMenu();
        });
        
        for (Button option : op) {
            option.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 26));
            vb.getChildren().add(option);
        }
    }
    
    private static void newCuenta(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Creando cuenta...");
        
        Text titleText = new Text("Creando cuenta");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        ProgressBar pb = new ProgressBar(0);
        pb.setPrefSize(vb.getWidth() - 100, 50);
        vb.getChildren().add(pb);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target);
        
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setDisable(true);
        back.setOnAction((ActionEvent event) -> {
            menuCliente();
        });
        vb.getChildren().add(back);
        
        Timeline tl = new Timeline(new KeyFrame(
            Duration.seconds(0.03),
            tlEvent -> {
                pb.setProgress(pb.getProgress()+0.01);
            }
        ));
        tl.setCycleCount(100);
        tl.setOnFinished((tlFin) -> {
            back.setDisable(false);
            
            char[] cod = Arrays.copyOf(new char []{'E', 'S', '2', '9'}, 20);
            Random rdm = new Random();
            for (int i = 4; i < 20; i++) 
                cod[i] = (char)(rdm.nextInt(10)+48);
            currentUser.addCuenta(new Cuenta(new String(cod)));
            
            primaryStage.setTitle(String.format("Cuenta creada: %s", new String(cod)));
            titleText.setText(new String(cod));
            target.setFill(Color.GREEN);
            target.setText("Cuenta creada correctamente");
        });
        tl.play();
    }
    
    private static void selectCuenta(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Selecciona la cuenta");
        
        Text titleText = new Text();
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        if (currentUser.sizeCuentas() == 0) {
            titleText.setText("Éste usuario no tiene cuentas");
            titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
            titleText.setFill(Color.FIREBRICK);
        } else {
            titleText.setText("Selecciona la cuenta");
            titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 42));
            for (int i = 0; i < currentUser.sizeCuentas(); i++) {
                Cuenta c = currentUser.getCuenta(i);
                Button cuenta = new Button(c.getIBAN());
                cuenta.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 24));
                cuenta.setOnAction((ActionEvent event) -> {
                    currentAcc = c;
                    menuCuenta();
                });
                Button copy = new Button("Copiar IBAN al portapapeles");
                copy.setFont(Font.font("Comic Sans MS", FontWeight.LIGHT, 12));
                copy.setOnAction((ActionEvent event) -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(c.getIBAN());
                    Clipboard.getSystemClipboard().setContent(content);
                });
                
                vb.getChildren().addAll(cuenta, copy);
            }
        }
        
        Region r2 = new Region();
        vb.getChildren().add(r2);
        
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setOnAction((ActionEvent event) -> {
            menuCliente();
        });
        vb.getChildren().add(back);
    }
    
    private static void menuCuenta(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText = new Text("Selecciona operación");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 34));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);

        Button[] op = new Button[6];
        
        op[0] = new Button("Ingresar");
        op[0].setOnAction((ActionEvent event) -> {
            ingresar();
        });
        
        op[1] = new Button("Reintegrar");
        op[1].setOnAction((ActionEvent event) -> {
            reintegrar();
        });
        
        op[2] = new Button("Transferir");
        op[2].setOnAction((ActionEvent event) -> {
            transferir();
        });
        
        op[3] = new Button("Cancelar cuenta");
        op[3].setOnAction((ActionEvent event) -> {
            cancelar();
        });
        
        op[4] = new Button("Listar movimientos");
        op[4].setOnAction((ActionEvent event) -> {
            movimientos();
        });
        
        op[5] = new Button("Cambiar cuenta");
        op[5].setOnAction((ActionEvent event) -> {
            menuCliente();
        });
        
        for (Button option : op) {
            option.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
            vb.getChildren().add(option);
        }
    }
    
    private static void ingresar(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText = new Text("Introduce la cantidad a ingresar:");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 34));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        TextField dinero = new TextField();
        dinero.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 26));
        dinero.setAlignment(Pos.CENTER);
        dinero.setMaxWidth(350);
        vb.getChildren().add(dinero);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setOnAction((ActionEvent event) -> {
            menuCuenta();
        });
        hbBtn.getChildren().add(back);
        
        Button enter = new Button("Ingresar");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        enter.setOnAction((ActionEvent event) -> {
            try{
                BigDecimal d = new BigDecimal(dinero.getText());
                currentAcc.add(d);
                currentAcc.addMov(String.format("Ingreso: +%.2f$", d));
                
                target.setFill(Color.GREEN);
                target.setText(String.format("Se han ingresado %.2f$ correctamente", d));
            } catch (NumberFormatException e){
                target.setFill(Color.FIREBRICK);
                target.setText("La cantidad debe ser un número");
            }
        });
        hbBtn.getChildren().add(enter);
        
        hbBtn.setAlignment(Pos.CENTER);
        vb.getChildren().add(hbBtn);
    }
    
    private static void reintegrar(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText = new Text("Introduce la cantidad a reintegrar:");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 34));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        TextField dinero = new TextField();
        dinero.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 26));
        dinero.setAlignment(Pos.CENTER);
        dinero.setMaxWidth(350);
        vb.getChildren().add(dinero);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setOnAction((ActionEvent event) -> {
            menuCuenta();
        });
        hbBtn.getChildren().add(back);
        
        Button enter = new Button("Reintegrar");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        enter.setOnAction((ActionEvent event) -> {
            try{
                BigDecimal d = new BigDecimal(dinero.getText());
                if (currentAcc.subtract(d)) {
                    currentAcc.addMov(String.format("Reintegro: -%.2f$", d));
                    target.setFill(Color.GREEN);
                    target.setText(String.format("Se han reintegrado %.2f$ correctamente", d));
                } else {
                    target.setFill(Color.FIREBRICK);
                    target.setText("No hay suficiente cantidad en la cuenta");
                }
            } catch (NumberFormatException e){
                target.setFill(Color.FIREBRICK);
                target.setText("La cantidad debe ser un número");
            }
        });
        hbBtn.getChildren().add(enter);
        
        hbBtn.setAlignment(Pos.CENTER);
        vb.getChildren().add(hbBtn);
    }
    
    private static void transferir(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText1 = new Text("Introduce el IBAN de la cuenta a ingresar:");
        titleText1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        titleText1.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText1);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        TextField iban = new TextField();
        iban.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 26));
        iban.setAlignment(Pos.CENTER);
        iban.setMaxWidth(350);
        vb.getChildren().add(iban);
        
        Text titleText2 = new Text("Introduce la cantidad a transferir:");
        titleText2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        titleText2.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText2);
        
        Region r2 = new Region();
        vb.getChildren().add(r2);
        
        TextField dinero = new TextField();
        dinero.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 26));
        dinero.setAlignment(Pos.CENTER);
        dinero.setMaxWidth(350);
        vb.getChildren().add(dinero);
        
        Text target1 = new Text();
        target1.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target1);
        
        Text target2 = new Text();
        target2.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target2);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setOnAction((ActionEvent event) -> {
            menuCuenta();
        });
        hbBtn.getChildren().add(back);
        
        Button enter = new Button("Transferir");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        enter.setOnAction((ActionEvent event) -> {
            try{
                BigDecimal d = new BigDecimal(dinero.getText());
                if (currentAcc.getIBAN().equals(iban.getText())) {
                    target1.setFill(Color.FIREBRICK);
                    target2.setFill(Color.FIREBRICK);
                    target1.setText("No se puede realizar una transferencia");
                    target2.setText("desde una cuenta a ella misma");
                } else {
                    Cuenta c = null;
                    boolean found = false;
                    for (int i = 0; i < Banco.sizeClientes() && !found; i++) 
                        for (int j = 0; j < Banco.getCliente(i).sizeCuentas() && !found; j++) 
                            if (Banco.getCliente(i).getCuenta(j).getIBAN().equals(iban.getText())) {
                                c = Banco.getCliente(i).getCuenta(j);
                                found = true;
                            }
                    if (!found) {
                        target1.setFill(Color.FIREBRICK);
                        target2.setFill(Color.FIREBRICK);
                        target1.setText("El IBAN no se corresponde");
                        target2.setText("con ninguna cuenta");
                    } else {
                        if (currentAcc.subtract(d)) {
                            c.add(d);
                            currentAcc.addMov(String.format("Transferencia: -%.2f$ (a %s)", d, c.getIBAN()));
                            c.addMov(String.format("Transferencia: +%.2f$ (de %s)", d, currentAcc.getIBAN()));
                            target1.setFill(Color.GREEN);
                            target2.setFill(Color.GREEN);
                            target1.setText(String.format("Se han transferido %.2f$ a", d));
                            target2.setText(String.format("%s correctamente", c.getIBAN()));
                        } else {
                            target1.setFill(Color.FIREBRICK);
                            target2.setFill(Color.FIREBRICK);
                            target1.setText("No hay suficiente cantidad en la cuenta");
                            target2.setText("");
                        }
                    }
                }
            } catch (NumberFormatException e){
                target1.setFill(Color.FIREBRICK);
                target2.setFill(Color.FIREBRICK);
                target1.setText("La cantidad debe ser un número");
                target2.setText("");
            }
        });
        hbBtn.getChildren().add(enter);
        
        hbBtn.setAlignment(Pos.CENTER);
        vb.getChildren().add(hbBtn);
    }
    
    private static void cancelar(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText = new Text("¿Estás seguro?");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 38));
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        Text target = new Text();
        target.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        vb.getChildren().add(target);
        
        HBox hbBtn = new HBox();
        hbBtn.setSpacing(30);
        Button back = new Button("No");
        back.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        back.setOnAction((ActionEvent event) -> {
            menuCuenta();
        });
        hbBtn.getChildren().add(back);
        
        Button enter = new Button("Sí");
        enter.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        enter.setOnAction((ActionEvent event) -> {
            back.setDisable(true);
            enter.setDisable(true);
            
            target.setFill(Color.BLUE);
            target.setText("Eliminando cuenta");
            
            Timeline tl = new Timeline(new KeyFrame(
                    Duration.seconds(0.75),
                    tlEvent -> {
                        target.setText(target.getText().concat("."));
                    }
            ));
            tl.setCycleCount(4);
            tl.setOnFinished((tlFin) -> {
                currentUser.remCuenta(currentAcc);
                target.setFill(Color.GREEN);
                target.setText("Cuenta cancelada correctamente");

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished((pauseFin) -> {
                    menuCliente();
                });
                pause.play();
            });
            tl.play();
        });
        hbBtn.getChildren().add(enter);
        
        hbBtn.setAlignment(Pos.CENTER);
        vb.getChildren().add(hbBtn);
    }
    
    private static void movimientos(){
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        vb.setPadding(new Insets(1, 75, 1, 75));
        
        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle(String.format("Cuenta actual: %s", currentAcc.getIBAN()));
        
        Text titleText = new Text();
        titleText.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(titleText);
        
        Region r = new Region();
        vb.getChildren().add(r);
        
        if (currentAcc.getMovs().isEmpty()) {
            titleText.setText("La cuenta no ha realizado movimientos");
            titleText.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 30));
            titleText.setFill(Color.FIREBRICK);
        } else {
            titleText.setText("Movimientos de la cuenta");
            titleText.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 36));
            
            VBox vbPane = new VBox();
            int size = currentAcc.getMovs().size();
            ArrayList<Text> target = new ArrayList<>();

            Text slash;
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    //slash = new Text("-------------------------------");
                    //slash.setFill(Color.GREY);
                    //slash.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 20));
                    //slash.setTextAlignment(TextAlignment.CENTER);
                    Separator sep = new Separator(Orientation.HORIZONTAL);
                    sep.setMaxWidth(300);
                    sep.setPadding(new Insets(15, 1, 15, 1));
                    vbPane.getChildren().add(sep);
                }

                target.add(new Text());
                target.get(i).setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 20));
                target.get(i).setTextAlignment(TextAlignment.CENTER);
                String mov = currentAcc.getMovs().get(size-i-1);
                if (mov.contains("Transferencia")){
                    String[] movSplit = mov.split("\\(");
                    target.get(i).setText(movSplit[0].substring(0, movSplit[0].length()-1) + "\n(" + movSplit[1]);
                } else
                    target.get(i).setText(mov);
                vbPane.getChildren().add(target.get(i));
            }
            //vbPane.setFillWidth(true);
            vbPane.setAlignment(Pos.CENTER);

            ScrollPane sp = new ScrollPane(vbPane);
            sp.setPrefViewportHeight(275);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            sp.setPadding(new Insets(20, 1, 20, 1));
            vb.getChildren().add(sp);
        }
        
        Button back = new Button("Volver");
        back.setFont(Font.font("Comic Sans MS", FontWeight.MEDIUM, 22));
        back.setOnAction((ActionEvent event) -> {
            menuCuenta();
        });
        vb.getChildren().add(back);
    }
    
}


