/**
 * <p>
 * Test para probar la clase ExcelReportManager
 * </p>
 * 
 * @author Iv�n Matas Gonz�lez
 *
 */
package us.muit.fs.a4i.test.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.control.ReportManager;
import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.persistence.ExcelReportManager;
import us.muit.fs.a4i.persistence.ReportFormaterI;

@ExtendWith(MockitoExtension.class)

class ExcelReportManagerTest {
	private static Logger log = Logger.getLogger(ExcelReportManager.class.getName());

	@Captor
	private ArgumentCaptor<Integer> intCaptor;
	@Captor
	private ArgumentCaptor<String> strCaptor;
	@Captor
	private ArgumentCaptor<ReportFormaterI> FormaterCaptor;

	@Mock(serializable = true)
	private static ReportItem<Integer> metricIntMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static ReportItem<String> metricStrMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static ReportI informe = Mockito.mock(ReportI.class);
	@Mock(serializable = true)
	private static ReportItem<Integer> reportItemMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static IndicatorI indicatorIMock = Mockito.mock(IndicatorI.class);
	private static String excelPath;
	private static String excelName;
	private static ExcelReportManager underTest;
	/**
	 * <p>Acciones a realizar antes de ejecutar los tests definidos en esta clase</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}
	
	
	void ExcelCreation() {
		List<ReportItemI> lista=new ArrayList<ReportItemI>();
		Date fecha=new Date();
		Mockito.when(metricIntMock.getValue()).thenReturn(55);
		Mockito.when(metricIntMock.getName()).thenReturn("downloads");
		Mockito.when(metricIntMock.getUnit()).thenReturn("downloads");
		Mockito.when(metricIntMock.getDescription()).thenReturn("Descargas realizadas");		
		Mockito.when(metricIntMock.getDate()).thenReturn(fecha);		
		lista.add(metricIntMock);
		
		Mockito.when(metricStrMock.getValue()).thenReturn("2-2-22");
		Mockito.when(metricStrMock.getName()).thenReturn("lastPush");
		Mockito.when(metricStrMock.getUnit()).thenReturn("date");
		Mockito.when(metricStrMock.getDescription()).thenReturn("Último push realizado en el repositorio");		
		Mockito.when(metricStrMock.getDate()).thenReturn(fecha);	
		lista.add(metricStrMock);
				
		Mockito.when(informe.getAllMetrics()).thenReturn(lista);
		Mockito.when(informe.getEntityId()).thenReturn("entidadTest");
		
		excelPath = new String("src" + File.separator + "test" + File.separator + "resources"+File.separator);
		excelName= new String("excelTest");
		underTest=new ExcelReportManager(excelPath,excelName);	
		try {
			log.info("El informe tiene el id "+informe.getEntityId());
			underTest.saveReport(informe);
		} catch (ReportNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
		void Test_persistencia() throws IOException {
		excelPath = new String("src" + File.separator + "test" + File.separator + "resources"+File.separator);
		excelName= new String("excelTest");
		underTest=new ExcelReportManager(excelPath,excelName);	
		
		ReportManagerI reporte = new ReportManager(ReportI.ReportType.PROJECT);
		ReportI reporte_aux = null;
		try {
			reporte_aux = reporte.newReport("prueba", ReportI.ReportType.PROJECT);
		}catch (Exception e) {
			e.printStackTrace();
			fail("fallo");
		}
		try {
			underTest.saveReport(reporte_aux);
		} catch (ReportNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 * 
	 */

	@Test
	void Test_persistIndicator() throws IOException {
		
		// Mockito ReportI
		Mockito.when(informe.getIndicatorByName("downloads")).thenReturn(reportItemMock);
		// Mockito ReportItemI
		List<ReportItemI> lista=new ArrayList<ReportItemI>();
		Date fecha=new Date();
		Mockito.when(metricIntMock.getValue()).thenReturn(55);
		Mockito.when(metricIntMock.getName()).thenReturn("downloads");
		Mockito.when(metricIntMock.getUnit()).thenReturn("downloads");
		Mockito.when(metricIntMock.getDescription()).thenReturn("Descargas realizadas");		
		Mockito.when(metricIntMock.getDate()).thenReturn(fecha);
		Mockito.when(metricIntMock.getIndicator()).thenReturn(indicatorIMock);
		Mockito.when(metricIntMock.getSource()).thenReturn("GitHub");
		lista.add(metricIntMock);
		// Mockito IndicatorI
		Mockito.when(indicatorIMock.getState()).thenReturn(IndicatorI.IndicatorState.OK);
		Mockito.when(indicatorIMock.getMetrics()).thenReturn(lista);
		
		excelPath = new String("src" + File.separator + "test" + File.separator + "resources"+File.separator);
		excelName= new String("excelTest");
		underTest=new ExcelReportManager(excelPath,excelName);
		
		informe.addIndicator(metricIntMock);
		// Guardar el reporte
		try {
			underTest.saveReport(informe);
		} catch (ReportNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Se abre el excel y se comprueba el color de la celda.
		FileInputStream file = new FileInputStream(new File("howtodoinjava_demo.xlsx"));
		 
        //Create Workbook instance holding reference to .xlsx file
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        HSSFSheet sheet = workbook.getSheetAt(0);
        
        HSSFColor color = sheet.getRow(sheet.getLastRowNum()).getCell(0).getCellStyle().getFillBackgroundColorColor();
        assertEquals(color,HSSFColor.HSSFColorPredefined.BLACK,"el color debería de ser negro");
        
	}
}