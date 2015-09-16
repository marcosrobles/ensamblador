/* Ensamblador.java
 * @fileoverview Esta clase gestiona los archivos .asm .inst y .err  
 * lectura y escritura en los respectivos con manejo de buffers
 * @version 1.0
 * @author Marcos Robles<ecko_lob@hotmail.com>
 * History
 */
package ensamblador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;

public class Ensamblador {

    File seleccionado=null;
   
    Errores maneja_errores = new Errores();
    Evaluadora evalua_lineas = new Evaluadora();
    
    
    /**
     * Este metodo lee un archivo .asm linea por linea lo guarda en un buffer y lo muestra en pantalla
     * Asimismo escribe un encabezado en los archivos con extension .inst y .err
     * @param{void}
     *@return{void}
      */   
     public void ReadFile() 
    { 
        Linea linea = new Linea();
        JFileChooser selArchivo = new JFileChooser("D:\\Documents\\NetBeansProjects\\Ensamblador\\src\\ensamblador\\");
        selArchivo.showOpenDialog(null);
        seleccionado = selArchivo.getSelectedFile(); 
        
      
        if(seleccionado==null)
        {
            System.out.println("No selecciono ningun archivo");
            System.exit(0);
        }
        
        //ENCABEZADO DE ARCHIVO DE INSTRUCCIONES Y ARCHIVO DE ERRORES
         String path_inst,path_err=""; 
        String [] guarda = new String[2];
       
        StringTokenizer tokenizarnombre = new StringTokenizer(seleccionado.getName(),".");
         byte x=0;
                      while (tokenizarnombre.hasMoreTokens()) 
                        {  
                            guarda[x]=tokenizarnombre.nextToken();
                             x++;
                        }
        path_inst = seleccionado.getParent()+"\\"+guarda[0]+".inst";
        path_err = seleccionado.getParent()+"\\"+guarda[0]+".err";
       try
        {  
           BufferedWriter bwi = new BufferedWriter(new FileWriter(path_inst));  
           BufferedReader bri = new BufferedReader(new FileReader(path_inst));

             bwi.write("LINEA ETIQUETA CODOP    OPERANDO");
             bwi.newLine();
             bwi.write("-----------------------------------------");
          bwi.close();
        }
        catch(FileNotFoundException fnfex){//Si no se encuentra el archivo, le notifica al usuario
            System.out.println(fnfex.getMessage()+"No se encontro el archivo");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
       
       //ARCHIVO DE ERRORES
       try
        {  
           BufferedWriter bwe = new BufferedWriter(new FileWriter(path_err));  
           BufferedReader bre = new BufferedReader(new FileReader(path_err));
             bwe.write("LINEA ERROR");
             bwe.newLine();
             bwe.write("-----------------------------------------");
          bwe.close();
        }
        catch(FileNotFoundException fnfex){//Si no se encuentra el archivo, le notifica al usuario
            System.out.println(fnfex.getMessage()+"No se encontro el archivo");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       //ARCHIVO ASM
        try
        {    
           BufferedReader br = new BufferedReader(new FileReader(seleccionado));  
           String cadena=null;
            
             while((cadena=br.readLine()) != null){ //mientras halla algo que leer        
                System.out.println(cadena);
                linea.quitarcomentarios(cadena);
                linea.separarlinea(seleccionado);               
            }  
             if(evalua_lineas.end_encontrado==false)
             {
                 maneja_errores.errores_end(linea.cuentalineas,2,seleccionado);
             }
          br.close();
        }
        catch(FileNotFoundException fnfex){//Si no se encuentra el archivo, le notifica al usuario
            System.out.println(fnfex.getMessage()+"No se encontro el archivo");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
      
    }
    
     
    
    
    
    /**
     * Este metodo escribe los tokens de manera organizada en un archivo con 
        *extension .inst
     * @param{String}lineainstrucciones,{File}seleccionado
     *@return{void}
      */ 
     public static  void writeFileInst(String lineainstrucciones,File seleccionado) 
    { 
          
       // Linea linea = new Linea();
        String path_inst=""; 
        String [] guarda = new String[2];
       
        StringTokenizer tokenizarnombre = new StringTokenizer(seleccionado.getName(),".");
         byte x=0;
                      while (tokenizarnombre.hasMoreTokens()) 
                        {  
                            guarda[x]=tokenizarnombre.nextToken();
                             x++;
                        }
        path_inst = seleccionado.getParent()+"\\"+guarda[0]+".inst";
       try
        {  
           BufferedWriter bw = new BufferedWriter(new FileWriter(path_inst,true));  
           BufferedReader br = new BufferedReader(new FileReader(path_inst));
            bw.newLine();//Como escribir a partir del fin del archivo
             bw.write(lineainstrucciones);
             bw.newLine(); 
          bw.close();
        }
        catch(FileNotFoundException fnfex){//Si no se encuentra el archivo, le notifica al usuario
            System.out.println(fnfex.getMessage()+"No se encontro el archivo");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
 
    }
    
    public static void main(String[] args) {
       Ensamblador hc12 = new Ensamblador();
       hc12.ReadFile();   
    }  
}
