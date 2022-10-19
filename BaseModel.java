package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import in.sterling.bean.UserBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;


/**
 * 
 * BaseModel contain common attribute and methods
 * 
 * 
 * 
 * */

public class BaseModel {
	
	/**
	 * 
	 * nextNonBusinessPK method used to create non business primary key
	 * 
	 * @param dbName
	 * @return long
	 * @throws ApplicationException
	 * 
	 */
	public static long nextNonBusinessPK(String dbname) throws ApplicationException{
		Connection con=null;
		long id = 0;
		try {
			con = JDBCDataSource.getConnection();
			Statement statement = con.createStatement();
			String query = "select max(id) from "+dbname;
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return id + 1;
	}	

}
