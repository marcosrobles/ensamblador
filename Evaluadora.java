/* Evaluadora.java
 * @fileoverview Esta clase recibe los tokens y los evalua segun su categoria
 * @version 1.0
 * @author Marcos Robles<ecko_lob@hotmail.com>
 */
package ensamblador;
import java.io.File;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import   java.util.regex.Pattern;
/**
 *
 * @author Marcos
 */
public class Evaluadora {
      Ensamblador hc12 = new Ensamblador();
      Errores maneja_errores = new Errores();
      LineaTabop linea_tabop = new LineaTabop();
     // Arbol maneja_modos_dir = new Arbol();
      String lineainstruccion="";
      String []lineamodos=new String[5];
 
    
      String requiereoperando="";
       boolean et_valida, codop_valido,op_valido=false;
      
  
       
      
    /**
     * Evalua la etiqueta mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}etiqueta,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
        
       
        
    
        
    public void EvaluarEtiqueta(String etiqueta,short cuentalineas ,File seleccionado,boolean et_sola)
    {

        int longitud=0;
        
         int etiqueta_mayus = etiqueta.compareToIgnoreCase("END"); 
        if(etiqueta_mayus == 0)//Encontro la directiva END en la posicion incorrecta y retorna error
        {
            maneja_errores.errores_end(cuentalineas,1,seleccionado); 
            hc12.end_encontrado=true;
        }
         //Expresion regular para evaluar etiquetas
        Pattern patron_etiqueta = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{0,7}");
        Matcher comprobador = patron_etiqueta.matcher(etiqueta);
        et_valida = comprobador.matches();
        
        if((longitud = etiqueta.length()) > 8)
                 {               
                     maneja_errores.errores_etiqueta(cuentalineas,2,seleccionado);  
                     et_valida=false;
                 }
        
       
        if(et_valida==true)
        {
             
           
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
        
         
        
    }
    
    /**
     * Evalua el codop mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}codop,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
    public void EvaluarCodop(String codop, short cuentalineas, File seleccionado)
    {
        
        int longitud=0;
        int codop_mayus = codop.compareToIgnoreCase("END"); 
        if(codop_mayus == 0)//Encontro la directiva END en la posicion correcta y paramos la lectura
        {
            hc12.end_encontrado=true;
            System.exit(0);
        }
        
         //Expresion regular para evaluar el codop
        Pattern patron_etiqueta = Pattern.compile("[a-zA-Z]([a-zA-Z]{0,4}|[.]{1})");
        Matcher comprobador = patron_etiqueta.matcher(codop);
        codop_valido = comprobador.matches();
        
          if((longitud = codop.length()) > 5)
                 {               
                     maneja_errores.errores_codop(cuentalineas,2,seleccionado);  
                     codop_valido=false;
                 }
        
       
        
        if(codop_valido==false)
        {
            
            //mandar llamar metodo manejador de errores
            maneja_errores.errores_codop(cuentalineas,1,seleccionado);
        }
        if(codop_valido==true)
        {
           hc12.leerTabop(codop);  
           if(hc12.codop_encontrado==false)
           {
               codop_valido=false;
               maneja_errores.errores_codop(cuentalineas,3,seleccionado);
           }
           
        }
       
    }
    
    /**
     * Evalua el operando mediante el uso de expresiones regulares y funciones de la clase String
     * @param{String}operando,{short cuentalineas,File seleccionado}
     *@return{void}
      */ 
    
     
    
    
    public void EvaluarOperando(String operando, short cuentalineas, File seleccionado)
    {
        if(codop_valido==false)//si es un codop invalido no es necesario evaluar el codop
        {
            
        }
        else 
        {          
        switch(requiereoperando)//todos solo entran al switch si requieren operando a exepcion del inherente
        {
                  case "INH": 
                                if(operando==null)
                            {
                                 op_valido=true;
                            }
                                else
                                {
                                    
                                   maneja_errores.errores_operando(cuentalineas, 2, seleccionado);
                                   op_valido=false;
                                }
                                
                                break;
                  case "INM":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                       
                   case "DIR":  
                                if(operando==null)
                            {
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                                op_valido=false;
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "EXT":  
                                if(operando==null)
                            {
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                                op_valido=false;
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "IDX":  
                                if(operando==null)
                            {
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                                op_valido=false;
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "IDX1": 
                                if(operando==null)
                            {
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                                op_valido=false;
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "IDX2": 
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "[D,IDX]":
                                    if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "[IDX2]":
                                    if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   case "REL":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                                op_valido=true;
                            }
                                break;
                   
                   default: {
                       if(operando!=null)
            {//si el codop no fue encontrado en tabop, decirle aqui que no evalue el operando o asi
                 int operando_mayus = operando.compareToIgnoreCase("END"); 
                 if(operando_mayus == 0)//Encontro la directiva END en la posicion incorrecta y retorna error
                 {
                   //Indicar que se encontro el end o parar el programa, aunque segun recuerdo eso lo hago en otra clase
                 }
                 op_valido=false;
                maneja_errores.errores_operando(cuentalineas, 2, seleccionado);
            }
            
                       }
        }
      
                
        }
    }
    
    
    public void EvaluarModosDir()
    {
       
        for(String elemento:hc12.modoslinea)
        {
             if(elemento!=null)
             {
                System.out.println(elemento);
               StringTokenizer tokenizar = new StringTokenizer(elemento);
                byte x=0;
                      while (tokenizar.hasMoreTokens()) 
                        {  
                            lineamodos[x]=tokenizar.nextToken();
                             x++;
                        }
                      int bytesporcalcular = Integer.parseInt(lineamodos[2]);
               switch(lineamodos[0])
               {
                   case "INH":    requiereoperando="INH";//ya se que no requiere operando, pero lo mando a evaluar operando  
                                break;
                   case "INM":  System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="INM";
                                   }
                                break;
                       
                   case "DIR":  System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="DIR";
                                   }
                                break;
                   case "EXT":  System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="EXT";
                                   }
                                break;
                   case "IDX":  System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="IDX";
                                   }
                                break;
                   case "IDX1": System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="IDX1";
                                   }
                                break;
                   case "IDX2": System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="IDX2";
                                   }
                                break;
                   case "[D,IDX]":System.out.println("Modo:"+lineamodos[0]);
                                    bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="[D,IDX]";
                                   }
                                break;
                   case "[IDX2]":System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="[IDX2]";
                                   }
                                break;
                   case "REL":  System.out.println("Modo:"+lineamodos[0]);
                                bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando="REL";
                                   }
                                break;
                   
                   default: System.out.println("No creo que caiga aqui");
               }
               
             }
             
        }
       
        
     
        
    }
    
    
    
}
