/**
 * <p>
 * Test para probar la clase ExcelReportManager
 * </p>
 * 
 * @author Ivan Matas
 *
 */
package us.muit.fs.a4i.test.persistence;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.persistence.ExcelReportManager;
import us.muit.fs.a4i.persistence.ReportFormater;
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
	private static ReportItem<Integer> itemIndicatorIntMock = Mockito.mock(ReportItem.class);

	@Mock(serializable = true)
	private static Indicator indicatorIntMock = Mockito.mock(Indicator.class);

	@Mock(serializable = true)
	private static ReportItem<Integer> itemIndicatorIntMock2 = Mockito.mock(ReportItem.class);

	@Mock(serializable = true)
	private static Indicator indicatorIntMock2 = Mockito.mock(Indicator.class);

	@Mock(serializable = true)
	private static ReportItem<Integer> itemIndicatorIntMock3 = Mockito.mock(ReportItem.class);

	@Mock(serializable = true)
	private static Indicator indicatorIntMock3 = Mockito.mock(Indicator.class);

	private static String excelPath;
	private static String excelName;
	private static ExcelReportManager underTest;

	/**
	 * <p>
	 * Acciones a realizar antes de ejecutar los tests definidos en esta clase
	 * </p>
	 * 
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	void ExcelCreation() {
		List<ReportItemI> listaMetric = new ArrayList<ReportItemI>();
		List<ReportItemI> listaInd = new ArrayList<ReportItemI>();
		Date fecha = new Date();
		Mockito.when(metricIntMock.getValue()).thenReturn(55);
		Mockito.when(metricIntMock.getName()).thenReturn("downloads");
		Mockito.when(metricIntMock.getUnit()).thenReturn("downloads");
		Mockito.when(metricIntMock.getDescription()).thenReturn("Descargas realizadas");
		Mockito.when(metricIntMock.getDate()).thenReturn(fecha);
		listaMetric.add(metricIntMock);

		Mockito.when(metricStrMock.getValue()).thenReturn("2-2-22");
		Mockito.when(metricStrMock.getName()).thenReturn("lastPush");
		Mockito.when(metricStrMock.getUnit()).thenReturn("date");
		Mockito.when(metricStrMock.getDescription()).thenReturn("Último push realizado en el repositorio");
		Mockito.when(metricStrMock.getDate()).thenReturn(fecha);
		listaMetric.add(metricStrMock);

		Mockito.when(itemIndicatorIntMock.getValue()).thenReturn(22);
		Mockito.when(itemIndicatorIntMock.getName()).thenReturn("otracosa");
		Mockito.when(itemIndicatorIntMock.getUnit()).thenReturn("cosas");
		Mockito.when(itemIndicatorIntMock.getDescription()).thenReturn("Indicador Ejemplo");
		Mockito.when(itemIndicatorIntMock.getDate()).thenReturn(fecha);
		Mockito.when(indicatorIntMock.getState()).thenReturn(IndicatorState.CRITICAL);
		Mockito.when(itemIndicatorIntMock.getIndicator()).thenReturn(indicatorIntMock);
		listaInd.add(itemIndicatorIntMock);

		Mockito.when(itemIndicatorIntMock2.getValue()).thenReturn(67);
		Mockito.when(itemIndicatorIntMock2.getName()).thenReturn("indicador2");
		Mockito.when(itemIndicatorIntMock2.getUnit()).thenReturn("unidad2");
		Mockito.when(itemIndicatorIntMock2.getDescription()).thenReturn("Indicador Ejemplo 2");
		Mockito.when(itemIndicatorIntMock2.getDate()).thenReturn(fecha);
		Mockito.when(indicatorIntMock2.getState()).thenReturn(IndicatorState.WARNING);
		Mockito.when(itemIndicatorIntMock2.getIndicator()).thenReturn(indicatorIntMock2);
		listaInd.add(itemIndicatorIntMock2);

		Mockito.when(itemIndicatorIntMock3.getValue()).thenReturn(98);
		Mockito.when(itemIndicatorIntMock3.getName()).thenReturn("indicador3");
		Mockito.when(itemIndicatorIntMock3.getUnit()).thenReturn("unidad3");
		Mockito.when(itemIndicatorIntMock3.getDescription()).thenReturn("IndicadorEjemplo3");
		Mockito.when(itemIndicatorIntMock3.getDate()).thenReturn(fecha);
		Mockito.when(indicatorIntMock3.getState()).thenReturn(IndicatorState.CRITICAL);
		Mockito.when(itemIndicatorIntMock3.getIndicator()).thenReturn(indicatorIntMock3);
		listaInd.add(itemIndicatorIntMock3);

		Mockito.when(informe.getAllMetrics()).thenReturn(listaMetric);
		Mockito.when(informe.getAllIndicators()).thenReturn(listaInd);
		Mockito.when(informe.getEntityId()).thenReturn("entidadTest");

		excelPath = new String("src" + File.separator + "test" + File.separator + "resources" + File.separator);
		excelName = new String("excelTest.xlsx");
		underTest = new ExcelReportManager(excelPath, excelName);

		underTest.setFormater(new ReportFormater());

		log.info("El informe tiene el id " + informe.getEntityId());
		underTest.saveReport(informe);

	}

	/**
	 * <p>
	 * Test para el m�todo de eliminar un informe en excel, solo verifica que si es
	 * null da excepcion
	 * </p>
	 * 
	 * @author Mariana Reyes Henriquez
	 */
	@Test
	void ExcelDelete() {
		excelPath = new String("src" + File.separator + "test" + File.separator + "resources" + File.separator);
		excelName = new String("excelTest.xlsx");
		Mockito.when(informe.getEntityId()).thenReturn("entidadTest");

		underTest = new ExcelReportManager(excelPath, excelName);
		underTest.setFormater(new ReportFormater());

		log.info("El informe tiene el id " + informe.getEntityId());
		underTest.saveReport(informe);
		try {
			log.info("Se intenta eliminar un informe que no existe");
			underTest.deleteReport(null);
			fail("Debería dar excepcion");
		} catch (ReportNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			underTest.deleteReport(informe);
		} catch (ReportNotDefinedException e) {
			fail("No debería dar la excepción");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}