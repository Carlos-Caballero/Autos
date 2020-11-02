package sample;

import javafx.beans.InvalidationListener;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Semaphore;

import static sample.Config.cochesE;
import static sample.Config.cochesS;

public class Auto extends Observable implements Runnable{
    private Semaphore mutexE;
    private Semaphore mutexS;
    private Semaphore puente;
    private ImageView auto;
    private String name=null;
    private Rectangle lugar;
    private Object[] data = new Object[3];
    private boolean entrar = true;
    private int tiempoEspera=0;

    public Auto(Semaphore mutexE,Semaphore mutexS, Semaphore puente, ImageView auto){
        this.mutexE = mutexE;
        this.mutexS = mutexS;
        this.puente = puente;
        this.auto = auto;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public ImageView getAuto() {
        return auto;
    }

    public Rectangle getLugar() {
        return lugar;
    }

    public String getName(){
        return name;
    }

    public void setLugar(Rectangle lugar) {
        this.lugar = lugar;
    }

    @Override
    public void run() {
                name = Thread.currentThread().getName();
                try {
                    entrar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ENTRO: "+Thread.currentThread().getName());
                salir();
                System.out.println("SALIO: "+Thread.currentThread().getName());




/*
        if (entrar){
            entrar = false;
        try {
            mutex.acquire();
            data[0] = this;
            data[1] = "irAentrada";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("ir a entrada enviado" + Thread.currentThread().getName());
            while (cochesS > 0) {
                mutex.acquire();
                mutex.release();
                System.out.println("esperando" + Thread.currentThread().getName());
            }
            cochesE++;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Entrar a puente
        data[0] = this;
        data[1] = "entrarAPuente";
        this.setChanged();
        this.notifyObservers(data);
        System.out.println("entrar a puente enviado" + Thread.currentThread().getName());
        try {
            mutex.acquire();
            cochesE--;
            data[0] = this;
            data[1] = "BuscarLugar";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("buscar lugar enviado" + Thread.currentThread().getName());
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //ESPERAR

        data[0] = this;
        data[1] = "esperar";
        this.setChanged();
        this.notifyObservers(data);
        System.out.println("esperar enviado" + Thread.currentThread().getName());
        try {
            mutex.acquire();
            data[0] = this;
            data[1] = "irASalida";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("salida enviado" + Thread.currentThread().getName());
            while (cochesE > 0) {
                mutex.acquire();
                mutex.release();
                System.out.println("esperando");
            }
            cochesS++;
            mutex.release();
            System.out.println("fin primer try catch" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("metodo salir"+Thread.currentThread().getName());
        //SALIR PUENTE
        try {
            mutex.acquire();
            data[0] = this;
            data[1] = "salir";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("salir enviado"+Thread.currentThread().getName());
            cochesS--;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    }

    public void entrar() throws InterruptedException {
        try {
            mutexE.acquire();
            data[0] = this;
            data[1] = "irAentrada";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("irAentrada"+Thread.currentThread().getName());
            Thread.sleep(2000);
            System.out.println("COCHES E "+cochesE+"COCHES S"+cochesS);
            cochesE++;
            if (cochesE == 1) {
                System.out.println("ESPERANDO"+Thread.currentThread().getName());puente.acquire();}
            mutexE.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("COCHES ENTRANDO: "+cochesE);
        data[0] = this;
        data[1] = "entrarAPuente";
        this.setChanged();
        this.notifyObservers(data);
        //Thread.sleep(2000);
        System.out.println("entrarAPuente"+Thread.currentThread().getName());
        data[0] = this;
        data[1] = "BuscarLugar";
        tiempoEspera = (int)(Math.random()*5 + 1);
        data[2]=tiempoEspera;
        this.setChanged();
        this.notifyObservers(data);
        System.out.println("BuscarLugar"+Thread.currentThread().getName());
        System.out.println("tiempo: "+tiempoEspera+" "+Thread.currentThread().getName());

        try {
            System.out.println("ANTES DE BUSCAR LUGAR: "+cochesE);
            mutexE.acquire();
            cochesE--;
            if (cochesE == 0) puente.release();
            mutexE.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void salir(){
        try {
            Thread.sleep(tiempoEspera*1000);
            mutexS.acquire();
            data[0] = this;
            data[1] = "moverASalida";
            this.setChanged();
            this.notifyObservers(data);
            System.out.println("moverASalida"+Thread.currentThread().getName());
            cochesS++;
            //tiempoEspera++;
            Thread.sleep(1000);
            if (cochesS == 1) puente.acquire();
            mutexS.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //USAR PUENTE

        try {
            mutexS.acquire();
            data[0] = this;
            data[1] = "salir";
            System.out.println("salir"+Thread.currentThread().getName());
            this.setChanged();
            this.notifyObservers(data);
            //tiempoEspera++;
            Thread.sleep(1000);
            cochesS--;
            if (cochesS == 0) puente.release();
            mutexS.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
