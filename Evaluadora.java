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

import static java.lang.Integer.bitCount;
import static java.lang.Integer.highestOneBit;
import static java.lang.Integer.lowestOneBit;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.reverse;
import static java.lang.Integer.reverseBytes;
import static java.lang.Integer.signum;
import static java.lang.Integer.toBinaryString;
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
      String modos="";
 
    
      String []requiereoperando=new String[12];
      boolean norequiereop,requiereop=false;
       boolean et_valida, codop_valido,op_valido=false;
       boolean es_binario,es_octal,es_hex,es_dec=false;
       int rango;
         String [] modo_ok = new String[13];
  
       
      
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
         
         for(int a=0;a<=11;a++)
                      {                   
                        modo_ok[a]=null; 
                      }
        
         
        requiereop=false;
        norequiereop=false;
        char  basenum=' ';
      
        if(codop_valido==false)//si es un codop invalido no es necesario evaluar el operando
        {
            
        }
        else 
        {       
          //aqui inicializar a requiere operando
          EvaluarModosDir(operando,cuentalineas,seleccionado);
          //si llega a este punto es porque ya evalue que ese modo de direccionamiento y requiere operando
          //si no requiere operando, no va a evaluarse en el switch
              
             for(String elemento:requiereoperando)
        {  
             
           if(elemento==null)//si elemento no contiene nada significa que no requirio operando(no aplica para inherente)
           {//ese codop con ese modo de direccionamiento no requiere operando
               if(operando!=null) //y si tiene operando manda error(no requiere operando)
               {
                   norequiereop=true;
                   //Esta variable sirve para indicar este error posteriormente  
                   
               }
           }
           else  //si elemento contiene algo significa que requiere operando(en cualquier modo de direccionamiento menos inherente)                          
           {
 
             if(modo_ok[0]=="INH"||modo_ok[1]=="IMM8"||modo_ok[2]=="IMM16"
                     ||modo_ok[3]=="DIR"||modo_ok[4]=="EXT"||modo_ok[5]=="IDX"||modo_ok[6]=="IDX1"||modo_ok[7]=="IDX2"
                     ||modo_ok[8]=="[D,IDX]"||modo_ok[9]=="[IDX2]"||modo_ok[10]=="REL8"||modo_ok[11]=="REL9"||modo_ok[12]=="REL16")//Si op_valido es true ya no necesito evaluar el operando en mas modos, creo
              {
                   break; 
                   
              }
             
             else
             {
             
            if(operando==null)  
            {
                
                if(elemento=="INH")//y si no tiene operando pero es INH esta bien
                {
                    if(modo_ok[0]=="INH"||modo_ok[1]=="IMM8"||modo_ok[2]=="IMM16"
                     ||modo_ok[3]=="DIR"||modo_ok[4]=="EXT"||modo_ok[5]=="IDX"||modo_ok[6]=="IDX1"||modo_ok[7]=="IDX2"
                     ||modo_ok[8]=="[D,IDX]"||modo_ok[9]=="[IDX2]"||modo_ok[10]=="REL8"||modo_ok[11]=="REL9"||modo_ok[12]=="REL16")
                                {
                                 break;  
                                }
                              
                                 op_valido=true;
                                 modo_ok[0]="INH";
                }
                else   //Entonces si el operando es null,y no es el modo inherente, indico en el archivo de errores que se requiere un operando 
                {
                                    requiereop=true;                                                  
                }        
            }
            else
            {
                //si requiere operando y tiene operando
            //Evaluo el operando segun el modo de direccionamiento identificado
                
             if(elemento=="INH")//y es INH esta mal
                {
                   
                              norequiereop=true;
                                 op_valido=false;
                                 modo_ok[0]="";
                }
            
           
            
            
           
        switch(elemento)//todos solo entran al switch si requieren operando a exepcion del inherente
        {
                  case "IMM8":  
                      
                               
                                 //evaluar operando y validarlo
                                boolean inm = operando.startsWith("#",0);
                                   if(inm==true)//si operando inicia con #
                                   { //entonces evaluaremos lo demas.
                                     
                                       
                                       
                                       basenum = operando.charAt(1);//obtenener que base numerica es
                                       switch(basenum)  
                                       {
                                           
                                           //Determinar si es de 8 bits(1 byte) o 16 bits(2 bytes) en bases a los bytescalculados de TABOP.txt 
                                            //Usar expresiones regulares que evaluen si en verdad 
                                            //el operando corresponde a la base numerica especificada
                                           //evaluar si verdaderamente corresponde  a la base especificada          
                                           //convertirlo a la base especificada
                                           //Evaluar si excede el rango de 8 o 16 bit segun la base.
                                           case '%'://BINARIO
                                                    rango=0;
                                                    Pattern patron_base_binaria = Pattern.compile("(0|1)+");
                                                    String suboperando=""; 
                                                    suboperando=operando.substring(2);
                                                    Matcher comprobadorbinario = patron_base_binaria.matcher(suboperando);
                                                    es_binario = comprobadorbinario.matches();
                                                    if(es_binario==true)
                                                    {
                                                      rango =  Integer.parseInt(suboperando,10);// convierte el numero N a base B 
                                                      int rango3 = reverse(rango);
                                                      String rango4 = toBinaryString(rango3);
                                                      
                                                       int zeros = numberOfLeadingZeros(rango3);
                                                       int signo = signum(rango);
                                                      
                                                    //  String rango3=Integer.toString(rango2,10);
                                                       
                                                      
                                                     //para conocer o entender  que rango tiene 
                                                      if(rango>255)
                                                      {
                                                          System.out.println("Error: Excede el rango de 8 bits: ");  
                                                      }
                                                        
                                                    
                                                      System.out.println("rango: "+rango);  
                                                      System.out.println("reverse: "+rango4);
                                 
                                                          System.out.println("zeros: "+zeros);
                                                          System.out.println("signo: "+signo);
                                                     op_valido=true;
                                                      modo_ok[1]="IMM8";   
                                                    }
                                                    else
                                                    {
                                                     maneja_errores.errores_operando(cuentalineas, 4, seleccionado);
                                                    op_valido=false;
                                                    }
                                                     
                                                  
                                                      break;
                                           case '@'://OCTAL
                                                    Pattern patron_base_octal = Pattern.compile("(0-7)+");
                                                    Matcher comprobadoroctal = patron_base_octal.matcher(operando);
                                                    es_octal = comprobadoroctal.matches();
                                                    if(es_octal==true)
                                                    {
                                                       op_valido=true;
                                                        modo_ok[1]="IMM8";  
                                                    }
                                                    else
                                                    {
                                                       op_valido=false;
                                                         maneja_errores.errores_operando(cuentalineas, 5, seleccionado); 
                                                    }
                                                      break;
                                           case '$': //HEXADECIMAL
                                                    Pattern patron_base_hex = Pattern.compile("[0-9A-Fa-f]+");
                                                    Matcher comprobador_hex = patron_base_hex.matcher(operando);
                                                    es_hex = comprobador_hex.matches();
                                                    if(es_hex==true)
                                                    {
                                                       op_valido=true;
                                                      modo_ok[1]="IMM8";   
                                                    }
                                                    else
                                                    {
                                                     op_valido=false;
                                                     maneja_errores.errores_operando(cuentalineas, 6, seleccionado);   
                                                    }
                                                      break;
                                           default: //DECIMAL 
                                                    //evaluar primero que si no es decimal no entre, 
                                                    //por ejemplo si el operando empieza con un simbolo raro o punto o asi.
                                                    Pattern patron_base_decimal = Pattern.compile("[0-9]+");
                                                    Matcher comprobador_decimal = patron_base_decimal.matcher(operando);
                                                    es_dec = comprobador_decimal.matches();
                                                    if(es_dec==true)
                                                    {
                                                         op_valido=true;
                                                      modo_ok[1]="IMM8"; 
                                                    }
                                                    else
                                                    {
                                                      op_valido=false;
                                                      maneja_errores.errores_operando(cuentalineas, 7, seleccionado); 
                                                    }
                                                    break;
                                       }
                                   }
                                   
                                
                            
                                break;
                   case "IMM16":  
                               
                                 //evaluar operando y validarlo
                                boolean inm16 = operando.startsWith("#",0);
                                   if(inm16==true)//si operando inicia con #
                                   { //entonces evaluaremos lo demas.
                                      
                                       
                                       
                                       basenum = operando.charAt(1);//obtenener que base numerica es
                                       switch(basenum)  
                                       {
                                           
                                           //Determinar si es de 8 bits(1 byte) o 16 bits(2 bytes) en bases a los bytescalculados de TABOP.txt 
                                            //Usar expresiones regulares que evaluen si en verdad 
                                            //el operando corresponde a la base numerica especificada
                                           //evaluar si verdaderamente corresponde  a la base especificada
                                           //Evaluar si excede el rango de 8 o 16 bit segun la base.
                                           //convertirlo a la base especificada
                                           case '%'://BINARIO
                                                    Pattern patron_base_binaria = Pattern.compile("(0|1)+");
                                                    Matcher comprobadorbinario = patron_base_binaria.matcher(operando);
                                                    es_binario = comprobadorbinario.matches();
                                                    if(es_binario==false)
                                                    {
                                                         op_valido=false;
                                                         maneja_errores.errores_operando(cuentalineas, 4, seleccionado);
                                                    }
                                                  
                                                      break;
                                           case '@'://OCTAL
                                                    Pattern patron_base_octal = Pattern.compile("(0-7)+");
                                                    Matcher comprobadoroctal = patron_base_octal.matcher(operando);
                                                    es_octal = comprobadoroctal.matches();
                                                    if(es_octal==false)
                                                    {
                                                         op_valido=false;
                                                         maneja_errores.errores_operando(cuentalineas, 5, seleccionado);
                                                    }
                                                      break;
                                           case '$': //HEXADECIMAL
                                                    Pattern patron_base_hex = Pattern.compile("[0-9A-Fa-f]+");
                                                    Matcher comprobador_hex = patron_base_hex.matcher(operando);
                                                    es_hex = comprobador_hex.matches();
                                                    if(es_hex==false)
                                                    {
                                                         op_valido=false;
                                                         maneja_errores.errores_operando(cuentalineas, 6, seleccionado);
                                                    }
                                                      break;
                                           default: //DECIMAL 
                                                    Pattern patron_base_decimal = Pattern.compile("[0-9]+");
                                                    Matcher comprobador_decimal = patron_base_decimal.matcher(operando);
                                                    es_dec = comprobador_decimal.matches();
                                                    if(es_dec==false)
                                                    {
                                                         op_valido=false;
                                                         maneja_errores.errores_operando(cuentalineas, 7, seleccionado);
                                                    }
                                                    break;
                                       }
                                   }
                                   else //operando en modo IMM no inicio con #
                                   { //escribimos error en archivo .err
                                       maneja_errores.errores_operando(cuentalineas, 3, seleccionado);
                                       op_valido=false;
                                   }
                                
                            
                                break;   
                       
                   case "DIR":  
                                
                                break;
                   case "EXT":  
                                
                                break;
                   case "IDX":  
                                
                                break;
                   case "IDX1": 
                                
                                break;
                   case "IDX2": 
                                
                                break;
                   case "[D,IDX]":
                                    
                                break;
                   case "[IDX2]":
                                  
                                break;
                   case "REL8":  
                              
                                break;
                    case "REL9":  
                               
                                break;
                    case "REL16":  
                               
                                break;
                   
                   default: {
                        //Indicar error de modo de direccionameinto desconocido
                       
            
                       }
         }
        
       }//llave else (si operando no es null)
             }
          
          
            
           }
          
           
      }    //llave for each 
        if(norequiereop==true)
        {
                   maneja_errores.errores_operando(cuentalineas, 2, seleccionado);
                   op_valido=false;
        }
        if(requiereop==true)
        {
            maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
            op_valido=false; 
        }
     }
          
    }
    
    
    public void EvaluarModosDir(String operando, short cuentalineas, File seleccionado)//EL PROBLEMA ESTA AQUI , porque en el arreglo requiereoperando
                                //se queda el modo INH del codop anterior, siendo que el codop 
                                //reciente ya no tiene ese modo de direccionamiento
    {
        int bytesporcalcular=0; 
       for(int z=0;z<=11;z++)
       {
           this.requiereoperando[z]="";
       }

        if(codop_valido==false)//si es un codop invalido no es necesario evaluar el modo de direccionamiento
        {
            
        }
        else
        {
            modos="";
        
        Arbol maneja_modos_dir = new Arbol();
        for(String elemento:hc12.modoslinea)//este es el contenido del arbol que guarda los modos de 
                                            //direccionamiento de cada codop
        {
                  
           
       
             if(elemento!=null)
             {
           
               StringTokenizer tokenizar = new StringTokenizer(elemento);
                byte x=0;
                      while (tokenizar.hasMoreTokens()) 
                        {  
                           //aqui dividimos la linea para 
                             lineamodos[x]=tokenizar.nextToken();  
                           
                             x++;
                        }
                      
              
             
                        
                        
                                modos=modos+" "+lineamodos[0]; 
                               
                                    bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                    
                                    
                               
                                
                          
                      
                      //IDENTIFICO MODOS DE DIRECCIONAMIENTO DEL CODOP 
                      //Este switch identifica que modo de direccionamiento es y si requiere operando
               switch(lineamodos[0])
               {
                   case "INH":  
             
                                System.out.println("Modo:"+lineamodos[0]);
                                requiereoperando[0]="INH";//ya se que no requiere operando, pero lo mando a evaluar operando  
                                break;
                   case "IMM8":  System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//si requiere operando
                                   {
                                      requiereoperando[1]="IMM8";   
                                   }
                                   
                                break;
                    case "IMM16":  System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[2]="IMM16";
                                     
                                   }
                                break;
                       
                   case "DIR":  System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[3]="DIR";
                                   }
                                break;
                   case "EXT":  System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[4]="EXT";
                                   }
                                break;
                   case "IDX":  System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[5]="IDX";
                                   }
                                break;
                   case "IDX1": System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[6]="IDX1";
                                   }
                                break;
                   case "IDX2": System.out.println("Modo:"+lineamodos[0]);
                                 bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[7]="IDX2";
                                   }
                                break;
                   case "[D,IDX]":System.out.println("Modo:"+lineamodos[0]);
                                    bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[8]="[D,IDX]";
                                   }
                                break;
                   case "[IDX2]":System.out.println("Modo:"+lineamodos[0]);
                                  bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[9]="[IDX2]";
                                   }
                                break;
                   case "REL8":  System.out.println("Modo:"+lineamodos[0]);
                                bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[10]="REL8";
                                   }
                                break;
                   case "REL9":  System.out.println("Modo:"+lineamodos[0]);
                                bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[11]="REL9";
                                   }
                                break;
                   case "REL16":  System.out.println("Modo:"+lineamodos[0]);
                                bytesporcalcular = Integer.parseInt(lineamodos[2]);
                                   if(bytesporcalcular>0)//requiere operando
                                   {
                                      requiereoperando[12]="REL16";
                                   }
                                break;
                   
                   default: System.out.println("Algo raro paso en la funcion EvaluarModosDir..");
               }
         
                        
               
           
       
             }
            
     
        }//llave for each
          
        }
      
    }
    
    
    
}
