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
      String modos="";
 
    
      String []requiereoperando=new String[12];
       boolean et_valida, codop_valido,op_valido=false;
       boolean es_binario,es_octal,es_hex,es_dec=false;
       int rango;
  
       
      
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
         
          
        char  basenum=' ';
        boolean modo_ok=false;
        if(codop_valido==false)//si es un codop invalido no es necesario evaluar el operando
        {
            
        }
        else 
        {       
          EvaluarModosDir();
          //si llega a este punto es porque ya evalue que ese modo de direccionamiento requiere operando
         
              
             for(String elemento:requiereoperando)
        {
            
             if(modo_ok==true)//Si op_valido es true ya no necesito evaluar el operando en mas modos, creo
              {
               break;  
              }
             else
             {
             
            if(operando==null)
            {
                
                if(elemento=="INH")
                {
                    if(modo_ok==true)
                                {
                                 break;  
                                }
                              
                                 op_valido=true;
                                 modo_ok=true;
                }
                else   //Entonces si el operando es null,y no es el modo inherente, indico en el archivo de errores que se requiere un operando 
                {
                                    maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                                   op_valido=false;                              
                }        
            }
            else
            {
            
            
            //Evaluo el operando segun el modo de direccionamiento identificado
            String modo=elemento;
            if(modo!=null)
            {
            
           
        switch(modo)//todos solo entran al switch si requieren operando a exepcion del inherente
        {
                  case "IMM8":  
                      
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
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
                                                    Pattern patron_base_binaria = Pattern.compile("(0|1)+");
                                                    String suboperando=""; 
                                                    suboperando=operando.substring(2);
                                                    Matcher comprobadorbinario = patron_base_binaria.matcher(suboperando);
                                                    es_binario = comprobadorbinario.matches();
                                                    if(es_binario==true)
                                                    {
                                                      rango =  Integer.parseInt(suboperando,2);// convierte el numero binario a base 10 
                                                      
                                                     //para conocer o entender  que rango tiene 
                                                    System.out.println("rango: "+rango);                          
                                                     op_valido=true;
                                                      modo_ok=true;
                                                         
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
                                                      modo_ok=true;  
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
                                                      modo_ok=true;  
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
                                                      modo_ok=true;
                                                    }
                                                    else
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
                                
                            }
                                break;
                   case "IMM16":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
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
                           //     op_valido=true;
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
                              //  op_valido=true;
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
                             //   op_valido=true;
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
                               // op_valido=true;
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
                               // op_valido=true;
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
                                //op_valido=true;
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
                              //  op_valido=true;
                            }
                                break;
                   case "REL8":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                             //   op_valido=true;
                            }
                                break;
                    case "REL9":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                             //   op_valido=true;
                            }
                                break;
                    case "REL16":  
                                if(operando==null)
                            {
                                op_valido=false;
                                maneja_errores.errores_operando(cuentalineas, 1, seleccionado);
                            }
                            else
                            {
                                 //evaluar operando y validarlo
                               
                            }
                                break;
                   
                   default: {
                       if(operando!=null)
                         {//si el codop no fue encontrado en tabop, decirle aqui que no evalue el operando o asi
                 
                            op_valido=false;
                            maneja_errores.errores_operando(cuentalineas, 2, seleccionado);
                         }
            
                       }
         }
        }
       }//llave else (si operando no es null)
             }
      }    //llave for each      
     }
          
    }
    
    
    public void EvaluarModosDir()//EL PROBLEMA ESTA AQUI 
    {
        int bytesporcalcular=0; 
        for(String elem:lineamodos)  //inicializar lineamodos
        {
            elem="";
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
