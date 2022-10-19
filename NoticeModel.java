package in.sterling.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.NoticeBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class NoticeModel extends BaseModel {

	/**
	 * @param nbean
	 * @return
	 * @throws ApplicationException
	 */
	public static long add(NoticeBean nbean) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;

		Date createdOn = new java.sql.Date(nbean.getCreateOn().getTime());
		Date expireDate = new java.sql.Date(nbean.getExpireDate().getTime());

		long pk = nextNonBusinessPK("st_notice");
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"INSERT INTO st_notice  (ID,SUBJECT,DETAILS,CREATED_ON,EXPIRE_DATE) VALUES(?,?,?,?,?) ");
			ps.setLong(1, pk);
			ps.setString(2, nbean.getSubject());
			ps.setString(3, nbean.getDetails());
			ps.setDate(4, createdOn);
			ps.setDate(5, expireDate);

			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("NoteiceBean" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param nbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long delete(NoticeBean nbean) throws ApplicationException, NoRecordFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		long pk = nbean.getId();
		if (findByPk(nbean) == null) {
			throw new NoRecordFoundException("NoticeBean Exception : no record found");
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_NOTICE WHERE ID=?");
			ps.setLong(1, pk);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("NoteiceBean" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param nbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long update(NoticeBean nbean) throws ApplicationException, NoRecordFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		long pk = nbean.getId();
		if (findByPk(nbean) == null) {
			throw new NoRecordFoundException("NoticeBean Exception : no record found");
		}

		Date createdOn = new java.sql.Date(nbean.getCreateOn().getTime());
		Date expireDate = new java.sql.Date(nbean.getExpireDate().getTime());

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("UPDATE ST_NOTICE SET SUBJECT=?,DETAILS=?,created_on=?,EXPIRE_DATE=? WHERE ID=?");

			ps.setString(1, nbean.getSubject());
			ps.setString(2, nbean.getDetails());
			ps.setDate(3, createdOn);
			ps.setDate(4, expireDate);
			ps.setLong(5, pk);

			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("NoteiceBean" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param nbean
	 * @return
	 * @throws ApplicationException
	 */
	public static NoticeBean findByPk(NoticeBean nbean) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long pk = nbean.getId();
		nbean = null;
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("SELECT * FROM st_notice WHERE ID=?");
			ps.setLong(1, pk);
			rs = ps.executeQuery();
			while (rs.next()) {
				nbean = new NoticeBean();
				nbean.setId(rs.getLong(1));
				nbean.setSubject(rs.getString(2));
				nbean.setDetails(rs.getString(3));
				nbean.setCreateOn(rs.getDate(4));
				nbean.setExpireDate(rs.getDate(5));
			}

			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("NoteiceBean" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return nbean;
	}

	/**
	 * @param nbean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public static ArrayList<NoticeBean> search(NoticeBean nbean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<NoticeBean> list = new ArrayList<NoticeBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM st_notice WHERE 1=1");
		if (nbean != null) {
			if (nbean.getId() > 0) {
				query.append(" and id=" + nbean.getId());
			}
			if (nbean.getSubject() != null && nbean.getSubject().trim().length() > 0) {
				query.append(" and subject like '" + nbean.getSubject() + "%'");
			}
			if (nbean.getCreateOn() != null) {
				query.append(" and created_on =" + nbean.getCreateOn());
			}
			if (nbean.getExpireDate() != null) {
				query.append(" and expire_date =" + nbean.getExpireDate());
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
				nbean = new NoticeBean();
				nbean.setId(rs.getLong(1));
				nbean.setSubject(rs.getString(2));
				nbean.setDetails(rs.getString(3));
				nbean.setCreateOn(rs.getDate(4));
				nbean.setExpireDate(rs.getDate(5));
				list.add(nbean);
			}

			con.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException("NoteiceBean" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}
}
