/* Linea.java
 * @fileoverview Esta clase recibe cada linea del archivo .asm  y la divide
   *en tokens y clasifica los posibles casos que representa una linea del archivo. 
 * @version 1.0
 * @author Marcos Robles<ecko_lob@hotmail.com>
 */
package ensamblador;



import java.io.File;
import java.util.StringTokenizer;
import   java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Linea {
    
  Evaluadora evalua_lineas = new Evaluadora();
    Errores maneja_errores =new Errores();
    String lineainstrucciones="";
    String [] guarda_tokens = new String[3];
    String linea=null,etiqueta=null,codop=null,operando=null;
    String error="";
    short cuentalineas=0;
    int cuentatokens=0;
   
    
   
    
/**
     * Este metodo borra los comentarios dejando solo los tokens necesarios
     * @param{String}cadena
     *@return{void}
      */ 
    public void quitarcomentarios(String cadena)
    {
        //Inicializar el arreglo donde se guardan los tokens retornados por nextToken()
        for(String elemento:guarda_tokens){
           elemento="";   
        }
                
        
        int a=cadena.indexOf(';');//guarda la posicion donde encuentra un ; en la cadena
        if(a!=-1)//si regresa -1 significa que no encontro ;
        {   
            this.linea=cadena.substring(0, a);//guarda todo lo que este antes del ;     
        }
        else
        {
            this.linea=cadena;
        }
       
    }
    
    
    /**
     * Este metodo divide la linea recibida en los tokens etiqueta, codop y operando
     * Utiliza la clase Strinktokenizer que tiene como delimitador espacios o tabuladores
     * @param{File}seleccionado
     *@return{void}
      */ 
    public void separarlinea(File seleccionado)
    {
        
        boolean et=false,cod=false,op=false;
         boolean et_sola=false;
         int es_etiqueta=1,es_codop=1,es_operando=1;
         
         cuentalineas+=1;
       if(linea!=null)//si linea contiene algun caracter
       {
           StringTokenizer tokenizador = new StringTokenizer(linea);
           Pattern letras = Pattern.compile("^[a-z|A-Z]");
           Pattern espacios = Pattern.compile("^[ |\t]+");
           Matcher comprobador = letras.matcher(linea);
           Matcher comprobador2 = espacios.matcher(linea);
            /*  find() busca en subcadenas de la linea si la expresion regular concuerda con 
             cada subcadena y retorna verdadero con la primera subcadena que concuerda.     */
            
          et = comprobador.find();
          cod = comprobador2.find();
          
          
     
         /*
          CASOS DE COMBINACIONES DE LINEA 
          a.ETIQUETA, CODOP, OPERANDO
          b.ETIQUETA, CODOP
          c.CODOP, OPERANDO
          d.CODOP
          
          La directiva END se evaluara dentro del caso b y d
         */
          if(cod==true)//Si la linea comienza con codop
           {   
               cuentatokens=tokenizador.countTokens();
              switch(cuentatokens)
              {
                  case 0:/*error="No se especifico ninguna instruccion(codop)";
                         maneja_errores.writeFileErrores(cuentalineas,error,seleccionado);*/
                          break;
                  case 1://CODOP
                       byte x=0;
                      while (tokenizador.hasMoreTokens()) 
                        {  
                            guarda_tokens[x]=tokenizador.nextToken();
                             x++;
                        }
                      etiqueta = null;
                      codop = guarda_tokens[0];
                      operando = null;
                    lineainstrucciones=cuentalineas+"\t"+etiqueta+"\t"+codop+"\t"+operando;
                    evalua_lineas.EvaluarCodop(codop,cuentalineas,seleccionado);
                    evalua_lineas.EvaluarOperando(operando,cuentalineas,seleccionado);
                    evalua_lineas.et_valida=true;
                    EscribeLinea(lineainstrucciones, seleccionado);
                 
                      break;
                      //
                  case 2: //b.CODOP, OPERANDO
                        x=0;
                      while (tokenizador.hasMoreTokens()) 
                        {  
                            guarda_tokens[x]=tokenizador.nextToken();
                             x++;
                        }
                      etiqueta = null;
                      codop = guarda_tokens[0];
                      operando = guarda_tokens[1];
                      lineainstrucciones=cuentalineas+"\t"+etiqueta+"\t"+codop+"\t"+operando;
                      evalua_lineas.EvaluarCodop(codop,cuentalineas,seleccionado);
                      evalua_lineas.EvaluarOperando(operando,cuentalineas,seleccionado);
                      evalua_lineas.et_valida=true;
                      EscribeLinea(lineainstrucciones, seleccionado);
                      break;
                  default://OTRO
                      error="Numero de tokens excedido"; 
                      
                      maneja_errores.writeFileErrores(cuentalineas,error,seleccionado);
              }
           }
          else //si no comienza con codop doy por echo que es una etiqueta
           {
              cuentatokens=tokenizador.countTokens();
              switch(cuentatokens)
              {
                  case 0:
                      break;
                  case 1://ETIQUETA      
                      byte x=0;
                     
                      while (tokenizador.hasMoreTokens()) 
                        {  
                            guarda_tokens[x]=tokenizador.nextToken();
                             x++;
                        }
                      etiqueta = guarda_tokens[0];
                      et_sola=true;
                       evalua_lineas.EvaluarEtiqueta(etiqueta, cuentalineas, seleccionado,et_sola);
               
                   
                      break;
                      //
                  case 2: //b.ETIQUETA, CODOP
                       x=0;
                      while (tokenizador.hasMoreTokens()) 
                        {  
                            guarda_tokens[x]=tokenizador.nextToken();
                             x++;
                        }
                      etiqueta = guarda_tokens[0];
                      codop = guarda_tokens[1];
                      operando = null;
                      lineainstrucciones=cuentalineas+"\t"+etiqueta+"\t"+codop+"\t"+operando;
                      evalua_lineas.EvaluarEtiqueta(etiqueta,cuentalineas,seleccionado,et_sola);
                      evalua_lineas.EvaluarCodop(codop,cuentalineas,seleccionado);
                      evalua_lineas.EvaluarOperando(operando,cuentalineas,seleccionado);
                      EscribeLinea(lineainstrucciones, seleccionado);
                     
              
                      break;
                  case 3://a.ETIQUETA, CODOP, OPERANDO
                             x=0;
                      while (tokenizador.hasMoreTokens()) 
                        {  
                            guarda_tokens[x]=tokenizador.nextToken();
                             x++;
                        }
                      etiqueta = guarda_tokens[0];
                      codop = guarda_tokens[1];
                      operando = guarda_tokens[2];
                      lineainstrucciones=cuentalineas+"\t"+etiqueta+"\t"+codop+"\t"+operando;
                      evalua_lineas.EvaluarEtiqueta(etiqueta,cuentalineas,seleccionado,et_sola);
                      evalua_lineas.EvaluarCodop(codop,cuentalineas,seleccionado);
                      evalua_lineas.EvaluarOperando(operando,cuentalineas,seleccionado);
                      EscribeLinea(lineainstrucciones, seleccionado);
        
                      break;
                  default://OTRO
                           error ="Solo puede haber entre 1-3 tokens"; 
                      maneja_errores.writeFileErrores(cuentalineas,error,seleccionado);
              }
           }
           
                
       }
    }
    
    
      public void EscribeLinea(String cadenainst,File seleccionado)
    {
        if(evalua_lineas.et_valida&&evalua_lineas.codop_valido && evalua_lineas.op_valido == true)
        {
        Ensamblador.writeFileInst(cadenainst, seleccionado);
        }
       else
        {
      }
    }

    
      
}
