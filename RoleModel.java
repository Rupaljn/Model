package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.RoleBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.DuplicateRecordException;
import in.sterling.exception.NoRecordFoundException;
import in.sterling.util.DataUtility;

/**
 * 
 * 
 * 
 * 
 * */

public class RoleModel extends BaseModel {
	/**
	 * 
	 * findByRoleName method is used to find role name
	 * 
	 * @param name
	 * @return
	 * 
	 * 
	 */

	public static RoleBean findByRoleName(RoleBean rbean) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = rbean.getName();
		rbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("select * from st_role where name=?");
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				rbean = new RoleBean();
				rbean.setId(rs.getLong(1));
				rbean.setName(rs.getString(2));
				rbean.setDescription(rs.getString(3));
				rbean.setCreatedBy(rs.getString(4));
				rbean.setModifiedBy(rs.getString(5));
				rbean.setCreatedDateTime(rs.getTimestamp(6));
				rbean.setModifiedDateTime(rs.getTimestamp(7));

			}
			ps.close();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return rbean;

	}

	/**
	 * @param rbean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public static long add(RoleBean rbean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement ps = null;
		long pk = nextNonBusinessPK("st_role");

		if (RoleModel.findByRoleName(rbean) != null) {
			throw new DuplicateRecordException("RoleBean Exception : Record already exitst");
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"INSERT INTO st_role (id,NAME,description,created_by,modified_by) VALUES (?,?,?,?,?)");
			ps.setLong(1, pk);
			ps.setString(2, rbean.getName());
			ps.setString(3, rbean.getDescription());
			ps.setString(4, rbean.getCreatedBy());
			ps.setString(5, rbean.getModifiedBy());
			ps.executeUpdate();
			con.setAutoCommit(true);

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("RoleBean Exception : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;

	}

	public static long delete(RoleBean rbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = rbean.getId();

		if (findByPK(rbean) == null) {
			throw new NoRecordFoundException("RoleBean Exception : NoRecordFoundException ");
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM st_role WHERE id =?");
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("RoleBean Exception : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * 
	 * update method is used to update role of selected user
	 * 
	 * @param bean
	 * 
	 * @return
	 * 
	 * 
	 */

	public static long update(RoleBean rbean) throws ApplicationException, NoRecordFoundException {

		if (RoleModel.findByPK(rbean) == null) {
			throw new NoRecordFoundException("Exception : no record found");
		}

		Connection con = null;
		PreparedStatement ps = null;
		String name = rbean.getName();
		String description = rbean.getDescription();
		long pk = rbean.getId();

		try {
			con = JDBCDataSource.getConnection();

			con.setAutoCommit(false);
			ps = con.prepareStatement("UPDATE ST_ROLE SET NAME=?,DESCRIPTION=? WHERE ID=?");
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setLong(3, pk);

			ps.executeUpdate();

			con.commit();
			ps.close();

		} catch (Exception e) {

			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("RoleBean Exception :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * 
	 * getList method used to display role on Rolelist view page
	 * 
	 * @param page
	 * @param pageSize
	 * 
	 * @return
	 * 
	 */
	public static ArrayList<RoleBean> getList(RoleBean rbean, int pageNo, int pageSize) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<RoleBean> list = new ArrayList<RoleBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_ROLE WHERE 1=1");

		if (rbean.getId() > 0) {
			query.append(" AND id = " + rbean.getId());
		}
		if (rbean.getName() != null && rbean.getName().trim().length() > 0) {
			query.append(" AND NAME like '" + rbean.getName() + "%'");
		}
		if (rbean.getDescription() != null && rbean.getDescription().trim().length() > 0) {
			query.append(" AND DESCRIPTION like '" + rbean.getDescription() + "%'");
		}

		if (pageSize > 0) {

			pageNo = (pageNo - 1) * pageSize;
			query.append(" Limit " + pageNo + ", " + pageSize);

		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rbean = new RoleBean();
				rbean.setId(rs.getLong(1));
				rbean.setName(rs.getString(2));
				rbean.setDescription(rs.getString(3));
				rbean.setCreatedBy(rs.getString(4));
				rbean.setModifiedBy(rs.getString(5));
				rbean.setCreatedDateTime(rs.getTimestamp(6));
				rbean.setModifiedDateTime(rs.getTimestamp(7));
				list.add(rbean);

			}
			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("RoleBean Exception : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

	/**
	 * 
	 * search method is used to search role
	 * 
	 * @param bean
	 * 
	 * @return RoleBean
	 * @throws ApplicationException
	 */
	public static ArrayList<RoleBean> search(RoleBean rbean, int pageNo, int pageSize) throws ApplicationException {

		ArrayList<RoleBean> list = new ArrayList<RoleBean>();
		Connection con = null;
		PreparedStatement ps = null;
		StringBuffer query = new StringBuffer("select id,name,description from st_role where 1=1 ");

		if (rbean.getId() > 0) {
			query.append(" AND id=" + rbean.getId());
		}

		if (rbean.getName() != null && rbean.getName().trim().length() > 0) {
			query.append(" AND name like'" + rbean.getName() + "%'");
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("Rolebean :" + query);
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rbean = new RoleBean();
				rbean.setId(rs.getLong(1));
				rbean.setName(rs.getString(2));
				rbean.setDescription(rs.getString(3));
				list.add(rbean);
			}
			con.commit();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("RoleBean Exception :" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

	public static ArrayList<RoleBean> list() throws ApplicationException {

		ArrayList<RoleBean> list = new ArrayList<RoleBean>();
		Connection con = null;
		PreparedStatement ps = null;
		StringBuffer query = new StringBuffer("select id,name,description from st_role where 1=1 ");

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RoleBean rbean = new RoleBean();
				rbean.setId(rs.getLong("id"));
				rbean.setName(rs.getString("name"));
				rbean.setDescription(rs.getString("description"));
				list.add(rbean);
			}
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("RoleBean Exception :" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

	/**
	 * 
	 * findByPK method used to find role using non business primary key
	 * 
	 * @param pk
	 * 
	 * @return ArrayList
	 * @throws ApplicationException
	 * 
	 */

	public static RoleBean findByPK(RoleBean rbean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = rbean.getId();
		rbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("select * from st_role where id=?");
			ps.setLong(1, pk);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				rbean = new RoleBean();
				rbean.setId(rs.getLong(1));
				rbean.setName(rs.getString(2));
				rbean.setDescription(rs.getString(3));
				rbean.setCreatedBy(rs.getString(4));
				rbean.setModifiedBy(rs.getString(5));
				rbean.setCreatedDateTime(rs.getTimestamp(6));
				rbean.setModifiedDateTime(rs.getTimestamp(7));
			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("RoleBean Exception :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return rbean;
	}

}
