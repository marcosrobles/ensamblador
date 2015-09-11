/* Evaluadora.java
 * @fileoverview Esta clase recibe los tokens y los evalua segun su categoria
 * @version 1.0
 * @author Marcos Robles<ecko_lob@hotmail.com>
 */
package ensamblador;
import java.io.File;
import java.util.regex.Matcher;
import   java.util.regex.Pattern;
/**
 *
 * @author Marcos
 */
public class Evaluadora {
      Errores maneja_errores = new Errores();
      String [] cadenainst = new String[3];
      String lineainstruccion="";
      boolean end_encontrado=false;
     
      
    /**
     * Evalua la etiqueta mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}etiqueta,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
    public void EvaluarEtiqueta(String etiqueta,short cuentalineas ,File seleccionado,boolean et_sola)
    {

        boolean et_valida=false;
        int longitud=0;
        
         int etiqueta_mayus = etiqueta.compareToIgnoreCase("END"); 
        if(etiqueta_mayus == 0)//Encontro la directiva END en la posicion incorrecta y retorna error
        {
            maneja_errores.errores_end(cuentalineas,1,seleccionado); 
            end_encontrado=true;
        }
         //Expresion regular para evaluar etiquetas
        Pattern patron_etiqueta = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]+");
        Matcher comprobador = patron_etiqueta.matcher(etiqueta);
        et_valida = comprobador.matches();
       
        if(et_valida==true)
        {
               cadenainst[0]=etiqueta;
            EscribeLinea(cadenainst, seleccionado,cuentalineas);
          if(et_sola==true)
          {
              //etiqueta sola
             maneja_errores.errores_etiqueta(cuentalineas,0,seleccionado); 
          }
        }
        if(et_valida==false)
        {
            //mandar llamar objeto manejador de errores
            maneja_errores.errores_etiqueta(cuentalineas,1,seleccionado);
        }
        
         if((longitud = etiqueta.length()) > 8)
                 {               
                     maneja_errores.errores_etiqueta(cuentalineas,2,seleccionado);            
                 }
        
    }
    
    /**
     * Evalua el codop mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}codop,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
    public void EvaluarCodop(String codop, short cuentalineas, File seleccionado)
    {
        
        boolean codop_valido=false;
        int longitud=0;
        int codop_mayus = codop.compareToIgnoreCase("END"); 
        if(codop_mayus == 0)//Encontro la directiva END en la posicion correcta y paramos la lectura
        {
            end_encontrado=true;
            System.exit(0);
        }
        
         //Expresion regular para evaluar el codop
        Pattern patron_etiqueta = Pattern.compile("[a-zA-Z][a-zA-Z.]{0,4}");
        Matcher comprobador = patron_etiqueta.matcher(codop);
        codop_valido = comprobador.matches();
       
        if(codop_valido==true)
        {
           cadenainst[1]=codop;
            EscribeLinea(cadenainst, seleccionado,cuentalineas);
        }
        if(codop_valido==false)
        {
            
            //mandar llamar metodo manejador de errores
            maneja_errores.errores_codop(cuentalineas,1,seleccionado);
        }
        
         if((longitud = codop.length()) > 5)
                 {               
                     maneja_errores.errores_codop(cuentalineas,2,seleccionado);            
                 }
        
    }
    
    /**
     * Evalua el operando mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}operando,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
    public void EvaluarOperando(String operando, short cuentalineas, File seleccionado)
    {
          cadenainst[2]=operando;
           EscribeLinea(cadenainst, seleccionado,cuentalineas);
          int operando_mayus = operando.compareToIgnoreCase("END"); 
        if(operando_mayus == 0)//Encontro la directiva END en la posicion incorrecta y retorna error
        {
            maneja_errores.errores_end(cuentalineas,1,seleccionado);
        }
    }
    
    
    public void EscribeLinea(String[] cadenainst,File seleccionado,short cuentalineas)
    {
        String etiqueta,codop,operando="";
        for(String elemento:cadenainst){
           elemento="";   
        }
       etiqueta = cadenainst[0];
       codop = cadenainst[1];
       operando = cadenainst[2];
           lineainstruccion = cuentalineas+"\t"+etiqueta+"\t"+codop+"\t"+operando;
        Ensamblador.writeFileInst(lineainstruccion, seleccionado);
      }
    
}
