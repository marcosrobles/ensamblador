/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author Marcos
 */
public class Arbol {
    //Linea linea = new Linea();
   // Evaluadora evalua_lineas = new Evaluadora();

    /**
     * @param args the command line arguments
     */
    
    class Nodo
    {
        String mododir;
        String codigocalculado;
        int bytescalculados;
        int bytesporcalcular;
        int totaldebytes;
        
        Nodo nodoizquierdo;
        Nodo nododerecho;
        
        Nodo(String mododir,String codigocalculado, int bytescalculados, int bytesporcalcular, int totaldebytes)
        {
            this.nodoizquierdo=null;
            this.nododerecho=null;
            this.mododir=mododir;
            this.codigocalculado=codigocalculado;
            this.bytescalculados=bytescalculados;
            this.bytesporcalcular=bytesporcalcular;
            this.totaldebytes=totaldebytes;
            
        }
    }
    
   
    Nodo raiz=null;
    boolean requiere_operando=false;
    public   String modoslinea[]=new String[10]; 
    int llamadas_obtener=0;
    
    
 
   
    
    public boolean tieneraiz()
    {
        if(raiz==null) 
            return false;
        else
            return true;
            
    }
    
    public Arbol Insertar(String mododir,String codigocalculado, int bytescalculados, int bytesporcalcular, int totaldebytes)
    {
      
        
        if(!tieneraiz())//si no hay nada en la raiz
        {
            //guarda este nodo en la raiz
            Nodo nodonuevo = new Nodo(mododir,codigocalculado,bytescalculados, bytesporcalcular, totaldebytes);
            raiz = nodonuevo;           
        }
       else//si ya existe algo en la raiz
        {
          boolean izquierda;
          //guardalo en nodo actual
          Nodo actual = raiz;
          while(true)
          {
              int comp;
              comp = actual.mododir.compareTo(mododir); 
              if(comp>0)
                  izquierda=false;
              else
                  izquierda=true;
              if(izquierda==false)
              {
                  if(actual.nododerecho==null)
                  {
                      Nodo nodonuevo = new Nodo(mododir,codigocalculado,bytescalculados, bytesporcalcular, totaldebytes);
                      actual.nododerecho = nodonuevo;
                      break;
                  }
                   else
                          actual = actual.nododerecho;
                  
              }
                  else
                  {
                      if(actual.nodoizquierdo==null)
                      {
                         Nodo nodonuevo = new Nodo(mododir,codigocalculado,bytescalculados, bytesporcalcular, totaldebytes); 
                         actual.nodoizquierdo = nodonuevo;
                         break;
                      
                      }
                      else
                          actual = actual.nodoizquierdo;
                       
                  }
                     
          }       
        }
        return this;
    }
    
    
    public void obtener()
            {
                Obtenernodos(raiz);
            }
    
    public String[] Obtenernodos(Nodo nod)
            {
              
            
              if(nod!=null)
              {
                  llamadas_obtener++; 
                 modoslinea[llamadas_obtener] = ""+nod.mododir+" "+nod.codigocalculado+" "+nod.bytescalculados+" "+nod.bytesporcalcular+" "+nod.totaldebytes;
                 Obtenernodos(nod.nododerecho);
                 Obtenernodos(nod.nodoizquierdo);
              }  
                return modoslinea;
                
            }
    
   
    
    
                
   
    
}
