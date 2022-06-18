/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Logger;


/**
 * @author Isabel Román
 *
 */
public class Indicator<T> {
	private static Logger log=Logger.getLogger(Indicator.class.getName());
	private Date date;
	private String source;
	private String description;
	private String name;
	private T value;
	private String unit;
	private State state;

	/**
	 * <p>Estados posibles del indicador, indican el grado de atención que requiere por parte del analista</p>
	 * @author Isabel Román
	 *
	 */
	public static enum State{
		OK,
		WARNING,
		CRITICAL
	};
	/**
	 * <p>Constructor privado, para construir desde fuera hay que usar el método build de IndicatorBuilder. Implementación patrón constructor</p>
	 * @param builder Clase constructora
	 */
	private Indicator(IndicatorBuilder<T> builder){
		
		this.description=builder.description;
		this.name=builder.name;
		this.value=builder.value;
		this.source=builder.source;
		this.unit=builder.unit;
		this.date=builder.date;
		this.state=builder.state;
	}
	/**
	 * 
	 * @return Descripción del indicador
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 
	 * @return Nombre del indicador
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @return Unidades de medida del indicador
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * 
	 * @return Valor del indicador
	 */
	public T getValue() {
		return value;
	}
	/**
	 * <p>Clase constructora, se usará el método build para crear un objeto Indicator desde fuera</p> 
	 * @author Isabel Román
	 *
	 * @param <T> Tipo utilizado en el valor del indicador que va a construir
	 */
	public static class IndicatorBuilder<T>{
		private String description;
		private String name;
		private Date date;
		private T value;
		private String source;
		private String unit;
		private State state;
		/**
		 * <p>Constructor de la clase constructora, recibe los parámetros obligatgorios nombre y valor del indicador</p>
		 * @param indicatorName Nombre del indicador
		 * @param indicatorValue Valor del indicador
		 */
		public IndicatorBuilder(String indicatorName, T indicatorValue) {
			this.name=indicatorName;
			this.value=indicatorValue;
			this.date=Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		}
		/**
		 * <p>Establece la descripción en el constructor</p>
		 * @param description descripción del indicador
		 * @return el objeto constructor
		 */
		public IndicatorBuilder<T> description(String description){
			this.description=description;
			return this;
		}
		/**
		 * 
		 * @param source Fuente de la que se obtuvo el indicador
		 * @return el objeto constructor
		 */
		public IndicatorBuilder<T> source(String source){
			this.source=source;
			return this;
		}
		/**
		 * 
		 * @param unit Unidad de medida del indicador
		 * @return el objeto constructor
		 */
		public IndicatorBuilder<T> unit(String unit){
			this.unit=unit;
			return this;
		}
		/**
		 * 
		 * @param state Estado del indicador. Grado de atención que necesita por parte del analista
		 * @return el objeto constructor
		 */
		public IndicatorBuilder<T> state(State state){
			this.state=state;
			return this;
		}
		/**
		 * <p>Método de construcción del indicador, patrón constructor</p>
		 * @return El indicador construido
		 */
		public Indicator<T> build(){
			return new Indicator<T>(this);			
		}
	}
	@Override
	public String toString() {
		String info;
		info="Indicador para "+description+", con valor=" + value + ", source=" + source
				+ ", unit=" + unit +" fecha de cálculo=  "+ date+" Estado= "+state;
		return info;
	}
	/**
	 * 
	 * @return Fecha de creación del indicador
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * 
	 * @return Fuente de los datos para obtener el indicador
	 */
	public String getSource() {
		return source;
	}
	
}