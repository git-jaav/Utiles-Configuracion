package pe.royalsystems.springerp.utilesJson.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.util.JSONPObject;


public class UtilesJSON  {		
	
	private static final String RQ_MET_GET="GET";
	private static final String RQ_MET_POST="POST";
	private static final String RQ_MET_PUT="PUT";
	private static final String RQ_MET_DELETE="DELETE";
	
	private static final String RQ_PROP_APPJSON="application/json";
	
	
	/**
	 * @param urlParam
	 * @param claseObjeto
	 * @return
	 */
	public static String getJson(String urlParam) {
		return getJson(urlParam, null);
	}
	
	
	/** REQUEST GET simple
	 * @param urlParam
	 * @param mapHeader
	 * @return
	 */
	public static String getJson(String urlParam,Map<String ,String> mapHeader) {
		return requestJson(urlParam, mapHeader, RQ_MET_GET);	
	}	
	
	/** REQUEST POT simple, con HEADER 
	 * @param urlParam
	 * @param mapHeader
	 * @return
	 */
	public static String postJson(String urlParam,Map<String , String> mapHeader) {
		return requestJson(urlParam, mapHeader, RQ_MET_POST);
	}	
	
	/**
	 * @param urlParam
	 * @param mapHeader
	 * @param requestMethod
	 * @return
	 */
	public static String requestJson(String urlParam,Map<String , String> mapHeader, String requestMethod) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("Accept", RQ_PROP_APPJSON);

			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);
		
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		
			
			if(json!=null){						
				conn.disconnect();	
				return json; 									 		
			}		
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}
	

	
	/** REQUEST GET con retorno de un OBJETO de una determinada clase
	 * @param urlParam
	 * @param claseObjeto
	 * @return
	 */
	public static Object getObjectJson(String urlParam,  Class<?> claseObjeto) {
		return getObjectJson(urlParam, claseObjeto, null);
	}
	
	
	/** REQUEST GET con retorno de un OBJETO de una determinada clase, 
	 * con parametros en el HEADER para el INPUT y OUTPUT
	 * @param urlParam
	 * @param claseObjeto
	 * @param mapHeader
	 * @return
	 */
	public static Object getObjectJson(String urlParam,  Class<?> claseObjeto,
			Map<String , String> mapHeader) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(RQ_MET_GET);
			conn.setRequestProperty("Accept", RQ_PROP_APPJSON);

			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);

			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}								
			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		
    	    
			
			if(json!=null){
				//MAPPER JASON TO OBJECT 
				ObjectMapper mapper = new ObjectMapper();				
				Object objeto =  mapper.readValue(json,claseObjeto);			
				conn.disconnect();	
				return objeto; 				
			}
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}	

	/** REQUEST POST con retorno de un OBJETO de una determinada clase, con objeto como FILTRO,
	 * @param urlParam
	 * @param objData
	 * @param claseObjeto
	 * @return
	 */
	public static Object getObjectJsonFiltro(String urlParam,Object objData,  Class<?> claseObjeto) {
		return getObjectJsonFiltro(urlParam, objData, claseObjeto,null);
	}
	
	/**REQUEST POST con retorno de un OBJETO de una determinada clase, con objeto como FILTRO,
	 * con parametros en el HEADER para el INPUT y OUTPUT
	 * @param urlParam
	 * @param objData
	 * @param claseObjeto
	 * @param mapHeader
	 * @return
	 */
	public static Object getObjectJsonFiltro(String urlParam,Object objData,  Class<?> claseObjeto,
			Map<String,String> mapHeader) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(RQ_MET_POST);
			conn.setRequestProperty("Content-Type", RQ_PROP_APPJSON);
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);	
			
			//MAPPER OBJECT TO JASON
			ObjectMapper mapper = new ObjectMapper();
			byte[] jsonInBytes = mapper.writeValueAsBytes(objData);
			/////////////////////////
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonInBytes);
			os.flush();
						
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED
					) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		
			
			if(json!=null){			
				//json = json.toLowerCase();//AUX
				//MAPPER JASON TO OBJECT 
				ObjectMapper mapperObj = new ObjectMapper();				
				Object objeto =  mapperObj.readValue(json,claseObjeto);			
				conn.disconnect();	
				return objeto; 									 		
			}
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}
	
	
	/**
	 * @param urlParam
	 * @param dataJson
	 * @return
	 */
	public static String getJsonFiltro(String urlParam,String dataJson) {
		return getJsonFiltro(urlParam, dataJson, null);
	}
	
	/**
	 * @param urlParam
	 * @param dataJson
	 * @param mapHeader
	 * @return
	 */
	public static String getJsonFiltro(String urlParam,String dataJson,Map<String,String> mapHeader) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(RQ_MET_POST);
			conn.setRequestProperty("Content-Type", RQ_PROP_APPJSON);
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);					
			
			//MAPPER OBJECT TO JASON
			ObjectMapper mapper = new ObjectMapper();
			//JSONObject json = new JSONObject(objData);			
						
			//byte[] jsonInBytes = mapper.writeValueAsBytes(objData);
			byte[] jsonInBytes = dataJson.getBytes();
			/////////////////////////
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonInBytes);
			os.flush();
			

			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED
					) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}

			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)
			String json = getJSON_Encoding(conn, "UTF-8");		
			
			if(json!=null){						
				conn.disconnect();	
				return json; 									 		
			}
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}
	

	
	/**
	 * @param urlParam
	 * @param typeReference
	 * @return
	 */
	public static List<?> getListJson(String urlParam,TypeReference<?> typeReference) {
		return getListJson(urlParam, typeReference, null);
	}
	
	/**
	 * @param urlParam
	 * @param typeReference
	 * @param mapHeader
	 * @return
	 */
	public static List<?> getListJson(String urlParam,TypeReference<?> typeReference,
			Map<String,String> mapHeader) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(RQ_MET_GET);
			conn.setRequestProperty("Accept", RQ_PROP_APPJSON);
			
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);		
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}								

			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);	
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		
    	    

			//BufferedReader br = new BufferedReader(new InputStreamReader(
				//(conn.getInputStream())));																
			
			if(json!=null){
				//MAPPER JASON TO OBJECT 
				ObjectMapper mapper = new ObjectMapper();				
				List<?> lista = mapper.readValue(json,typeReference);				
				conn.disconnect();	
				return lista; 				
			}
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}
	
	/** Abstraccion
	 * @param urlParam
	 * @param objData
	 * @param claseObjeto
	 * @return
	 */
	public static List<?> getListJsonFiltro(String urlParam,Object objData,Class<?> claseObjeto) {
		return getListJsonFiltro(urlParam, objData, new TypeReference<List<?>>() {});
	}
	
	/**
	 * @param urlParam
	 * @param objData
	 * @param typeReference
	 * @return
	 */
	public static List<?> getListJsonFiltro(String urlParam,Object objData,TypeReference<?> typeReference) {	
		return getListJsonFiltro(urlParam, objData, typeReference, null);
	}
	
	
	/**
	 * @param urlParam
	 * @param objData
	 * @param typeReference
	 * @param mapHeader
	 * @return
	 */
	public static List<?> getListJsonFiltro(String urlParam,Object objData,TypeReference<?> typeReference,
			Map<String,String> mapHeader) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(RQ_MET_POST);
			conn.setRequestProperty("Content-Type", RQ_PROP_APPJSON);
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);	
			
			//MAPPER OBJECT TO JASON
			ObjectMapper mapper = new ObjectMapper();
			byte[] jsonInBytes = mapper.writeValueAsBytes(objData);
			/////////////////////////
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonInBytes);
			os.flush();			
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED
					) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}

			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		    
			
			if(json!=null){
				//MAPPER JASON TO OBJECT
				
				ObjectMapper mapperJsonToObject = new ObjectMapper();								
//				Object obj = mapperJsonToObject.readValue(json,typeReference.getClass());
//				if(obj instanceof List<?>){
//					lista = mapperJsonToObject.readValue(json,typeReference);
//				}else{
//					lista.add(obj);
//				}
				List<?> lista = mapperJsonToObject.readValue(json,typeReference);				
				conn.disconnect();	
				return lista; 				
			}
		} catch (MalformedURLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally{
			if(conn !=null){
				conn.disconnect();	
			}
		}
		return null;
	}
	
	/**
	 * @param urlParam
	 * @param objData
	 * @param claseObjeto
	 * @return
	 */
	public static Object postObjectJson(String urlParam, Object objData, Class<?> claseObjeto) {				
		return postObjectJson(urlParam, objData, claseObjeto,null);
	}
	
	/**
	 * @param urlParam
	 * @param objData
	 * @param claseObjeto
	 * @param mapHeader
	 * @return
	 */
	public static Object postObjectJson(String urlParam, Object objData, Class<?> claseObjeto,
			Map<String,String> mapHeader) {		
		try {
			URL url = new URL(urlParam);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(RQ_MET_POST);
			conn.setRequestProperty("Content-Type", RQ_PROP_APPJSON);
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);
			
			//MAPPER OBJECT TO JASON
			ObjectMapper mapper = new ObjectMapper();
			byte[] jsonInBytes = mapper.writeValueAsBytes(objData);
			/////////////////////////
			
			OutputStream os = conn.getOutputStream();
			os.write(jsonInBytes);
			os.flush();			
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED
					) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);

			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		

	        
			//BufferedReader br = new BufferedReader(new InputStreamReader(
				//		(conn.getInputStream())));

			//MAPPER JASON TO OBJECT 
			if(json!=null){
				ObjectMapper mapperJsonToObject = new ObjectMapper();			
				Object objetoResult  = mapperJsonToObject.readValue(json,claseObjeto);			
				conn.disconnect();				
				return objetoResult;
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param urlParam
	 * @param claseObjeto
	 * @return
	 */
	public static Object postObjectJson(String urlParam, Class<?> claseObjeto) {				
		return postObjectJsonMapHead(urlParam, claseObjeto,null);
	}
	
	/**
	 * @param urlParam
	 * @param claseObjeto
	 * @return
	 */
	public static Object postObjectJsonMapHead(String urlParam, Class<?> claseObjeto,
			Map<String,String> mapHeader) {		
		try {
			URL url = new URL(urlParam);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(RQ_MET_POST);
			conn.setRequestProperty("Content-Type", RQ_PROP_APPJSON);			
			/**Verificar valores INPUT de HEADER*/			
			setConnectioMapHeaderInput(conn, mapHeader);	
			
			//SIN CONTEN DATA						
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED
					) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}

			/**Actualizar valores OUTPUT de HEADER*/			
			setConnectioMapHeaderOutput(conn, mapHeader);
			
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		

	        
			//BufferedReader br = new BufferedReader(new InputStreamReader(
				//		(conn.getInputStream())));

			//MAPPER JASON TO OBJECT 
			if(json!=null){
				ObjectMapper mapperJsonToObject = new ObjectMapper();			
				Object objetoResult  = mapperJsonToObject.readValue(json,claseObjeto);			
				conn.disconnect();				
				return objetoResult;
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

    
    /******************************************************************************/
    /*** METODOS GENERALES */
    /******************************************************************************/
    
    public static String getJSON_Encoding(HttpURLConnection conn,String encode) throws IOException{
    	
		//para obtener el Texto en el ENCODING correcto (UTF-8)
		InputStream in = conn.getInputStream();
        String encoding = conn.getContentEncoding();	        
        encoding = encoding == null ? encode : encoding; 
        String json = IOUtils.toString(in, encoding);
        
        return json;
    }
    
    /**
     * @param jsonList
     * @return
     */
    public static boolean isJsonList(String jsonList){
    	boolean result = false;
    	/***Primer metodo empirico: Si posee CORCHETES iniciales '[' es una lista de Objetos
    	 * si posee  '{' es una estructura
    	 **/
    	StringTokenizer stToken = new StringTokenizer(jsonList);
    	if(stToken.hasMoreElements()){
    		String token = stToken.nextToken();
    		//logica , siempre en cuando el SOURCE JSON sea válido ....
    		if((""+token.charAt(0)).equals("[")){
    			result = true;
    		}
    	}    	    	
    	return result;
    }
    
    
    /***Verificar valores INPUT de HEADER
     * @param conn
     * @param mapHeader
     */
    public static void setConnectioMapHeaderInput(HttpURLConnection conn,
    					Map<String,String> mapHeader){		
		if(conn != null && mapHeader!=null && mapHeader.size()>0){				
			//conn.getHeaderFields().putAll(mapHeader);	
			for(String key : mapHeader.keySet()){
				conn.setRequestProperty(key, mapHeader.get(key));	
			}				
		}    	
    }
    
    public static void setConnectioMapHeaderOutput(HttpURLConnection conn,
			Map<String,String> mapHeader){		
    	if(conn!=null && mapHeader!=null && mapHeader.size()>0){    	
    		for(String key : mapHeader.keySet()){
    			if(conn.getHeaderFields().containsKey(key)){
    				String conten = "";
    				for(String value : conn.getHeaderFields().get(key)){
    					conten = conten+""+value+" ";
    				}    				    				
    				mapHeader.put(key,conten);    				
    			}
    			
    		}					
    	} 	
    }
    

    
    /** Devuelve el elemento (indexNode)  JSON de una lista JSON (jsonList) enviada
     * @param json
     * @param indexNode
     * @return
     */
    public static String getNodeJson(String jsonList, int indexNode){
    	String result = "";    	
    	try {
    		ObjectMapper mapper = new ObjectMapper();    		    		
			JsonNode elemNode = mapper.readTree(jsonList);			
			if(elemNode.size() > indexNode){
				JsonNode node = elemNode.get(indexNode);				
				result = node.toString();				
				//node.as
			}									
			return result;    	
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public static Map<String,Object> getMapJson(String json){
    	Map<String,Object> mapJson = new HashMap<String,Object>();
    	try {
    		Iterator<String> fields =null;
    		ObjectMapper mapper = new ObjectMapper();
			JsonNode nodeJson = mapper.readTree(json);
			if(nodeJson!=null){
				fields = nodeJson.fieldNames();
				while(fields.hasNext()){
					String field = fields.next();
					if(nodeJson.get(field).isNumber()){
						mapJson.put(field, nodeJson.get(field).decimalValue());
					}else{
						mapJson.put(field, nodeJson.get(field).textValue());	
					}				
				}
			}					
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}       	    	
    	return mapJson;
    }
    
    
    /** Convertir una cadena JSON en un Objeto de una determinada clase: claseObjeto
     * @param json
     * @param claseObjeto
     * @return
     */
    public static Object getParseObjectJson(String json,  Class<?> claseObjeto) {
		if(json!=null){
			try {
				//MAPPER JASON TO OBJECT 
				ObjectMapper mapper = new ObjectMapper();				
				Object objeto;				
				objeto = mapper.readValue(json,claseObjeto);
				return objeto; 
			} catch (JsonParseException e) {			
				e.printStackTrace();
			} catch (JsonMappingException e) {				
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}								
		}    	
		return null;
    }
    public static Iterator<String> getFieldsJson(String json){    	
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			JsonNode nodeJson = mapper.readTree(json);
			if(nodeJson!=null){
				return nodeJson.fieldNames();
			}						
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}   
    	return null;
    }    

    /**Servicio para obtener un OBJETO JSON*/
    public JSONObject getJSONObject(String urlStr,String indica){
    	JSONObject objeto = null;
    	try {
    		URL url = new URL(urlStr);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod(RQ_MET_GET);
    		conn.setRequestProperty("Accept",RQ_PROP_APPJSON);

    		if (conn.getResponseCode() != 200) {
    			throw new RuntimeException("Failed : HTTP error code : "
    					+ conn.getResponseCode());
    		}
    		
			//para obtener el Texto en el ENCODING correcto (UTF-8)     	    			
			String json = getJSON_Encoding(conn, "UTF-8");		
 	        
	        objeto = new JSONObject(json); 	        	     
	        
    		conn.disconnect();
    	} catch (MalformedURLException e) {

    		e.printStackTrace();
    	  } catch (IOException e) {

    		e.printStackTrace();

    	  }
    	return objeto;
    }
}
