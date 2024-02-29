/**
 * 
 */
package us.muit.fs.a4i.persistence;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
//import java.awt.Font;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.Font;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * <p>
 * Clase que cotendrá las funciones de manejo de excel comunes al manejo de
 * cualquier informe
 * </p>
 * <p>
 * Se utiliza la API apachePOI para manejar los ficheros excel
 * </p>
 * <p>
 * Las primeras versiones se centran en la escritura
 * </p>
 * <p>
 * Política de informes: un informe es una hoja de un documento excel,
 * identificada con el id del informe
 * </p>
 * <p>
 * Este Gestor tiene los métodos para obtener la hoja y persistirla
 * </p>
 * <p>
 * Si la hoja exist�a la recupera y se añadirá sobre ella, no se elimina lo
 * anterior, si no existía se crea nueva
 * </p>
 * 
 * @author Isabel Román
 * 
 *
 */
public class ExcelReportManager implements PersistenceManager, FileManager {
	private static Logger log = Logger.getLogger(ExcelReportManager.class.getName());
	/**
	 * <p>
	 * Referencia al gestor de estilo que se va a utilizar
	 * </p>
	 */
	protected ReportFormaterI formater;
	
	FileInputStream inputStream = null;

	/**
	 * <p>
	 * Localización del fichero excel
	 * </p>
	 */
	protected String filePath = "";
	/**
	 * <p>
	 * Nombre del fichero excel
	 * </p>
	 */
	protected String fileName = "";

	protected HSSFWorkbook wb = null;
	protected HSSFSheet sheet = null;

	public ExcelReportManager(String filePath, String fileName) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
	}

	public ExcelReportManager() {
		super();
	}


	@Override
	public void setFormater(ReportFormaterI formater) {
		log.info("Establece el formateador");
		this.formater = formater;

	}

	@Override
	public void setPath(String path) {
		log.info("Establece la ruta al fichero");
		this.filePath = path;

	}

	@Override
	public void setName(String name) {
		log.info("Establece el nombre del fichero");
		this.fileName = name;

	}

	/**
	 * <p>
	 * El libro contendrá todos los informes de un tipo concreto. Primero hay que
	 * abrir el libro. Busco la hoja correspondiente a esta entidad, si ya existe la
	 * elimino. Creo la hoja
	 * </p>
	 * 
	 * @return Hoja de excel
	 * @throws IOException                error al abrir el fichero
	 * @throws EncryptedDocumentException documento protegido
	 */
	protected HSSFSheet getCleanSheet(String entityId) throws EncryptedDocumentException, IOException {
		log.info("Solicita una hoja nueva del libro manejado, para la entidad con id: "+entityId);
		if (wb == null) {
			inputStream = new FileInputStream(filePath + fileName + ".xls");
			wb = (HSSFWorkbook) WorkbookFactory.create(inputStream);
			log.info("Generado workbook");

		}
		if (sheet == null) {
			/**
			 * int templateIndex=wb.getSheetIndex("Template"); HSSFSheet sheet =
			 * wb.cloneSheet(templateIndex); int newIndex=wb.getSheetIndex(sheet);
			 **/
			/**
			 * <p>
			 * Verifico si la hoja existe y si es así la extraigo
			 * </p>
			 * <p>
			 * Si no existe la creo.
			 */
			sheet = wb.getSheet(entityId.replaceAll("/", "."));

			if (sheet != null) {
				log.info("Recuperada hoja, que ya existía");
				/*
				 * Si la hoja existe la elimino
				 */
				int index = wb.getSheetIndex(sheet);
				wb.removeSheetAt(index);
			}
			sheet = wb.createSheet(entityId.replaceAll("/", "."));
			log.info("Creada hoja nueva");

		}

		return sheet;
	}

	/**
	 * Guarda en un hoja limpia con el nombre del id del informe todas las métricas
	 * y los indicadores que incluya
	 */
	@Override
	public void saveReport(ReportI report) throws ReportNotDefinedException {
		log.info("Guardando informe con id: "+report.getEntityId());
		if (report == null) {
			throw new ReportNotDefinedException();
		}
		try {
			FileOutputStream out;
			if (sheet == null) {
				sheet = getCleanSheet(report.getEntityId());
			}

			/**
			 * A partir de la última que haya Fila 1: Encabezado métricas Filas 2 a N:Para
			 * cada métrica del informe una fila Fila N+1: Encabezado indicadores Filas N+2
			 * a M: Para cada indicador una fila
			 */
			int rowIndex = sheet.getLastRowNum();
			rowIndex++;
			sheet.createRow(rowIndex).createCell(0).setCellValue("Métricas guardadas el día ");
			sheet.getRow(rowIndex).createCell(1)
					.setCellValue(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString());
			Collection<ReportItemI> collection = report.getAllMetrics();
			for (ReportItemI metric : collection) {
				persistMetric(metric);
			}
			//Ahora irían los indicadores
			rowIndex++;
            sheet.createRow(rowIndex).createCell(0).setCellValue("Indicadores");
			collection = report.getAllIndicators();
			for (ReportItemI indicator : collection) {
				persistIndicator(indicator);
			}
            
			out = new FileOutputStream(filePath + fileName + ".xls");
			wb.write(out);
			out.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private void persistMetric(ReportItemI metric) throws IOException {
		log.info("Introduzco métrica en la hoja");

		int rowIndex = sheet.getLastRowNum();
		rowIndex++;
		Row row = sheet.createRow(rowIndex);
		log.info("Indice de fila nueva " + rowIndex);
		int cellIndex = 0;
		// Aquí debería incorporar el formato de fuente en las celdas
		// docs sacados de aquí https://www.javatpoint.com/apache-poi-excel-font
		// https://www.e-iceblue.com/Tutorials/Java/Spire.XLS-for-Java/Program-Guide/Cells/Apply-Fonts-in-Excel-in-Java.html

		CellStyle style = wb.createCellStyle();
		
		// Obtengo los datos de la fuente a utilizar
		// Se crea un objeto de tipo Font (definido en la carpeta entities) con los valores por defectos del constructor
		// Con dichos valores por defecto (el color) se configura el estilo de las celdas
		org.apache.poi.ss.usermodel.Font fuenteJava = (org.apache.poi.ss.usermodel.Font) formater.getMetricFont();
		Font fuente = new Font();
		
		//Establezco el color y la fuente a utilizar en la m�trica
		style.setFont(fuenteJava);
		style.setFillForegroundColor(Short.parseShort(fuente.getColor()));

		
		row.createCell(cellIndex++).setCellValue(metric.getName());
		row.createCell(cellIndex++).setCellValue(metric.getValue().toString());
		row.createCell(cellIndex++).setCellValue(metric.getUnit());
		row.createCell(cellIndex++).setCellValue(metric.getDescription());
		row.createCell(cellIndex++).setCellValue(metric.getSource());
		row.createCell(cellIndex).setCellValue(metric.getDate().toString());
		log.info("Indice de celda final" + cellIndex);

	}

	private void persistIndicator(ReportItemI indicator) {
		log.info("Introduzco indicador en la hoja");
        //Mantengo uno diferente porque en el futuro la información del indicador será distinta a la de la métrica
		int rowIndex = sheet.getLastRowNum();
		rowIndex++;
		Row row = sheet.createRow(rowIndex);
		log.info("Indice de fila nueva " + rowIndex);
		int cellIndex = 0;

		// Aquí debería indicar el formato de fuente en las celdas, que dependerá del
		// estado del índice

		CellStyle style = wb.createCellStyle();

		try { // ha sido necesario modificar el tipo porque hab�a conflictos con lo nuevo a�adido en el m�todo persistMetric
			style.setFont((org.apache.poi.ss.usermodel.Font) formater.getIndicatorFont(indicator.getIndicator().getState()));
		} catch (IOException e) {
			log.warning("Problema al abrir el fichero con los formatos");
			e.printStackTrace();
		}

		row.createCell(cellIndex++).setCellValue(indicator.getName());
		row.createCell(cellIndex++).setCellValue(indicator.getValue().toString());
		row.createCell(cellIndex++).setCellValue(indicator.getUnit());
		row.createCell(cellIndex++).setCellValue(indicator.getDescription());

		row.createCell(cellIndex++).setCellValue(indicator.getIndicator().getState().toString());

		row.createCell(cellIndex++).setCellValue(indicator.getSource());

		row.createCell(cellIndex).setCellValue(indicator.getDate().toString());

		log.info("Indice de celda final " + cellIndex);

	}

	@Override
	public void deleteReport(ReportI report) throws ReportNotDefinedException {
		// TODO Auto-generated method stub
		log.info("Eliminando informe excel");
		if (report == null) {
			throw new ReportNotDefinedException();
		}
		try {
			sheet = wb.getSheet(report.getEntityId());
			if (sheet != null) {
				int index = wb.getSheetIndex(sheet);
				wb.removeSheetAt(index);
		}
		}catch (Exception e) { 
			
			e.printStackTrace();
		
		
		
	}
	}
}
