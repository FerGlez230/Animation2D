/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animacion2d;

/**
 *
 * @author maryf
 */
public class MetodosExtra {
    public MetodosExtra(){
        
    }
    

    public int[][] matrizHomogenea(int[][] p){
        int [][] vectores=new int [3][p.length];
        for(int i=0; i<p.length; i++){
            for(int j=0; j<3; j++){
               if(j==2)
                   vectores[j][i]=1;
                   else
                    vectores[j][i]=p[i][j];
            }  
        }
        return vectores;
    }

    public int getMCD(int num1, int num2){
        while(num1 != num2)
            if(num1>num2)
                num1= num1-num2;
            else
                num2= num2 -num1;
        
        return num1;
    }
    public float[] multiplicarMatriz(float [][] s, int[][] v, int pos){
        float []res=new float[3];
        float sum=0;
        for(int i=0; i<s.length; i++){
            for(int j=0; j<s.length; j++){
                sum+=s[i][j]*v[j][pos];}
            res[i]=sum;
            sum=0;
        }
        return res;
    }
}
