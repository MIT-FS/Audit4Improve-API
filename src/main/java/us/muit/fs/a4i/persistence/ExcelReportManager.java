/**
 * 
 */
package us.muit.fs.a4i.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.ReportI;



/**
 * <p>Clase que cotendrán las funciones de manejo de excel comunes al manejo de cualquier informe</p>
 * <p>Se utiliza la API apachePOI para manejar los ficheros excel</p>
 * <p>Las primeras versiones se centran en la escritura</p>
 * <p>Política de informes: un informe es una hoja de un documento excel, identificada con el id del informe</p>
 * <p>Este Gestor tiene los métodos para obtener la hoja y persistirla</p>
 * <p>Si la hoja existía la recupera y se añadirá sobre ella, no se elimina lo anterior, si no existía se crea nueva</p>
 * @author Isabel Román
 * 
 *
 */
public class ExcelReportManager implements PersistenceManager, FileManager{
	private static Logger log=Logger.getLogger(ExcelReportManager.class.getName());
	/**
	 * <p>Referencia al gestor de estilo que se va a utilizar</p>
	 */
	protected ReportFormaterI formater;
	ReportI report;
	FileInputStream inputStream=null;
	
	/**
	 * <p>Localización del fichero excel</p>
	 */
	protected String filePath="";
	/**
	 * <p>Nombre del fichero excel</p>
	 */
	protected String fileName="";
	
	protected HSSFWorkbook wb=null;
	protected HSSFSheet sheet=null;
	
	public void setReport(ReportI report) {
		log.info("Establece el informe");
		this.report=report;
	}



	@Override
	public void setFormater(ReportFormaterI formater) {
		log.info("Establece el formateador");
		this.formater=formater;
		
	}

	@Override
	public void setPath(String path) {
		log.info("Establece la ruta al fichero");
		this.filePath=path;
		
	}

	@Override
	public void setName(String name) {
		log.info("Establece el nombre del fichero");
		this.fileName=name;
		
	}
	/**
	 * El libro contendrán todos los informes de un tipo concreto
	 * Primero hay que abrir el libro
	 * Busco la hoja correspondiente a esta entidad, si ya existe la elimino
	 * Creo la hoja
	 * @return Hoja de excel
	 * @throws IOException error al abrir el fichero
	 * @throws EncryptedDocumentException documento protegido
	 */
	protected HSSFSheet getCleanSheet() throws EncryptedDocumentException, IOException {
		log.info("Solicita una hoja nueva del libro manejado");
		if(wb==null) {			
			inputStream = new FileInputStream(filePath+fileName+".xls");
			wb = (HSSFWorkbook) WorkbookFactory.create(inputStream);
			log.info("Generado workbook");
		  
		}
		if(sheet==null)
		{
			/**
			  int templateIndex=wb.getSheetIndex("Template");
			  HSSFSheet sheet = wb.cloneSheet(templateIndex);
			  int newIndex=wb.getSheetIndex(sheet);
			  **/
			/**
			 * <p>Verifico si la hoja existe y si es así la extraigo</p>
			 * <p>Si no existe la creo.
			 */
			  sheet= wb.getSheet(report.getId().replaceAll("/", "."));
			  
			  if(sheet!=null) {
				  log.info("Recuperada hoja, que ya existía");
				  /*
				   * Si la hoja existe la elimino
				   */
				  int index = wb.getSheetIndex(sheet);
				  wb.removeSheetAt(index);
			  }
			  sheet=wb.createSheet(report.getId().replaceAll("/", "."));
			  log.info("Creada hoja nueva");
				
		}
		
	  
		return sheet;
	}
	/**
	 * Guarda en un hoja limpia con el nombre del id del informe todas las métricas y los indicadores que incluya
	 */
	@Override
    public void saveReport() throws ReportNotDefinedException{
		log.info("Guardando informe");
    	if(report==null) {
    		throw new ReportNotDefinedException();
    	}
    	try {
    		FileOutputStream out;
    		if(sheet==null) {
    			sheet=getCleanSheet();
    		}
    		
    		/**
    		 * A partir de la última que haya
			 * Fila 1: Encabezado métricas
			 * Filas 2 a N:Para cada métrica del informe una fila
			 * Fila N+1: Encabezado indicadores
			 * Filas N+2 a M: Para cada indicador una fila
			 */
    		    int rowIndex=sheet.getLastRowNum();
    		    rowIndex++;
    			sheet.createRow(rowIndex).createCell(0).setCellValue("Métricas tomadas el día ");
    			sheet.getRow(rowIndex).createCell(1).setCellValue(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString());
    			Collection<Metric> collection=report.getAllMetrics();
    			for(Metric metric:collection) {
    				persistMetric(metric);
    			}
    
    			
    			out = new FileOutputStream(filePath+fileName+".xls");
    			wb.write(out);
    			out.close();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}

   private void persistMetric(Metric metric) {
	   log.info("Introduzco métrica en la hoja");	   
	  
	   int rowIndex=sheet.getLastRowNum();
	   rowIndex++;
	   Row row=sheet.createRow(rowIndex);
	   log.info("Indice de fila nueva "+rowIndex);
	   int cellIndex=0;
	   //Aquí debería incorporar el formato de fuente en las celdas
	   row.createCell(cellIndex++).setCellValue(metric.getName());
	   row.createCell(cellIndex++).setCellValue(metric.getValue().toString());
	   row.createCell(cellIndex++).setCellValue(metric.getUnit());
	   row.createCell(cellIndex++).setCellValue(metric.getDescription());
	   row.createCell(cellIndex++).setCellValue(metric.getSource());
	   row.createCell(cellIndex).setCellValue(metric.getDate().toString());
	   log.info("Indice de celda final"+cellIndex);
	   
	   }
	   
   private void persistIndicator(Indicator indicator) {
	   log.info("Introduzco indicador en la hoja");
	   
	   
	   int rowIndex=sheet.getLastRowNum();
	   rowIndex++;
	   Row row=sheet.createRow(rowIndex);
	   log.info("Indice de fila nueva "+rowIndex);
	   int cellIndex=0;
	   //Aquí debería indicar el formato de fuente en las celdas, que dependerá del estado del índice
	   row.createCell(cellIndex++).setCellValue(indicator.getName());
	   row.createCell(cellIndex++).setCellValue(indicator.getValue().toString());

	   row.createCell(cellIndex++).setCellValue(indicator.getDescription());
	
	   row.createCell(cellIndex).setCellValue(indicator.getDate().toString());
	   log.info("Indice de celda final"+cellIndex);
	   
	   }

	@Override
	public void deleteReport() throws ReportNotDefinedException {
		// TODO Auto-generated method stub
		
	}    	 
    }


