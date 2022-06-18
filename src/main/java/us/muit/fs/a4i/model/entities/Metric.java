/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.io.IOException;
import java.time.LocalDateTime;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.exceptions.MetricException;

/**
 * @author Isabel Rom�n
 *
 */
public class Metric<T> {
	private static Logger log=Logger.getLogger(Metric.class.getName());
	/**
	 * Obligatorio
	 */
	private String name;
	/**
	 * Obligatorio
	 */
	private T value;
	/**
	 * Obligatorio
	 * Fecha en la que se tom� la medida (por defecto cuando se crea el objeto)
	 */
	private Date date;
	
	private String description;
	private String source;
	private String unit;
	/**
	 * Construye un objeto m�trica a partir de un constructor, previamente configurado
	 * S�lo lo utiliza el propio constructor, es privado, nadie, que no sea el constructor, puede crear una m�trica
	 * @param builder Constructor de la m�trica
	 */
	private Metric(MetricBuilder<T> builder){
		
		this.description=builder.description;
		this.name=builder.name;
		this.value=builder.value;
		this.source=builder.source;
		this.unit=builder.unit;
		this.date=builder.date;
	}
	
	/**
	 * Obtiene la descripci�n de la m�trica
	 * @return Descripci�n del significado de la m�trica
	 */
	public String getDescription() {
		return description;
	}
	
	
	/**
	 * Consulta el nombre de la m�trica
	 * @return Nombre de la m�trica
	 */
	public String getName() {
		return name;
	}
	/**
	 * Consulta el valor de la m�trica
	 * @return Medida
	 */
	public T getValue() {
		return value;
	}
	/**
	 * Consulta la fuente de informaci�n
	 * @return Origen de la medida
	 */
	public String getSource() {
		return source;
	}
	/***
	 * Establece la fuente de la informaci�n para la medida
	 * @param source fuente de informaci�n origen
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * Consulta las unidades de medida
	 * @return la unidad usada en la medida
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * Consulta cuando se obtuvo la m�trica
	 * @return Fecha de consulta de la m�trica
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * <p>Clase para construir m�tricas. Verifica las m�tricas antes de crearlas</p>
	 *
	 */
	public static class MetricBuilder<T>{
		private String description;
		private String name;
		private Date date;
		private T value;
		private String source;
		private String unit;
		public MetricBuilder(String metricName, T metricValue) throws MetricException {
			HashMap<String,String> metricDefinition=null;
			//el nombre incluye java.lang, si puede eliminar si se manipula la cadena
			//hay que quedarse s�lo con lo que va detr�s del �ltimo punto o meter en el fichero el nombre completo
			//Pero �y si se usan tipos definidos en otras librer�as? usar el nombre completo "desambigua" mejor
			log.info("Verifico La m�trica de nombre "+metricName+" con valor de tipo "+metricValue.getClass().getName());
			try {
			metricDefinition=Context.getContext().getChecker().definedMetric(metricName,metricValue.getClass().getName());
					
			if(metricDefinition!=null) {				
				this.name=metricName;
				this.value=metricValue;			
				this.date=Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
				this.description=metricDefinition.get("description");
				this.unit=metricDefinition.get("unit");
			}else {
				throw new MetricException("M�trica "+metricName+" no definida o tipo "+metricValue.getClass().getName()+" incorrecto");
			}
			}catch(IOException e) {
				throw new MetricException("El fichero de configuraci�n de m�tricas no se puede abrir");
			}
			
			
		}
		/**
		 * <p>Establece la descripci�n de la m�trica</p>
		 * @param description Breve descripci�n del significado de la m�trica
		 * @return El propio constructor
		 */
		public MetricBuilder<T> description(String description){
			this.description=description;
			return this;
		}
		/**
		 * <p>Establece la fuente de informaci�n</p>
		 * @param source Fuente de la que se extrajeron los datos
		 * @return El propio constructor
		 */
		public MetricBuilder<T> source(String source){
			this.source=source;
			return this;
		}
		/**
		 * <p>Establece las unidades de medida</p>
		 * @param unit Unidades de medida de la m�trica
		 * @return El propio constructor
		 */
		public MetricBuilder<T> unit(String unit){
			this.unit=unit;
			return this;
		}
		public Metric<T> build(){
			return new Metric<T>(this);			
		}
	}
	
	@Override
	public String toString() {
		String info;
		info="M�trica para "+description+", con valor=" + value + ", source=" + source
				+ ", unit=" + unit +" fecha de la medida=  "+ date;
		return info;
	}
	
}
