package pe.jaav.sistemas.util.model.mainjpa;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class Main {

	public static void main(String[] args) {		
		//pruebaMap();
		String path = "D:\\JAAV\\_OWN_PROJECTS\\YIMPU_SYS\\Proy_CodigoFuente\\ws_yimpu_be_1.0\\JPA_TEST\\resources\\";
		
		crearFileClaseEstandarMapeoDao(path, "GenTablasVario", "GenTablasVarioPK");
		crearFileClaseEstandarMapeoDao(path, "GenTablasVariosDetalle", "GenTablasVariosDetallePK");
		
		crearFileClaseEstandarMapeoService(path, "GenTablasVario", "GenTablasVarioPK");
		crearFileClaseEstandarMapeoService(path, "GenTablasVariosDetalle", "GenTablasVariosDetallePK");		
		
		
	}
	
	
	public static void pruebaMap(){
		System.out.println("Hello world !!");
	}

	public static void crearFileClaseEstandarMapeoDao( String path,
			String nombreEntidadType,String nombrePkType){
		//File file = new File("");
		try {
			System.out.println("*****INICIO*****");
			//			
			
			/***CREAR DAO*/			
			List<String> lines = crearLineasPlantillaDao(nombreEntidadType, nombrePkType);
			Path file = Paths.get(path+"dao\\"+nombreEntidadType+"Dao.java");			
			Files.write(file, lines, Charset.forName("UTF-8"));
			
			
			/***CREAR DAO*/			
			List<String> lines2 = crearLineasPlantillaDaoImpl(nombreEntidadType, nombrePkType);
			Path file2 = Paths.get(path+"dao.impl\\"+nombreEntidadType+"DaoImpl.java");			
			Files.write(file2, lines2, Charset.forName("UTF-8"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("*****FIN*****");
		//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);	
	}
	
	public static void crearFileClaseEstandarMapeoService( String path,
			String nombreEntidadType,String nombrePkType){
		//File file = new File("");
		try {
			System.out.println("*****INICIO*****");
			//			
			
			/***CREAR DAO*/			
			List<String> lines = crearLineasPlantillaService(nombreEntidadType, nombrePkType);
			Path file = Paths.get(path+"service\\"+nombreEntidadType+"Service.java");			
			Files.write(file, lines, Charset.forName("UTF-8"));
			
			
			/***CREAR DAO*/			
			List<String> lines2 = crearLineasPlantillaServiceImpl(nombreEntidadType, nombrePkType);
			Path file2 = Paths.get(path+"service.impl\\"+nombreEntidadType+"ServiceImpl.java");			
			Files.write(file2, lines2, Charset.forName("UTF-8"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("*****FIN*****");
		//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);	
	}
	
	public static List<String> crearLineasPlantillaDao(String nombreEntidadType,String nombrePkType){				
		List<String> lines = Arrays.asList(
				"package pe.jaav.sistemas.seguridadgeneral.model.dao;",
				" ",
				"import java.util.List;",
				"import org.hibernate.Criteria;",
				"import pe.jaav.sistemas.seguridadgeneral.model.domain."+nombreEntidadType+";",
				"import pe.jaav.sistemas.seguridadgeneral.model.dao.AbstractDao;",
				" ",
				"public interface "+nombreEntidadType+"Dao extends AbstractDao<"+nombreEntidadType+", "+nombrePkType+"> {",		
				"	public int guardar("+nombreEntidadType+" objDao);",
				"	public int actualizar("+nombreEntidadType+" objDao);",
				"	public int eliminar("+nombreEntidadType+" objDao);",		
				"	public int contarListado("+nombreEntidadType+" objDao);",	
				"	public List<"+nombreEntidadType+"> listar("+nombreEntidadType+" objDao,boolean paginable);",
				"	public Criteria getCriteriaFilter(Object objDaoGen);",
				"}"			
				);
		
		return lines;
	}
	
	public static List<String> crearLineasPlantillaDaoImpl(String nombreEntidadType,String nombrePkType){				
		List<String> lines = Arrays.asList(
				"package pe.jaav.sistemas.seguridadgeneral.model.dao.impl;",
				" ",
				"import java.util.List;",
				"import org.hibernate.Criteria;",
				"import org.hibernate.criterion.MatchMode;",
				"import org.hibernate.criterion.Projections;",
				"import org.hibernate.criterion.Restrictions;",
				"import org.springframework.stereotype.Repository;",
				"import pe.jaav.common.util.UtilesCommons;",
				"import pe.jaav.sistemas.seguridadgeneral.model.dao."+nombreEntidadType+"Dao;",
				"import pe.jaav.sistemas.seguridadgeneral.model.domain."+nombreEntidadType+";",
				"import pe.jaav.sistemas.seguridadgeneral.model.dao.impl.AbstractDaoImpl;",
				" ",
				"@Repository",
				"public class "+nombreEntidadType+"DaoImpl extends AbstractDaoImpl<"+nombreEntidadType+", "+nombrePkType+"> implements "+nombreEntidadType+"Dao {",

				"	protected  "+nombreEntidadType+"DaoImpl() {",
				"		super("+nombreEntidadType+".class);",						
				"	}",
				" ",
				"	public int guardar("+nombreEntidadType+" objDao) {",
				"		Object result = getCurrentSession().save(objDao);",
				"		return (Integer) (result!=null?result:0);",
				"	}",
				" ",
				"	public int actualizar("+nombreEntidadType+" objDao) {",
				"		update(objDao);",
				"		return 1;",
				"	}",
				" ",
				"	public int eliminar("+nombreEntidadType+" objDao) {",
				"		delete(objDao);",
				"		return 1;",
				"	}",
				" ",
				"	public int contarListado("+nombreEntidadType+" objDao) {",
				"		Criteria criteria = getCriteriaFilter(objDao);",				
				"		criteria.setProjection(Projections.rowCount());",
				"		Object result =  criteria.uniqueResult();",
				"		String obj = result != null ? result.toString() : \"0\";",
				"		return Integer.parseInt(obj);"				,
				"	}",
				" ",
				"	@SuppressWarnings(\"unchecked\")",
				"	public List<"+nombreEntidadType+"> listar("+nombreEntidadType+" objDao,boolean paginable) {",	
				"		Criteria criteria = getCriteriaFilter(objDao);",
						
				"		if(paginable){",
				"			setPaginable(objDao, criteria);",
				"		}",
				"		return criteria.list();",				
				"	}",
				" ",	
				"	public Criteria getCriteriaFilter(Object objDaoGen){",		
				"		Criteria criteria = null;",
				"		if(objDaoGen instanceof "+nombreEntidadType+"){",
				"			"+nombreEntidadType+" objDao = ("+nombreEntidadType+")objDaoGen;",
				"			criteria = getCurrentSession().createCriteria("+nombreEntidadType+".class);",
				"			/*if (UtilesCommons.noEsVacio(objDao.getRolCodigo())) {",
				"				criteria.add(Restrictions.eq(\"rolCodigo\", objDao.getRolCodigo()));",
				"			}*/",
				"		}",
				"		return criteria;",
				"	}",				
				
				"}"
									
				);
		
		return lines;
	}
	
	
	public static List<String> crearLineasPlantillaService(String nombreEntidadType,String nombrePkType){				
		List<String> lines = Arrays.asList(
				"package pe.jaav.sistemas.lavanderia.service;",
				" ",
				"import java.util.List;",
				"import pe.jaav.sistemas.lavanderia.model.domain."+nombreEntidadType+";",
				" ",
				"public interface "+nombreEntidadType+"Service {",
				" ",
				"	public "+nombreEntidadType+" obtenerPorID("+nombrePkType+" id);",
				"	public int contarListado("+nombreEntidadType+" objFiltro);",
				"	public List<"+nombreEntidadType+"> listar("+nombreEntidadType+" objFiltro,boolean paginable);",	
				"	",
				"	public int guardar("+nombreEntidadType+" objTransac);",
				"	public int actualizar("+nombreEntidadType+" objTransac);",
				"	public int eliminar("+nombreEntidadType+" objTransac);",	
				"	",
				"}"
				);
		
		return lines;
	}	
	
	public static List<String> crearLineasPlantillaServiceImpl(String nombreEntidadType,String nombrePkType){
		//La primera letra como ninuscula
		String nombreVariableEntidadType = nombreEntidadType.substring(0,1).toLowerCase()
				+nombreEntidadType.substring(1, nombreEntidadType.length());
		
		List<String> lines = Arrays.asList(
				"package pe.jaav.sistemas.lavanderia.service.impl;",
				"	",
				"import java.util.List;",
				"	",
				"import org.springframework.beans.factory.annotation.Autowired;",
				"import org.springframework.stereotype.Service;",
				"import org.springframework.transaction.annotation.Transactional;",
				"import pe.jaav.sistemas.lavanderia.model.dao."+nombreEntidadType+"Dao;",
				"import pe.jaav.sistemas.lavanderia.model.domain."+nombreEntidadType+";",
				"import pe.jaav.sistemas.lavanderia.service."+nombreEntidadType+"Service;",
				" ",
				"@Service(\""+nombreVariableEntidadType+"Service\")",
				"@Transactional(readOnly = true)",
				"public class "+nombreEntidadType+"ServiceImpl implements "+nombreEntidadType+"Service {",
				"	",
				"	@Autowired",
				"	"+nombreEntidadType+"Dao "+nombreVariableEntidadType+"Dao;",
				"	",
				"	@Override",
				"	public "+nombreEntidadType+" obtenerPorID("+nombrePkType+" id) {",		
				"		return "+nombreVariableEntidadType+"Dao.findById(id);",
				"	}",
				"	",
				"	@Override",
				"	public int contarListado("+nombreEntidadType+" objFiltro) {",		
				"		return "+nombreVariableEntidadType+"Dao.contarListado(objFiltro);",
				"	}",
				"",
				"	@Override",
				"	public List<"+nombreEntidadType+"> listar("+nombreEntidadType+" objFiltro, boolean paginable) {",		
				"		return "+nombreVariableEntidadType+"Dao.listar(objFiltro, paginable);",	
				"	}",	
				"	",	
				"	@Override",	
				"	@Transactional(readOnly = false)",	
				"	public int guardar("+nombreEntidadType+" objTransac) {",			
				"		return "+nombreVariableEntidadType+"Dao.guardar(objTransac);",	
				"	}",	
				" ",	
				"	@Override",	
				"	@Transactional(readOnly = false)",	
				"	public int actualizar("+nombreEntidadType+" objTransac) {",	
				"		return "+nombreVariableEntidadType+"Dao.actualizar(objTransac);",	
				"	}",	
				" ",
				"	@Override",	
				"	@Transactional(readOnly = false)",	
				"	public int eliminar("+nombreEntidadType+" objTransac) {",	
				"		return "+nombreVariableEntidadType+"Dao.eliminar(objTransac);",	
				"	}",	
				"}"
				);
		
		return lines;
	}						
}
