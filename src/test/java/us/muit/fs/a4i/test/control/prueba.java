/**
 * 
 */
package us.muit.fs.a4i.test.control;

import java.io.IOException;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;
import us.muit.fs.a4i.persistence.PersistenceManager;

/**
 * @author isa
 *
 */
public class prueba {

	/**
	 * @param args
	 * @throws ReportNotDefinedException
	 */
	public static void main(String[] args) throws ReportNotDefinedException {

		ReportManagerI manager;
		ReportI report;
		PersistenceManager persister;
		RemoteEnquirer builder;
		try {
			System.out.println("Listado de propiedades disponibles " + Context.getContext().getPropertiesNames());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * manager=new ReportManager(); builder=new GitHubRepositoryEnquirer();
		 * 
		 * persister=new ExcelReportManager(); manager.setRemoteBuilder(builder);
		 * manager.setPersistenceManager(persister);
		 * 
		 * report=manager.createReport("MIT-FS/Audit4Improve-API");
		 * 
		 * try { FileManager file=(FileManager) persister; file.setName("test");
		 * manager.save();
		 * 
		 * } catch (ReportNotDefinedException e) {
		 * System.out.println("Cuidado tiene que establecer el informe");
		 * e.printStackTrace(); }
		 */
	}
}
