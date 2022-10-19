package in.sterling.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import in.sterling.bean.TimeTableBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class TimeTableModel extends BaseModel {

	public static long add(TimeTableBean ttbean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = nextNonBusinessPK("st_timetable");
		Date date = null;
		if (ttbean != null && ttbean.getDate()!=null) {
			date = new Date(ttbean.getDate().getTime());
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"INSERT INTO ST_TIMETABLE(ID,BRANCH,SEMESTER,YEAR,SUBJECT,DATE,TIME,FACULTY_ID) VALUES(?,?,?,?,?,?,?,?)");
			ps.setLong(1, pk);
			ps.setString(2, ttbean.getBranch());
			ps.setInt(3, ttbean.getSemester());
			ps.setInt(4, ttbean.getYear());
			ps.setString(5, ttbean.getSubject());
			ps.setDate(6, date);
			ps.setString(7, ttbean.getTime());
			ps.setLong(8, ttbean.getFacultyId());
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("TimeTableBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	public static long delete(TimeTableBean ttbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = ttbean.getId();
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_TIMETABLE WHERE ID=?");
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("TimeTableBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param ttbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long update(TimeTableBean ttbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = ttbean.getId();
		Date date = new Date(ttbean.getDate().getTime());
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"UPDDATE ST_TIMETABLE SET BRANCH=?,SEMESTER=?,YEAR=?,SUBJECT=?,DATE=?,TIME=?,FACULTY_ID=? WHERE ID=?");
			ps.setString(1, ttbean.getBranch());
			ps.setInt(2, ttbean.getSemester());
			ps.setInt(3, ttbean.getYear());
			ps.setString(4, ttbean.getSubject());
			ps.setDate(5, date);
			ps.setString(6, ttbean.getTime());
			ps.setLong(7, ttbean.getFacultyId());
			ps.setLong(8, pk);

			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("TimeTableBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param ttbean
	 * @return
	 * @throws ApplicationException
	 */
	public static TimeTableBean findByPk(TimeTableBean ttbean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		long pk = ttbean.getId();
		ttbean = null;
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("SELECT * FROM ST_TIMETABLE WHERE ID=?");
			ps.setLong(1, pk);
			rs = ps.executeQuery();
			while (rs.next()) {
				ttbean = new TimeTableBean();
				ttbean.setId(rs.getLong(1));
				ttbean.setBranch(rs.getString(2));
				ttbean.setSemester(rs.getInt(3));
				ttbean.setYear(rs.getInt(4));
				ttbean.setSubject(rs.getString(5));
				ttbean.setDate(rs.getDate(6));
				ttbean.setTime(rs.getString(7));
				ttbean.setFacultyId(rs.getLong(8));

			}

			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("TimeTableBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return ttbean;
	}

	public static ArrayList<TimeTableBean> search(TimeTableBean ttbean, int pageNo, int pageSize)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<TimeTableBean> list = new ArrayList<TimeTableBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE 1=1");

		if (ttbean != null) {
			if (ttbean.getId() > 0) {
				query.append(" and id =" + ttbean.getId());
			}
			if (ttbean.getBranch() != null && ttbean.getBranch().trim().length() > 0) {
				query.append(" and branch like '" + ttbean.getBranch() + "%'");
			}
			if (ttbean.getSemester() > 0) {
				query.append(" and semester =" + ttbean.getSemester());
			}
			if (ttbean.getYear() > 0) {
				query.append(" and year =" + ttbean.getYear());
			}
			if (ttbean.getSubject() != null && ttbean.getSubject().trim().length() > 0) {
				query.append(" and subject like '" + ttbean.getSubject() + "%'");
			}
			if (ttbean.getDate() != null) {
				query.append(" and date = " + ttbean.getDate());
			}
			if (ttbean.getTime() != null) {
				query.append(" and time =" + ttbean.getTime());
			}
			if (ttbean.getFacultyId() > 0) {
				query.append(" and faculty_id =" + ttbean.getFacultyId());
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
				ttbean = new TimeTableBean();
				ttbean.setId(rs.getLong(1));
				ttbean.setBranch(rs.getString(2));
				ttbean.setSemester(rs.getInt(3));
				ttbean.setYear(rs.getInt(4));
				ttbean.setSubject(rs.getString(5));
				ttbean.setDate(rs.getDate(6));
				ttbean.setTime(rs.getString(7));
				ttbean.setFacultyId(rs.getLong(8));
				list.add(ttbean);
			}

			con.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("TimeTableBean :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

}
