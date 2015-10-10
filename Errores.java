/*  Errores.java
 * @fileoverview Esta clase gestiona los errores generados por la clase Evaluadora
    *y los escribe en un archivo con extension .err de manera organizada
 * @version 1.0
 * @author Marcos Robles<ecko_lob@hotmail.com>
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

/**
 *
 * @author Marcos
 */
public class Errores {
    
   
    String error="";
    
     /**
     * Define los errores en etiquetas
     * @param{short}cuentalineas{int}tipo_de_error{File}seleccionado
     *@return{void}
      */ 
    public void errores_etiqueta(short cuentalineas, int tipo_de_error,File seleccionado)
    {
        
        //Automata que retorna el tipo de error que se encontro en la etiqueta
        switch(tipo_de_error)
        {
            case 0:      error ="La etiqueta no puede estar sola";
                        writeFileErrores(cuentalineas,error,seleccionado);
                break;
            case 1:      error ="Caracteres invalidos en la etiqueta";     
                        writeFileErrores(cuentalineas,error,seleccionado);
                break;
            case 2:   error = "Se sobrepaso el tamaño maximo (8 caracteres) en la etiqueta";
                     //linea_errores.writeFileErrores(hc12.seleccionado, linea.cuentalineas,linea_errores.error);
                       writeFileErrores(cuentalineas,error,seleccionado);
                break;
            
        }
        
    }
    
    /**
     * Define los errores en codops
     * @param{short}cuentalineas{int}tipo_de_error{File}seleccionado
     *@return{void}
      */ 
    public void errores_codop(short cuentalineas, int tipo_de_error, File seleccionado)
    {
        //Automata que retorna el tipo de error que se encontro en el codop
        switch(tipo_de_error)
        {
            case 1:error="Caracteres invalidos en el codop";
                  writeFileErrores(cuentalineas,error,seleccionado);
                break;
            case 2:error="Tamaño maximo(5) sobrepasado en el codop";
                  writeFileErrores(cuentalineas,error,seleccionado);
                break;
            case 3: error="El codop no se encuentra en el tabop"; 
                    writeFileErrores(cuentalineas,error,seleccionado);
                break;
        }
    }
    
    /**
     * Define los errores en operandos
     * @param{short}cuentalineas{int}tipo_de_error{File}seleccionado
     *@return{void}
      */ 
    public void errores_operando(short cuentalineas, int tipo_de_error,File seleccionado)
    {
        //Automata que retorna el tipo de error que se encontro en el operando
        switch(tipo_de_error)
        {
            case 1:  error="Requiere operando";
               writeFileErrores(cuentalineas,error,seleccionado);
                break;
            case 2:  error="No requiere operando";
               writeFileErrores(cuentalineas,error,seleccionado);
                break;
             case 3:  error="Modo IMM requiere # al inicio";
               writeFileErrores(cuentalineas,error,seleccionado);
                break;
             case 4: error="Este numero no es binario (El simbolo % indica que debe ser binario)";
                  break;
             case 5: error="Este numero no es octal (El simbolo @ indica que debe ser octal)";
                  break;
             case 6: error="Este numero no es hexadecimal (El simbolo $ indica que debe ser hexadecimal)";
                  break;
             case 7: error="Este numero no es decimal";
                  break;
            
        }
    }
    
    /**
     * Define los errores en la directiva end
     * @param{short}cuentalineas{int}tipo_de_error{File}seleccionado
     *@return{void}
      */ 
    public void errores_end(short cuentalineas, int tipo_de_error,File seleccionado)
    {
       //Automata que retorna el tipo de error que se encontro en la directiva END
        switch(tipo_de_error)
        {
            case 1:  /*error = "Directiva END en posicion incorrecta";
              writeFileErrores(cuentalineas,error,seleccionado);*/
                break;  
            case 2: error="No se encontro la directiva END";
                 writeFileErrores(cuentalineas,error,seleccionado);
        } 
    }
    
    
    /**
     * Escribe los errores en un archivo con extension .err
     * @param{short}cuentalineas{int}tipo_error{File}seleccionado
     *@return{void}
      */ 
     public void writeFileErrores(short cuentalineas,String tipo_error,File seleccionado) 
    { 
          
       // Linea linea = new Linea();
        String path_err=""; 
        String [] guarda = new String[2];
       
        StringTokenizer tokenizarnombre = new StringTokenizer(seleccionado.getName(),".");
         byte x=0;
                      while (tokenizarnombre.hasMoreTokens()) 
                        {  
                            guarda[x]=tokenizarnombre.nextToken();
                             x++;
                        }
        path_err = seleccionado.getParent()+"\\"+guarda[0]+".err";
       try
        {  
           BufferedWriter bwe = new BufferedWriter(new FileWriter(path_err,true));  
           BufferedReader bre = new BufferedReader(new FileReader(path_err));
            bwe.newLine();
             bwe.write(cuentalineas+"\t"+tipo_error);
             bwe.newLine(); 
          bwe.close();
        }
        catch(FileNotFoundException fnfex){//Si no se encuentra el archivo, le notifica al usuario
            System.out.println(fnfex.getMessage()+"No se encontro el archivo");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
 
    }
    
    
    
    
}
