/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animacion2d;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author maryf
 */
public class Animacion2D extends JFrame implements Runnable{

    private BufferedImage buffer, bufferImg;
    private Graphics2D graphics;
    private MetodosExtra obj;
    private int[][] vector=new int[15][4];
    private Color[] colores=new Color[10];
    private Thread hilo;
    private final int alto=500, ancho=600;
    int xOrigen,yOrigen;
    
    public  Animacion2D(){
        setSize(ancho, alto);
        setTitle("Animacion 2D Fer");
        setDefaultCloseOperation(3);
        setLayout(null);
        setLocationRelativeTo(null);
        obj=new MetodosExtra();
        
        setColores();
        buffer=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
        xOrigen=ancho/2;
        yOrigen = 300;
        graphics=bufferImg.createGraphics();
        //Graphics2D g2d = bufferImg.createGraphics();
        //graphics.setBackground(Color.WHITE);
        
            
        hilo=new Thread(this);
        
        setVisible(true);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        Animacion2D animacion=new Animacion2D();
        
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
         hilo.start();
         //polarCircle(300,330,30,colores[5]);
         //fill(300,330, colores[5]);
        
        this.getGraphics().drawImage(bufferImg,0, 0, this);
    }
    @Override
    public void update(Graphics g){
        //System.out.println(""+getGraphics());
        
        
//        BufferedImage fondo=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
//        graphics=fondo.createGraphics();
        //graphics=bufferImg.createGraphics();
        //Graphics2D g2d = bufferImg.createGraphics();
        //graphics.setBackground(new Color(0, 0, 0, 0));
        
        //graphics.fillRect(0, 0, ancho, alto);
        g.drawImage(bufferImg,0, 0, this);
        bufferImg.getGraphics().setColor(Color.WHITE);
        graphics.fillRect(0,0,ancho,alto);
        bufferImg.getGraphics().fillRect(0, 0, ancho, alto);
    }
    public int[][] rotacion(float angulo, int[][] p){
        
        float [][] rotacion={{0,0,0},{0,0,0},{0,0,1}};
        int [][] vectores=new int [p.length][3];
        int [][] vectoresRes=new int [p.length][3];
        float []res=new float[3];
        //Matriz con los puntos 
        vectores=obj.matrizHomogenea(p);
            rotacion[0][0]=(float)Math.cos(Math.toRadians(angulo));
            rotacion[0][1]=(float)-Math.sin(Math.toRadians(angulo));
            rotacion[1][0]=(float)Math.sin(Math.toRadians(angulo));
            rotacion[1][1]=(float)Math.cos(Math.toRadians(angulo));
            for(int k=0; k<p.length; k++){
                    res=obj.multiplicarMatriz(rotacion, vectores,k );
                    vectoresRes[k][0]=(int) res[0];
                    vectoresRes[k][1]=(int) res[1];
                    vectoresRes[k][2]=(int) res[2];
            } 
//                update(this.getGraphics());
//                dibujarTriangulo(vectoresRes);
//                hilo.run();
        
        return vectoresRes;
    }
    public void putPixel(int x, int y, Color c){
        if(x<0||x>ancho-1||y<0||y>alto-1){
            return;
        }else{
        buffer.setRGB(0, 0, c.getRGB());
        bufferImg.getGraphics().drawImage(buffer,x, y, this);}
    }
    public void bresenhamLine(int x1, int y1, int x2, int y2, Color c){
        int d1, d2, p, xk, yk;
        int dx, dy, stepy, stepx;
        if(x2<x1&&y2<y1){
            xk=x1; x1=x2; x2=xk;
            yk=y1; y1=y2; y2=yk;
        }
        dx=x2-x1;
        dy=y2-y1;
        if (dy < 0) { 
            dy = -dy; stepy = -1; 
          } 
        else stepy = 1;
          if (dx < 0) {  
            dx =-dx; stepx = -1; 
          } 
          else stepx=1;

        if(dx>dy){
                d1=2*dy;
                d2=2*(dy-dx);
                putPixel(x1, y1, c);
                p=2*dy-dx;
                xk=x1; 
                yk=y1;
                while(xk!=x2){
                    if(p>0){
                        xk+=stepx; yk+=stepy;
                        putPixel(xk,yk,c);
                        p=p+d2;
                    }else{
                        xk+=stepx; 
                        putPixel(xk,yk,c);
                        p=p+d1;
                    }
                }
        }else{
                d1=2*dx;
                d2=2*(dx-dy);
                putPixel(x1, y1, c);
                p=2*dy-dx;
                xk=x1; 
                yk=y1;
                while(yk!=y2){
                    if(p>0){
                        xk+=stepx; yk+=stepy;
                        putPixel(xk,yk,c);
                        p=p+d2;
                    }else{
                        yk+=stepy; 
                        putPixel(xk,yk,c);
                        p=p+d1;
                    }
                } 
        }  
    }
    public void fillrectangle(int x1, int y1, int x2, int y2, Color outside, Color inside){
        bresenhamLine(x1,y1,x2,y1,outside);
        bresenhamLine(x1,y2,x2,y2,outside);
        bresenhamLine(x1,y1,x1,y2,outside);
        bresenhamLine(x2,y1,x2,y2,outside);
        if(x2<x1&&y2<y1){
            int t;
            t=x1; x1=x2; x2=t;
            t=y1; y1=y2; y2=t;
        }
        for(int i=x1+1; i<=x2; i++){
            bresenhamLine(i,y1+1,i,y2-1,outside);
        }
    }
    public void polarCircle(int h, int k, int r, Color c){
        float y=0,x=0;
        int cont=0;
        for(float t=0; t<=360; t=(float) (t+0.2)){
            x=h+(float)(r*Math.cos(Math.toRadians(t)));
            y=k+(float)(r*Math.sin(Math.toRadians(t)));
            vector[cont][0]=Math.round(x);
            vector[cont][1]=Math.round(y);
            putPixel(Math.round(x), Math.round(y),c);
        }
    }
    public void fill(int x, int y,Color c){
        if(x<0||x>599||y<0||y>399)
            return;
        if(bufferImg.getRGB(x, y)!=c.getRGB()){
            //System.out.println("x: "+x+ " y: "+y);
            putPixel(x,y,c); 
            fill(x-1,y,c); 
            fill(x,y+1,c);
            fill(x+1,y,c);
            fill(x,y-1,c);    
        }  else{
            return;
        }
    }
    public void setColores(){
        colores[0]=new Color(233,210,192);
        colores[1]=new Color(212,184,170);
        colores[2]=new Color(107,43,16);
        colores[3]=new Color(86,25,7);
        colores[4]=new Color(241,119,44);
        colores[5]=new Color(229,93,45);
        colores[6]=new Color(213,69,32);
    }
private void escalacion(float s, int[][] p) {
        int [][] vectores=new int [3][p.length];
        int [][] vectoresRes=new int [p.length][3];
        float [][] escalamiento={{0,0,0},{0,0,0},{0,0,1}};
        float []res=new float[3];
        float deltaS;
        float factor=1;
        int cont=1;
        //Matriz con los puntos 
        vectores=obj.matrizHomogenea(p);
        
            
        if(s>=1)
            deltaS=(float)0.5;
        else
            deltaS=(float)1; 
        
            while(factor!=s){
                
                if (s >= 1) 
                    factor+=deltaS;
                 else {
                    factor=(float)1/(cont+deltaS);
                    System.out.println("factor"+factor+ "s: "+s);
                    cont++;
                }
                
                escalamiento[0][0]=factor;
                escalamiento[1][1]=factor;
               
                for(int k=0; k<p.length; k++){
                    res=obj.multiplicarMatriz(escalamiento, vectores,k );
                    vectoresRes[k][0]=(int) res[0];
                    vectoresRes[k][1]=(int) res[1];
                    vectoresRes[k][2]=(int) res[2];
                } 
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                yOrigen+=50;
                
                dibujarLineaOrigen(reacomodarVectorInverso(vectoresRes));
                actualiza(1000);
                
            }  
    }
    private void traslacion(int dx, int dy, int[][] p, int [][]l) {
        int [][] vectores=new int [p.length][3];
        int [][] vectoresRes=new int [p.length][3];
        float [][] traslacion={{1,0,0},{0,1,0},{0,0,1}};
        float []res=new float[3];
        int deltaX, deltaY, mcd, contX=1, contY=1, aux=1;
        
        //Obtenemos el maximo comun divisor y obtenemos con
        //deltaX y deltaY la razon de cambio por ejemplo 3 cada 2
        mcd=obj.getMCD(dx,dy);
        deltaX=dx/mcd;
        deltaY=dy/mcd;
        float razon=(float)dy/dx;
        
        razon=Math.round(razon);
        System.out.println("Razon: "+razon);
        System.out.println("Delta x "+ deltaX);
        System.out.println("Deltay "+ deltaY);
        //Matriz con los puntos 
        vectores=obj.matrizHomogenea(p);
        
        if(dx>=dy){
            while(contX<=dx){
                System.out.println("Traslacion x: "+contX+" y: "+contY);
                traslacion[0][2]=contX;
                traslacion[1][2]=contY;
                
                for(int k=0; k<p.length; k++){
                    res=obj.multiplicarMatriz(traslacion, vectores,k );
                    vectoresRes[k][0]=(int) res[0];
                    vectoresRes[k][1]=(int) res[1];
                    vectoresRes[k][2]=(int) res[2];
                } 
                bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                bufferImg.createGraphics();
                bresenhamLine(vectoresRes[0][0],vectoresRes[0][1],vectoresRes[1][0],vectoresRes[1][1],colores[4]);
                dibujarLinea(l);
                actualiza(200);
                
                if(aux<razon){
                    contX++;
                    aux++;
                }else{
                    if(aux==razon) contY++;
                    if(aux<deltaX)
                        contX++;
                    else{
                        aux=0;
                        contY++;
                        contX++;
                    }
                    aux++;
                }
                
            }
        }
    }
    @Override
    public void run() {
        while(true){
            
            limpiar();
            for(int i=20; i>0; i--){
                    //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                    polarCircle(300,360,10+i,colores[6]);
                    fill(300,360, colores[6]);
                    //fill(300,350, colores[6]);
                    actualiza(100);
            } 
            for(int i=0; i<20; i++){
//                    bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
//                    bufferImg.createGraphics();
                    polarCircle(300,360,10+i,colores[5]);
                    actualiza(100);
            }
            int col=5;
            for(int i=0; i<150; i=i+2){
                    bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                    bufferImg.createGraphics();
                    if(col==7)col=5;
                    polarCircle(300,360-i,30,colores[col]);
                    col++;
                    actualiza(20);
                }
            int [] radios={30,40,50,60,70,80,90,100,110,120,130,140};
//            
            destelloCirculo(300,210,0,1,radios);
            destelloCirculo(300,210,radios.length-1,-1,radios);
            int [] r={0,30,50,70,90,110,130,160,180,200,220,250,280,310,340,370,410,440};
            destelloCirculo(300,210,0,1,r);
           destelloCirculoDifuminar(300,210,0,1,r);
            
          
            moverLinea(270);
            //ACOMODO DE LAS PRIMERAS LINEAS
            int [][]p={{218,218},{218,268}};
            int[][]l={{118,298,118,348},{468,298,468,348},{368,218,368,268}};
            for(int i=0; i<=42; i++){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                if(i<20)
                    p[1][1]+=1;
                if(i<=28)
                    bresenhamLine(p[0][0]+i,p[0][1]-i,p[1][0]+i,p[1][1]-i,colores[4]);
                else
                    bresenhamLine(p[0][0]+i,p[0][1]-28,p[1][0]+i,p[1][1]-28,colores[4]);
                dibujarLinea(l);
                actualiza(20);
            }
            p[0][0]=368; p[0][1]=218; p[1][0]=368; p[1][1]=268;
            vector[0][0]=260; vector[0][1]=190; vector[0][2]=260; vector[0][3]=260;
            l[2][0]=260; l[2][1]=190; l[2][2]=260; l[2][3]=260;
            for(int i=0; i<=28; i++){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                if(i<20)
                    p[1][1]+=1;
                if(i<=28)
                    bresenhamLine(p[0][0]-i,p[0][1]-i,p[1][0]-i,p[1][1]-i,colores[4]);
                else
                    bresenhamLine(p[0][0]-i,p[0][1]-28,p[1][0]-i,p[1][1]-28,colores[4]);
                dibujarLinea(l);
                actualiza(20);
            }
            vector[1][0]=340; vector[1][1]=190; vector[1][2]=340; vector[1][3]=260;
            for(int i=0; i<=30; i++){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                bresenhamLine(340,220,340-i,220-(i/3),colores[4]);
                bresenhamLine(260,220,260+i,220-(i/3),colores[4]);
                dibujarLinea(vector);
                dibujarLinea(l);
                actualiza(50);
            }
            vector[2][0]=260; vector[2][1]=220; vector[2][2]=290; vector[2][3]=210;
            vector[3][0]=340; vector[3][1]=220; vector[3][2]=310; vector[3][3]=210;
            
            for(int i=0; i<=30; i++){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                if(i<=20){
                   bresenhamLine(290,210,290-i,210-i,colores[4]);
                   bresenhamLine(310,210,310+i,210-i,colores[4]);
                   
                   bresenhamLine(260,190,260+(i/2),190+i,colores[4]);
                   bresenhamLine(340,190,340-(i/2),190+i,colores[4]);
                   
                   bresenhamLine(290,210,290+i,210,colores[4]);
                }else{
                    bresenhamLine(290,210,290-i,190,colores[4]);
                    bresenhamLine(310,210,310+i,190,colores[4]);
                   
                   bresenhamLine(260,190,270,190+i-3,colores[4]);
                   bresenhamLine(340,190,330,190+i-3,colores[4]);
                   
                   bresenhamLine(290,210,310,210,colores[4]);
                }
                dibujarLinea(vector);
                dibujarLinea(l);
                actualiza(50);
            }
                vector[4][0]=290; vector[4][1]=210; vector[4][2]=260; vector[4][3]=190;
                vector[5][0]=310; vector[5][1]=210; vector[5][2]=340; vector[5][3]=190;
                
                vector[6][0]=270; vector[6][1]=217; vector[6][2]=260; vector[6][3]=190;
                vector[7][0]=330; vector[7][1]=217; vector[7][2]=340; vector[7][3]=190;
                
                vector[8][0]=290; vector[8][1]=210; vector[8][2]=310; vector[8][3]=210;
                int temp=0;
            for(int i=0; i<=142; i=i+2){//cambio tercera 142 x, 38 y y cuarta linea 128 x 38y
                
                if(i<=128)temp=i; else temp=128;
                if(i<=40){
                    if(i<=20){
                        l[0][3]--; 
                        l[1][3]--; 
                    }
                    bresenhamLine(l[0][0]+i,(int)(l[0][1]-(i/3.73)),l[0][2]+i+i,(int)(l[0][3]-(i/3.73)),colores[4]);
                    bresenhamLine(l[1][0]-temp,(int)(l[1][1]-(i/3.73)),l[1][2]-temp-i,(int)(l[1][3]-(i/3.73)),colores[4]);
                }else{
                    bresenhamLine(l[0][0]+i,(int)(l[0][1]-(i/3.73)),l[0][2]+i+40,(int)(l[0][3]-(i/3.73)),colores[4]);
                    bresenhamLine(l[1][0]-temp,(int)(l[1][1]-(i/3.73)),l[1][2]-temp-40,(int)(l[1][3]-(i/3.73)),colores[4]);
                }
                dibujarLinea(vector);
                actualiza(30);
            }
            vector[9][0]=260; vector[9][1]=260; vector[9][2]=300; vector[9][3]=300;
            vector[10][0]=340; vector[10][1]=260; vector[10][2]=300; vector[10][3]=300;
            
            for(int i=20; i<=50;i++){
                if(i<=30){
                    bresenhamLine(300,300,300+i,300-i,colores[4]);
                    bresenhamLine(300,300,300-i,300-i,colores[4]);
                }
                else{
                    bresenhamLine(300,300,300+i,270,colores[4]);
                    bresenhamLine(300,300,300-i,270,colores[4]);
                }
                dibujarLinea(vector);
                actualiza(50);
            }
            vector[11][0]=300; vector[11][1]=300; vector[11][2]=250; vector[11][3]=270;
            vector[12][0]=300; vector[12][1]=300; vector[12][2]=350; vector[12][3]=270;
            
            for(int i=0; i<10; i++){
                bresenhamLine(340,260,340+i,260+i,colores[4]);
                bresenhamLine(260,260,260-i,260+i,colores[4]);
                dibujarLinea(vector);
                actualiza(50);
            }
            vector[13][0]=340; vector[13][1]=260; vector[13][2]=350; vector[13][3]=270;
            vector[14][0]=260; vector[14][1]=260; vector[14][2]=250; vector[14][3]=270;
           dibujarLinea(vector);
           actualiza(300);
           dibujarLinea(vector);
           diamond(300,280,310,290,colores[3]);
           //bresenhamLine(xOrigen+v[i][0],yOrigen+v[i][1],xOrigen+v[i][2],yOrigen+v[i][3],colores[4]);
           fillrectangle(270,240,280,250, Color.BLACK, Color.BLACK);
           fillrectangle(310,240,320,250, Color.BLACK, Color.BLACK);
           actualiza(500);
           int vectorNuevo[][]=reacomodarVector(vector);
           escalacion((float) 2.5,vectorNuevo);
        }    
    }
    private void limpiar() {
        yOrigen=300;
        for(int i=0; i<vector.length; i++){
            for(int j=0; j<vector[0].length; j++){
                vector[i][j]=0;
            }
        }
    }
    public int[][] reacomodarVector(int [][]v){
        int res[][]=new int [v.length*2+6][2];
        int cont=0, cont2=0;
        for(int i=0; i<v.length; i++){
            System.out.println(""+v[i][0]+" "+v[i][1]+" "+v[i][2]+" "+v[i][3]);
        }
        for(int i=0; i<v.length*2; i++){
            for(int j=0; j<4; j++){
                if(cont2==2){ cont2=0; if(j==2)i++;}
                res[i][cont2]=v[cont][j]-300;
                cont2++;
            }
            cont++;
        }
        for(int i=0; i<res.length; i++){
            System.out.println(""+res[i][0]+" "+res[i][1]);
        }
        res[res.length-6][0]=270-300;
        res[res.length-6][1]=240-300;
        res[res.length-5][0]=280-300;
        res[res.length-5][1]=250-300;
        
        res[res.length-4][0]=310-300;
        res[res.length-4][1]=240-300;
        res[res.length-3][0]=320-300;
        res[res.length-3][1]=250-300;
        
        res[res.length-2][0]=300-300;
        res[res.length-2][1]=280-300;
        res[res.length-1][0]=310-300;
        res[res.length-1][1]=290-300;
        
        return res;
    }
    public int[][] reacomodarVectorInverso(int [][]v){
        System.out.println("Reacomodar");
        int res[][]=new int [v.length/2][4];
        int cont=0, cont2=0;
        for(int i=0; i<v.length; i++){
            for(int j=0; j<4; j++){
                if(cont2==2){ cont2=0; if(j==2)i++;}
                res[cont][j]=v[i][cont2];
                cont2++;
            }
            cont++;
        }
        for(int i=0; i<res.length; i++){
            System.out.println(""+res[i][0]+" "+res[i][1]+" "+res[i][2]+" "+res[i][3]);
        }
        return res;
    }
    public void moverLinea(int angulo){
        float anguloTemp=0;
        float deltaAngulo=5;
        int x=60, y=50;
        int AUMENTO=50;
        int p[][]={{x,y},{x+AUMENTO,y}};
        int res[][]=new int [2][2];
        bresenhamLine(0,30,30,60,colores[4]);
        while(angulo>=anguloTemp){
            //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
            //bufferImg.createGraphics();
            res=rotacion(anguloTemp,p);
            if(anguloTemp==angulo){
                System.out.println("1) "+(x+res[0][0])+" "+ (y-res[0][1])+" "+
                (x+res[1][0])+" "+ (y-res[1][1]));
                System.out.println("2)"+(x-res[0][0])+" "+ (y+250+res[0][1])+" "+
                (x-res[1][0])+" "+ (y+250+res[1][1]));
                System.out.println("3)"+(x+250+res[0][0])+" "+ (y+250+res[0][1])+" "+
                (x+250+res[1][0])+" "+ (y+250+res[1][1]));
                System.out.println("4)"+(x+250-res[0][0])+" "+ (y-res[0][1])+" "+
                (x+250-res[1][0])+" "+ (y-res[1][1]));
            }
            //System.out.println(""+res[0][0]+","+res[0][1]+" "+res[0][0]+","+res[0][1]);
            bresenhamLine(x+res[0][0], y-res[0][1],
                x+res[1][0], y-res[1][1], colores[4]);
            bresenhamLine(x-res[0][0], y+250+res[0][1],
                x-res[1][0], y+250+res[1][1], colores[4]);
            //if(anguloTemp<=180)
            bresenhamLine(x+250+res[0][0], y+250+res[0][1],
                x+250+res[1][0], y+250+res[1][1], colores[4]);
            
            bresenhamLine(x+250-res[0][0], y-res[0][1],
                x+250-res[1][0], y-res[1][1], colores[4]);
            x+=2;
            y+=2;
            anguloTemp+=deltaAngulo;
            actualiza(50);
        }
        
    }
    public void diamond(int x1, int y1, int x2, int y2, Color c){
        bresenhamLine(x1,y1,x2,y2,c);
        bresenhamLine(x1-(x2-x1),y2,x1,y1,c);
        bresenhamLine(x1-(x2-x1),y2,x1,y2+(y2-y1),c);
        bresenhamLine(x1,y2+(y2-y1),x2,y2,c);
    }
    public void dibujarLinea(int [][]v){
        for(int i=0; i<v.length; i++){
             bresenhamLine(v[i][0],v[i][1],v[i][2],v[i][3],colores[4]);
        }
    }
    public void dibujarLineaOrigen(int [][]v){
        for(int i=0; i<v.length-3; i++){
             bresenhamLine(xOrigen+v[i][0],yOrigen+v[i][1],xOrigen+v[i][2],yOrigen+v[i][3],colores[4]);
        }
        fillrectangle(xOrigen+v[v.length-3][0],yOrigen+v[v.length-3][1],
                xOrigen+v[v.length-3][2],yOrigen+v[v.length-3][3], Color.BLACK, Color.BLACK);
        fillrectangle(xOrigen+v[v.length-2][0],yOrigen+v[v.length-2][1],
                xOrigen+v[v.length-2][2],yOrigen+v[v.length-2][3], Color.BLACK, Color.BLACK);
        diamond(xOrigen+v[v.length-1][0],yOrigen+v[v.length-1][1],
                xOrigen+v[v.length-1][2],yOrigen+v[v.length-1][3], colores[3]);
    }
    public void destelloCirculo(int h, int k, int i, int step, int []radios){
        
        int color=4, cont=0;
        int fin;
        if(step>0) fin=radios.length-1;
        else fin=0;
        while(i!=fin){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                cont=0;
                while(cont<=i){
                    
                    if(color==6) color=4;else color++;
                    polarCircle(h,k,radios[cont],colores[color]);
                    if(cont==0) fill(h,k,colores[color]);
                    cont++;
                }
                actualiza(50);
                i+=step;
            }   
    }
    public void destelloCirculoDifuminar(int h, int k, int i, int step, int []radios){
        
        int color=4, cont=0;
        int fin;
        if(step>0) fin=radios.length-1;
        else fin=0;
        while(i!=fin){
                //bufferImg=new BufferedImage(ancho, alto,BufferedImage.TYPE_INT_ARGB);
                //bufferImg.createGraphics();
                cont=0+i;
                while(cont<radios.length){
                    if(color==6) color=4;else color++;
                    polarCircle(h,k,radios[cont],colores[color]);
                    if(cont==0) fill(h,k,colores[color]);
                    cont++;
                }
                actualiza(50);
                i+=step;
            }   
    }
    public void actualiza(int tiempo){
        try {
            update(getGraphics());
            hilo.sleep(tiempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Animacion2D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
