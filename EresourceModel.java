package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.EResourceBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class EresourceModel extends BaseModel {

	/**
	 * @param erbean
	 * @return
	 * @throws ApplicationException
	 */
	public static long add(EResourceBean erbean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = nextNonBusinessPK("ST_ERESOURCE");
		String query = "INSERT INTO ST_ERESOURCE (ID,TABLES_CONTAINS,NAME,DETAIL,created_by,modified_by) VALUES(?,?,?,?,?,?)";
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setLong(1, pk);
			ps.setString(2, erbean.getTableContains());
			ps.setString(3, erbean.getName());
			ps.setString(4, erbean.getDetail());
			ps.setString(5, erbean.getCreatedBy());
			ps.setString(6, erbean.getModifiedBy());
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();

			throw new ApplicationException("EresourceBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param erbean
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public static long delete(EResourceBean erbean) throws NoRecordFoundException, ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		if (findByPk(erbean) == null) {
			throw new NoRecordFoundException("EresourceBean : no record found");
		}
		long pk = erbean.getId();
		String query = "DELETE FROM ST_ERESOURCE WHERE ID=?";
	
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("EresourceBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;
	}

	/**
	 * @param erbean
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public static long update(EResourceBean erbean) throws NoRecordFoundException, ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		if (findByPk(erbean) == null) {
			throw new NoRecordFoundException("EresourceBean : no record found");
		}
		long pk = erbean.getId();

		String query = "UPDATE ST_ERESOURCE SET tables_contains=?,name=?,detail=? WHERE ID=?";
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1, erbean.getTableContains());
			ps.setString(2, erbean.getName());
			ps.setString(3, erbean.getDetail());
			ps.setLong(4, pk);
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("EresourceBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;

	}

	/**
	 * @param erbean
	 * @return
	 * @throws ApplicationException
	 */
	public static EResourceBean findByPk(EResourceBean erbean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long pk = erbean.getId();
		erbean = null;
		String query = "SELECT * FROM ST_ERESOURCE WHERE ID=?";
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setLong(1, pk);
			rs = ps.executeQuery();
			while (rs.next()) {
				erbean = new EResourceBean();
				erbean.setId(rs.getLong(1));
				erbean.setTableContains(rs.getString(2));
				erbean.setName(rs.getString(3));
				erbean.setDetail(rs.getString(4));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("EresourceBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return erbean;

	}

	/**
	 * @param erbean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static ArrayList<EResourceBean> search(EResourceBean erbean, int pageNo, int pageSize)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<EResourceBean> list = new ArrayList<EResourceBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM st_eresource WHERE 1=1");

		if (erbean != null) {
			if (erbean.getId() > 0) {
				query.append(" AND id=" + erbean.getId());
			}
			if (erbean.getName() != null && erbean.getName().trim().length() > 0) {
				query.append(" AND name like '" + erbean.getName() + "%'");
			}
			if (erbean.getTableContains() != null && erbean.getTableContains().trim().length() > 0) {
				query.append(" AND tables_contains like '" + erbean.getTableContains() + "%'");
			}
			if (erbean.getDetail() != null && erbean.getDetail().trim().length() > 0) {
				query.append(" AND detail like '" + erbean.getDetail() + "%'");
			}
		}

		/*----*/
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit " + pageNo + "," + pageSize);
		}

		
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				erbean = new EResourceBean();
				erbean.setId(rs.getLong(1));
				erbean.setTableContains(rs.getString(2));
				erbean.setName(rs.getString(3));
				erbean.setDetail(rs.getString(4));
				list.add(erbean);

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("EresourceBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return list;

	}

}
