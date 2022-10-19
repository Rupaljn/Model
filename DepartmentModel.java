package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.DepartmentBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class DepartmentModel extends BaseModel {

	/**
	 * @param dbean
	 * @return
	 */
	public static long add(DepartmentBean dbean) {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = 0;
		try {
			
			con = JDBCDataSource.getConnection();
			// Get auto-generated next primary key
			pk = nextNonBusinessPK("ST_Department");
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"INSERT INTO ST_Department(ID,CODE,NAME,DECRIPTION,COLLEGE_ID) VALUES(?,?,?,?,?)");
			ps.setLong(1, pk);
			ps.setLong(2, dbean.getCode());
			ps.setString(3, dbean.getName());
			ps.setString(4, dbean.getDecription());
			ps.setString(5, dbean.getCollegeId());
			ps.executeUpdate();
			con.commit();
			ps.close();

		} catch (Exception e) {

			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param dbean
	 * @return
	 */
	public static long delete(DepartmentBean dbean) {
		Connection con = null;
		PreparedStatement ps = null;
		if (findByPk(dbean) == null) {
			throw new NoRecordFoundException("Exception : no record found");
		}
		long pk = dbean.getId();

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_Department WHERE ID=?");
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.commit();
			ps.close();

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("Exception : Department " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;
	}

	/**
	 * @param dbean
	 * @return
	 * @throws ApplicationException
	 */
	public static DepartmentBean findByPk(DepartmentBean dbean) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		long pk = dbean.getId();
		dbean=null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_Department WHERE ID=?");
			ps.setLong(1, pk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dbean = new DepartmentBean();
				dbean.setId(rs.getLong(1));
				dbean.setCode(rs.getInt(2));
				dbean.setName(rs.getString(3));
				dbean.setDecription(rs.getString(4));
				dbean.setCollegeId(rs.getString(5));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}

		return dbean;
	}

	/**
	 * @param dbean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public static ArrayList<DepartmentBean> search(DepartmentBean dbean, int pageNo, int pageSize)
			throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<DepartmentBean> list = new ArrayList<DepartmentBean>();

		StringBuffer query = new StringBuffer("SELECT * FROM ST_Department WHERE 1=1 ");
		if (dbean != null) {
			if (dbean.getId() > 0) {
				query.append(" AND id =" + dbean.getId());
			}
			if (dbean.getCode() > 0) {
				query.append(" AND code =" + dbean.getCode());
			}
			if (dbean.getName() != null && dbean.getName().trim().length() > 0) {
				query.append(" AND name like '" + dbean.getName() + "%'");
			}
			if (dbean.getCollegeId() != null && dbean.getCollegeId().trim().length() > 0) {
				query.append(" AND college_id like '" + dbean.getCollegeId() + "%'");
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit" + pageNo + "," + pageSize);
		}

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dbean = new DepartmentBean();
				dbean.setId(rs.getLong(1));
				dbean.setCode(rs.getInt(2));
				dbean.setName(rs.getString(3));
				dbean.setDecription(rs.getString(4));
				dbean.setCollegeId(rs.getString(5));
				list.add(dbean);
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}

		return list;
	}

	/**
	 * @param dbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long update(DepartmentBean dbean)throws ApplicationException,NoRecordFoundException{
		
		Connection con=null;
		PreparedStatement ps=null;
		if(DepartmentModel.findByPk(dbean)==null){
			throw new NoRecordFoundException("Exception : no record found");
		}
		long pk=dbean.getId();
		try {
			con=JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps=con.prepareStatement("UPDATE ST_Department SET CODE=?,NAME=?,DECRIPTION=?,COLLEGE_ID=? WHERE ID=?");
			ps.setLong(1,dbean.getCode());
			ps.setString(2,dbean.getName());
			ps.setString(3,dbean.getDecription());
			ps.setString(4, dbean.getCollegeId());
			ps.setLong(5, pk);
			
			System.out.println(ps.executeUpdate()+"test = "+dbean.getId());
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
		throw new ApplicationException("de"+e.getMessage());
		}finally{
			JDBCDataSource.closeConnection(con);
		}
		
		
		
		return pk;
	}
	
}
