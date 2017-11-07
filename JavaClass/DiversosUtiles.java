
public class DiversosUtiles{




  /**Actualizar-agregrar en la clase de MAPEO DE DOZER**/
  /** Mapear un OBJETO de clase S a otro objeto de clase T  , para luego mapear los valores de los objetos siguientes
	 * , si los hubiesen, (nextObjects)
	 * @param object
	 * @return
	 */	
	public T getJsonObjectMultiple(S firstObject,  Object ... nextObjects ) {
		//mapear de Primer objeto origen
		T destinoObj = getJsonObjectDozer(firstObject) ;
		//ahora mapear de segundo objeto origen
		if(destinoObj!=null && nextObjects !=null){
			for(Object objSgt :  nextObjects){
				mapperDozer.map(objSgt, destinoObj);	
			}				
		}		
		return destinoObj;
		//return getJsonObjectReflect(object) ;
	}
  
}
