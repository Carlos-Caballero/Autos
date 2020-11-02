package sample;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class Controller implements Initializable,Observer {

    @FXML
    private ImageView img1;

    @FXML
    private Text txtAutos;

    @FXML
    private AnchorPane panel;

    @FXML
    private Rectangle r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20;

    @FXML
    private ImageView img11;
    private String autoE = "/sample/carroE.jpg";
    private String autoS = "/sample/carroS.jpg";
    public static ArrayList<Rectangle> lugares = new ArrayList<Rectangle>();
    private ArrayList<ImageView> autos = new ArrayList<ImageView>();
    private int autosDisp = 100;
    private Semaphore mutexE = new Semaphore(1);
    private Semaphore puente= new Semaphore(1);
    private Semaphore mutexS = new Semaphore(1);
    private Semaphore entrada = new Semaphore(1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lugares.add(r1);
        lugares.add(r2);
        lugares.add(r3);
        lugares.add(r4);
        lugares.add(r5);
        lugares.add(r6);
        lugares.add(r7);
        lugares.add(r8);
        lugares.add(r9);
        lugares.add(r10);
        lugares.add(r11);
        lugares.add(r12);
        lugares.add(r13);
        lugares.add(r14);
        lugares.add(r15);
        lugares.add(r16);
        lugares.add(r17);
        lugares.add(r18);
        lugares.add(r19);
        lugares.add(r20);

        for(int i = 0; i < 22; i++){//   todo
            Image img = new Image(autoE);
            ImageView imageV = new ImageView(img);
            imageV.setFitHeight(57.0);
            imageV.setFitWidth(49.0);
            imageV.setX(0);
            imageV.setY(0);
            Text text = new Text();
            text.setText("E"+i);
            panel.getChildren().add(imageV);
            autos.add(imageV);
            Auto auto = new Auto(mutexE,mutexS,puente,imageV);
            auto.addObserver(this);
            Thread autoT = new Thread(auto,"E"+i);
            autoT.setDaemon(true);
            autoT.start();
        }
    }

    @FXML
    void iniciar(MouseEvent event) {
        probar(autos.remove(0));
    }

    public void probar(ImageView c1){
        moverAEntrada(c1);
        moverAPuente(c1);
        entrarAPuente(c1);
        int mayor = lugares.size() -1;
        int valor = (int)Math.floor(Math.random()*(mayor-0+1)+0);
        System.out.println(valor+"POSICION 1");
        Rectangle r = lugares.remove(valor);
        estacionar(c1,r);
        int time = esperarYRegresar(c1,(int)Math.random()*5 + 1,r);
        int time2=moverASalida(c1,time);
        salir(c1,time2);
        /*moverAEntrada(c2);
        moverAPuente(c2);
        entrarAPuente(c2);
        mayor = lugares.size() -1;
        valor = (int)Math.floor(Math.random()*(mayor-0+1)+0);
        System.out.println(valor+"POSICION 2");
        r = lugares.remove(valor);
        estacionar(c2,r);
        time = esperarYRegresar(c2,Math.random()*5 + 1,r);
        time2=moverASalida(c2,time);
        salir(c2,time2);

         */
    }

    public void moverAEntrada(ImageView c){
        autosDisp--;
        txtAutos.setText(String.valueOf(autosDisp));
        Image img = new Image(autoE);
        c.setImage(img);
        c.setOpacity(1.0);
        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(315 - c.getLayoutX());
        tt.setToY(573 - c.getLayoutY());

        tt.play();
    }

    public void moverAPuente(ImageView c){
        final TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(364 - c.getLayoutX());
        tt.setToY(573 - c.getLayoutY());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    tt.play();
                })
        );
        timeline.play();
    }

    public void entrarAPuente(ImageView c){
        final TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(367 - c.getLayoutX());
        tt.setToY(386 - c.getLayoutY());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    tt.play();
                })
        );
        timeline.play();

    }

    public void estacionar(ImageView c, Rectangle lugar){
        final TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(lugar.getLayoutX() - c.getLayoutX());
        tt.setToY(lugar.getLayoutY() - c.getLayoutY());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    tt.play();
                })
        );
        timeline.play();
    }

    public int esperarYRegresar(ImageView c, int time, Rectangle lugar){
        System.out.println("tiempo: "+ time);
        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(414 - c.getLayoutX());
        tt.setToY(428 - c.getLayoutY());
        int duration = time + 4;
        //lugares.add(lugar);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration), e -> {
                    tt.play();
                    lugares.add(lugar);
                    Image img = new Image(autoS);
                    c.setImage(img);
                })
        );
        timeline.play();

        return duration+1;
    }

    public int moverASalida(ImageView c,int time){
        final TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(365 - c.getLayoutX());
        tt.setToY(428 - c.getLayoutY());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(time), e -> {
                    tt.play();
                })
        );
        timeline.play();
        return time+1;
    }

    public void salir(ImageView c, int time){
        final TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1.0));
        tt.setNode(c);
        tt.setToX(367 - c.getLayoutX());
        tt.setToY(616 - c.getLayoutY());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(time), e -> {
                    tt.play();
                    fadeTransition(c);
                })
        );
        timeline.play();
    }

    public void fadeTransition(ImageView c){
        FadeTransition ft = new FadeTransition();
        ft.setDuration(Duration.seconds(1));
        ft.setNode(c);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }


    @Override
    public void update(Observable o, Object arg) {
        Object[] data = (Object[])arg;
        String opcion = (String)data[1];
        Auto auto = (Auto) data[0];
        int time=0,time2=0;
        switch (opcion){
            case "irAentrada":
                moverAEntrada(auto.getAuto());
                /*moverAEntrada(c1);
                moverAPuente(c1);
                entrarAPuente(c1);
                int mayor = lugares.size() -1;
                int valor = (int)Math.floor(Math.random()*(mayor-0+1)+0);
                System.out.println(valor+"POSICION 1");
                Rectangle r = lugares.remove(valor);
                estacionar(c1,r);
                double time = esperarYRegresar(c1,Math.random()*5 + 1,r);
                double time2=moverASalida(c1,time);
                salir(c1,time2);*/
                break;

            case "entrarAPuente":
                moverAPuente(auto.getAuto());
                entrarAPuente(auto.getAuto());
                break;

            case "BuscarLugar":
                    try {
                        if(lugares.isEmpty()){
                            System.out.println("ESPERANDO "+auto.getName());
                            entrada.acquire();
                        }
                        int mayor = lugares.size()-1;
                        int valor = (int)Math.floor(Math.random()*(mayor-0+1)+0);
                        System.out.println(valor+"POSICION");
                        Rectangle r = lugares.remove(valor);
                        auto.setLugar(r);
                        estacionar(auto.getAuto(),r);
                        auto.setTiempoEspera(esperarYRegresar(auto.getAuto(),(int)data[2],auto.getLugar()));
                        entrada.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                break;

            case "moverASalida":
                time2=moverASalida(auto.getAuto(),1);
                break;

            case "salir":
                salir(auto.getAuto(),1);
                break;
        }
    }
}
