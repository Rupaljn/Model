package in.sterling.model;

import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class JDbcDataSourceTest{

	//Declare private static class variable of self type
	private static JDbcDataSourceTest datasource=null;
	//Make default constroctor private so the no other class will instantiat it 
	private JDbcDataSourceTest(){
		
	}
	
	//Declare private instance variable of CombopooledDS 
	private ComboPooledDataSource cpds=null;
	
	public static JDbcDataSourceTest getInstance(){
		
		if(datasource==null){
			
			datasource=new JDbcDataSourceTest();
			datasource.cpds=new ComboPooledDataSource();
			ResourceBundle rb=ResourceBundle.getBundle("in.sterling.bundle.system");
			
			try {
				datasource.cpds.setDriverClass(rb.getString("driver"));
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			datasource.cpds.setJdbcUrl(rb.getString("url"));
			datasource.cpds.setUser(rb.getString("user"));
			datasource.cpds.setPassword(rb.getString("password"));
			datasource.cpds.setInitialPoolSize(2);
			datasource.cpds.setMaxPoolSize(100);
			datasource.cpds.setMinPoolSize(10);
			datasource.cpds.setAcquireIncrement(10);
			
			
			
		}
		
		
		return datasource;
	}
	
	
	
	
	
	
}