package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.CommentBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class CommentModel extends BaseModel {

	/**
	 * @param cbean
	 * @return
	 */
	public static long add(CommentBean cbean)throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"INSERT INTO st_comment (id,resourceid,text,user_id,resourcename) values(?,?,?,?,?)");
			ps.setLong(1, nextNonBusinessPK("st_comment"));
			ps.setLong(2, cbean.getResourceId());
			ps.setString(3, cbean.getText());
			ps.setLong(4, cbean.getUserId());
			ps.setString(5, cbean.getResourceName());
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (SQLException e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("CommentBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return 0;
	}

	/**
	 * @param cbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long delete(CommentBean cbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = cbean.getId();

		if (findByPk(cbean) == null) {
			throw new NoRecordFoundException("CommentBean Exception : no record found");
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM st_comment WHERE ID=?");
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("CommentBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;
	}

	/**
	 * @param cbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long update(CommentBean cbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = cbean.getId();
		if (findByPk(cbean) == null) {
			throw new NoRecordFoundException("CommentBean Exception : no record found");
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("UPDATE st_comment SET RESOURCEID=?,TEXT=?,RESOURCENAME=? WHERE ID=?");
			ps.setLong(1, cbean.getResourceId());
			ps.setString(2, cbean.getText());
			ps.setString(3, cbean.getResourceName());
			ps.setLong(4, pk);
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("CommentBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return pk;

	}

	/**
	 * @param cbean
	 * @return
	 * @throws ApplicationException
	 */
	public static CommentBean findByPk(CommentBean cbean) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long pk = cbean.getId();
		cbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM st_comment WHERE ID=?");
			ps.setLong(1, pk);
			rs = ps.executeQuery();

			while (rs.next()) {
				cbean = new CommentBean();
				cbean.setId(rs.getLong(1));
				cbean.setResourceId(rs.getLong(2));
				cbean.setText(rs.getString(3));
				cbean.setUserId(rs.getLong(5));
				cbean.setResourceName(rs.getString(6));
			}

			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("CommentBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return cbean;
	}

	/**
	 * 
	 * getCommentList method is used show list of comment
	 * 
	 * @param bean
	 * @param page
	 * @param pageSize
	 * 
	 */

	public static ArrayList<CommentBean> search(CommentBean cbean, int pageNo, int pageSize) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<CommentBean> list = new ArrayList<CommentBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM st_comment WHERE 1=1 ");
		if (cbean != null) {
			if (cbean.getId() > 0) {
				query.append(" and id =" + cbean.getId());
			}
			if (cbean.getResourceId() > 0) {
				query.append(" and resourceid =" + cbean.getResourceId());
			}
			if (cbean.getResourceName() != null && cbean.getResourceName().trim().length() > 0) {
				query.append(" and resourcename like'" + cbean.getResourceName() + "%'");

			}
			if (cbean.getUserId() > 0) {
				query.append(" and user_id =" + cbean.getUserId());
			}
		}
		if (pageSize > 0) {

			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit " + pageNo + "," + pageSize);
		}
	

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
		
			rs = ps.executeQuery();
			while (rs.next()) {
				cbean = new CommentBean();
				cbean.setId(rs.getLong(1));
				cbean.setResourceId(rs.getLong(2));
				cbean.setText(rs.getString(3));
				cbean.setUserId(rs.getLong(5));
				cbean.setResourceName(rs.getString(6));
				list.add(cbean);
			}
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("CommentBean : " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

}
