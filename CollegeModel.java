package in.sterling.model;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mchange.v2.c3p0.test.PSLoadPoolBackedDataSource;

import in.sterling.bean.CollegeBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.DuplicateRecordException;

/*
 * @author Sterling
 * @version 1.0
 */

public class CollegeModel extends BaseModel {

	// private static Logger log = Logger.getLogger(StudentModel.class);
	/**
	 * created next PK of record
	 *
	 * @throws DatabaseException
	 */

	/**
	 * Add COLLEGE Info
	 *
	 * @param model
	 * @throws DatabaseException
	 *
	 */
	public static long add(CollegeBean cbean) throws ApplicationException {
		// log.debug("RoleModel add started.");
		String sql = "INSERT INTO ST_COLLEGE(id,College_name,address,state,city,phno,created_By,modified_by,created_datetime,modified_datetime,College_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		long pk = 0;
		if(findByName(cbean.getName())!=null){
			throw new DuplicateRecordException("CollegeName is already exist");
		}
		if(findByCollegeId(cbean)!=null){
			throw new DuplicateRecordException("CollegeId is already exist");
		}
		try {
			con = JDBCDataSource.getConnection();
			pk = nextNonBusinessPK("st_college");
			con.setAutoCommit(false);
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, pk);
			pstmt.setString(2, cbean.getName());
			pstmt.setString(3, cbean.getAddress());
			pstmt.setString(4, cbean.getState());
			pstmt.setString(5, cbean.getCity());
			pstmt.setString(6, cbean.getPhoneNo());
			pstmt.setString(7, cbean.getCreatedBy());
			pstmt.setString(8, cbean.getModifiedBy());
			pstmt.setTimestamp(9, cbean.getCreatedDateTime());
			pstmt.setTimestamp(10, cbean.getModifiedDateTime());
			pstmt.setString(11, cbean.getCollegeId());

			int c = pstmt.executeUpdate();
			System.out.println("Rows Inserted" + c);
			con.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Inserting Data ");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("RoleModel add ended.");
		return pk;
	}

	/**
	 * Delete a College
	 *
	 * @param cbean
	 * @throws DatabaseException
	 */
	public static void delete(CollegeBean cbean) throws ApplicationException {
		// log.debug("CollegeModel delete Started");
		Connection con = null;
		try {
			String query = "DELETE FROM ST_COLLEGE WHERE ID=?";
			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setLong(1, cbean.getId());
			pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(""+e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("CollegeModel delete Ended.");
	}

	/**
	 * Find User by College
	 *
	 * @param login
	 *            : get parameter
	 * @return cbean
	 * @throws DatabaseException
	 */
	public static CollegeBean findByName(String name) throws ApplicationException {
		// log.debug("CollegeModel findByName Started");
		Connection con = null;
		CollegeBean cbean = null;
		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ST_COLLEGE WHERE COLLEGE_NAME = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cbean = new CollegeBean();

				cbean.setId(rs.getLong(1));
				cbean.setCollegeId(rs.getString(2));
				cbean.setName(rs.getString(3));
				cbean.setAddress(rs.getString(4));
				cbean.setState(rs.getString(5));
				cbean.setCity(rs.getString(6));
				cbean.setPhoneNo(rs.getString(7));
				cbean.setCreatedBy(rs.getString(8));
				cbean.setModifiedBy(rs.getString(9));
				cbean.setCreatedDateTime(rs.getTimestamp(10));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting College by Name");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("CollegeModel findByName End");
		return cbean;
	}

	
	
	
	public static CollegeBean findByCollegeId(CollegeBean cbean) throws ApplicationException {
		// log.debug("CollegeModel findByName Started");
		Connection con = null;
		String collegeId=cbean.getCollegeId();
		cbean=null;
		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ST_COLLEGE WHERE COLLEGE_ID = ?");
			pstmt.setString(1, collegeId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cbean = new CollegeBean();
				cbean.setId(rs.getLong(1));
				cbean.setCollegeId(rs.getString(2));
				cbean.setName(rs.getString(3));
				cbean.setAddress(rs.getString(4));
				cbean.setState(rs.getString(5));
				cbean.setCity(rs.getString(6));
				cbean.setPhoneNo(rs.getString(7));
				cbean.setCreatedBy(rs.getString(8));
				cbean.setModifiedBy(rs.getString(9));
				cbean.setCreatedDateTime(rs.getTimestamp(10));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting College by Name");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("CollegeModel findByName End");
		return cbean;
	}

	
	
	/**
	 * Find User by College
	 *
	 * @param pk
	 *            : get parameter
	 * @return cbean
	 * @throws DatabaseException
	 */
	public static CollegeBean findByPK(CollegeBean cbean) throws ApplicationException {
		// log.debug("CollegeModel findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE ID=?");
		Connection con = null;
		
		long pk=cbean.getId();
		cbean = null;
		try {

			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cbean = new CollegeBean();
				cbean.setId(rs.getLong(1));
				cbean.setCollegeId(rs.getString(2));
				cbean.setName(rs.getString(3));
				cbean.setAddress(rs.getString(4));
				cbean.setState(rs.getString(5));
				cbean.setCity(rs.getString(6));
				cbean.setPhoneNo(rs.getString(7));
				cbean.setCreatedBy(rs.getString(8));
				cbean.setModifiedBy(rs.getString(9));
				cbean.setCreatedDateTime(rs.getTimestamp(10));
				cbean.setModifiedDateTime(rs.getTimestamp(11));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			// log.error("Database Exception..", e);
			throw new ApplicationException("Exception :"+e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("CollegeModel findByPK End");
		return cbean;
	}

	/**
	 * Update a College
	 *
	 * @param cbean
	 * @throws DatabaseException
	 */
	public static void update(CollegeBean cbean) throws ApplicationException,DuplicateRecordException{
		// log.debug("CollegeModel update Started");

		CollegeBean cbeanExist = findByName(cbean.getName());
		Connection con = null;

		if (cbeanExist != null && cbeanExist.getId() != cbean.getId()) {

			throw new DuplicateRecordException("College is already exist");
		}

		try {
			String query = "UPDATE ST_COLLEGE SET COLLEGE_NAME=?,ADDRESS=?,STATE=?,CITY=?,PHNO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=?,COLLEGE_ID=? WHERE ID=?";
			con = JDBCDataSource.getConnection();

			con.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, cbean.getName());
			pstmt.setString(2, cbean.getAddress());
			pstmt.setString(3, cbean.getState());
			pstmt.setString(4, cbean.getCity());
			pstmt.setString(5, cbean.getPhoneNo());
			pstmt.setString(6, cbean.getCreatedBy());
			pstmt.setString(7, cbean.getModifiedBy());
			pstmt.setTimestamp(8, cbean.getCreatedDateTime());
			pstmt.setTimestamp(9, cbean.getModifiedDateTime());
			pstmt.setString(10,cbean.getCollegeId());
			pstmt.setLong(11, cbean.getId());
			int c = pstmt.executeUpdate();
			con.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			// log.error("Database Exception..", e);
				e.printStackTrace();
				throw new ApplicationException("Exception : Delete rollback exception " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		// log.debug("CollegeModel update End");
	}

	/**
	 * Search College with pagination
	 *
	 * @return list : List of Users
	 * @param cbean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 *
	 * @throws DatabaseException
	 */
	public static ArrayList<CollegeBean> search(CollegeBean cbean, int pageNo, int pageSize) throws ApplicationException {

		Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE 1=1 ");

		if (cbean != null) {
/*			if (cbean.getId() > 0) {
				sql.append(" AND id = " + cbean.getId());
			}
*/
			if (cbean.getName() != null && cbean.getName().trim().length() > 0) {
				sql.append(" AND COLLEGE_NAME like '" + cbean.getName() + "%'");
			}
			
	/*		if (cbean.getAddress() != null && cbean.getAddress().trim().length() > 0) {
				sql.append(" AND ADDRESS like '" + cbean.getAddress() + "%'");
			}
			if (cbean.getState() != null && cbean.getState().trim().length() > 0) {
				sql.append(" AND STATE like '" + cbean.getState() + "%'");
			}
			if (cbean.getCity() != null && cbean.getCity().trim().length() > 0) {
				sql.append(" AND CITY like '" + cbean.getCity() + "%'");
			}
			if (cbean.getPhoneNo() != null && cbean.getPhoneNo().trim().length() > 0) {
				sql.append(" AND PHNO = " + cbean.getPhoneNo());
			}*/

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" Limit " + pageNo + ", " + pageSize);
		}

	ArrayList<CollegeBean> list = new ArrayList<CollegeBean>();

		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(sql);
			while (rs.next()) {
				cbean = new CollegeBean();
				cbean.setId(rs.getLong(1));
				cbean.setCollegeId(rs.getString(2));
				cbean.setName(rs.getString(3));
				cbean.setAddress(rs.getString(4));
				cbean.setState(rs.getString(5));
				cbean.setCity(rs.getString(6));
				cbean.setPhoneNo(rs.getString(7));
				cbean.setCreatedBy(rs.getString(8));
				cbean.setModifiedBy(rs.getString(9));
				cbean.setCreatedDateTime(rs.getTimestamp(10));
				cbean.setModifiedDateTime(rs.getTimestamp(11));
				list.add(cbean);
			}
			rs.close();
		} catch (Exception e) {
			// log.error("Database Exception..", e);
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search college");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		// log.debug("CollegeModel search Ended.");
		return list;
	}

	/**
	 * Get List of College with pagination
	 *
	 * @return list : List of College
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */
	public static ArrayList<CollegeBean> list(int pageNo, int pageSize) throws ApplicationException {
		// log.debug("CollegeModel list Started");
		Connection con = null;
		ArrayList<CollegeBean> list = new ArrayList<CollegeBean>();

		StringBuffer sql = new StringBuffer("select * from ST_COLLEGE where 1=1 ");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CollegeBean cbean = new CollegeBean();
				cbean.setId(rs.getLong(1));
				cbean.setCollegeId(rs.getString(2));
				cbean.setName(rs.getString(3));
				cbean.setAddress(rs.getString(4));
				cbean.setState(rs.getString(5));
				cbean.setCity(rs.getString(6));
				cbean.setPhoneNo(rs.getString(7));
				cbean.setCreatedBy(rs.getString(8));
				cbean.setModifiedBy(rs.getString(9));
				cbean.setCreatedDateTime(rs.getTimestamp(10));
				cbean.setModifiedDateTime(rs.getTimestamp(11));
				list.add(cbean);
			}
			rs.close();
		} catch (Exception e) {
			// log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		// log.debug("CollegeModel list Ended");
		return list;

	}
	public static void main(String[] args) {
		CollegeBean cbean = new CollegeBean();
		cbean.setCollegeId("");
		cbean.setName("");
		cbean.setAddress("");
		cbean.setState("");
		cbean.setCity("");
		cbean.setPhoneNo("");
		System.out.println(CollegeModel.add(cbean));
	
	}
}