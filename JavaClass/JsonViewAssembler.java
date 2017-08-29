package pe.royalsystems.springerp.ERPActivoFijoRest.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

public class JsonViewAssembler<S,T> {	
	
	//private S valueSource;
	//private T valueTarget;
	private Class<T>  claseTarget;
	
	private DozerBeanMapper mapperDozer;
	
	public JsonViewAssembler() {
	    
	}
	public JsonViewAssembler(Class<T> clsTarget) {
	    this.claseTarget  = clsTarget;
	    this.mapperDozer = new DozerBeanMapper();
	}
	
	protected JsonViewAssembler(S valueSource,T valueTarget) {
		    //this.valueSource = valueSource;
		    //this.valueTarget = valueTarget;
		    this.mapperDozer = new DozerBeanMapper();
	}
	
	/** Mapear un OBJETO de clase S a otro objeto de clase T  	
	 * @param object
	 * @return
	 */	
	public T getJsonObject(S object) {
		return getJsonObjectDozer(object) ;
		//return getJsonObjectReflect(object) ;
	}
	
	/** Mapear un OBJETO de clase S a otro objeto de clase T  , para luego mapear los valores de otro Objeto, en el resultado T 
	 * @param object
	 * @return
	 */	
	public T getJsonObject(S firstObject, Object secondObject) {
		//mapear de Primer objeto origen
		T destinoObj = getJsonObjectDozer(firstObject) ;
		//ahora mapear de segundo objeto origen
		if(destinoObj!=null && secondObject !=null){
			mapperDozer.map(secondObject, destinoObj);	
		}		
		return destinoObj;
		//return getJsonObjectReflect(object) ;
	}
	
		
	/** Mapear un OBJETO de clase S a otro objeto de clase T , que es recibido, sobrescribiendo sus valores actuales.
	 * @param object
	 * @param objetoTarget
	 * @return
	 */
	public T getJsonObjectDestino(S object, T objetoTarget) {
		mapperDozer.map(object, objetoTarget);		
		return objetoTarget;
		//return getJsonObjectReflect(object) ;
	}
	
	
	/** Mapear una LISTA de clase S a otra LISTA de clase T
	 * @param listaOrigen
	 * @return
	 */
	public  List<T> getJsonList(List<S> listaOrigen){
		return getJsonListDozer(listaOrigen) ;
		//return getJsonListReflect(listaOrigen) ;
	}
	
	/***DOZER****/
	
	/** Mapear un OBJETO de clase S a otro objeto de clase T con Dozer 
	 * (Se mapearan los atributos que coincidad)
	 * @param object
	 * @return
	 */
	public T getJsonObjectDozer(S object) {
		try{			
			//T elem =  mapperDozer.map(object, claseTarget);
			return mapperDozer.map(object, claseTarget);			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
    /**
     * @param listaOrigen
     * @return
     */
    public  List<T> getJsonListDozer(List<S> listaOrigen){
   	 List<T> listaTarget = new ArrayList<T>();
   	 for(S orign : listaOrigen){
   		 T target = getJsonObjectDozer(orign) ;
   		 if(target!=null){
   			listaTarget.add(target);	 
   		 }   		 
   	 }    	 
   	 return  listaTarget;
   }
    
    
    /***JAVA REFLECT ****/
	
	/**Construye dinamicamente un nuevo objetode Una clase determinada a partir del objeto Original*/
    public  T getJsonObjectReflect(S objetoOrigen){
		try{			
			T objNuevo=null;					
			if(objetoOrigen!=null && claseTarget !=null ){				
				//String type = claseNuevoObjeto.getName();				
				objNuevo = claseTarget.newInstance();
				//objNuevo =  Class.forName(type).newInstance();
				////obtener los valores del objetoOrigen y set a objNuevo
				Field[] fields = objetoOrigen.getClass().getDeclaredFields();
				for(Field field : fields) {
	                try {
						if((field.getModifiers() & java.lang.reflect.Modifier.FINAL) == java.lang.reflect.Modifier.FINAL){
							//ES FINAL
						}else{
		                	//String fieldName = field.getName();
		                	field.setAccessible(true);
		                	Object fieldValue = field.get(objetoOrigen);		                	
		                	field.set(objNuevo,fieldValue);
						}							                		               					
					} catch (IllegalArgumentException  e) {						
						e.printStackTrace();
					}	
				}									
			}
			return objNuevo;
		}catch(Exception ex){			
			ex.printStackTrace();
		}
		return null;
    }
    
    /**
     * @param listaOrigen
     * @return
     */
    public  List<T> getListViewReflect(List<S> listaOrigen){
    	 List<T> listaTarget = new ArrayList<T>();
    	 for(S orign : listaOrigen){
    		 T target = getJsonObjectReflect(orign) ;
    		 listaTarget.add(target);
    	 }    	 
    	 return  listaTarget;
    }
	
    

    
}
