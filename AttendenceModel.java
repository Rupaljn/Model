package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

import in.sterling.bean.AttendenceBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.NoRecordFoundException;

public class AttendenceModel extends BaseModel {

	/**
	 * @param abean
	 * @return
	 * @throws ApplicationException
	 */
	public static long add(AttendenceBean abean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = nextNonBusinessPK("st_attendence");

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(
					"insert into st_attendence (id,STUDENT_ID,STUDENT_NAME,BRANCH_NAME,MONTH,YEAR,SUBJECT1,SUBJECT2,SUBJECT3,SUBJECT4,SUBJECT5,SUBJECT6,SUBJECT7,SUBJECT8,SUBJECT9,SUBJECT10,ATTENDENCE1,ATTENDENCE2,ATTENDENCE3,ATTENDENCE4,ATTENDENCE5,ATTENDENCE6,ATTENDENCE7,ATTENDENCE8,ATTENDENCE9,ATTENDENCE10,created_by,modified_by)"
							+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			ps.setLong(1, pk);
			ps.setLong(2, abean.getStudentId());
			ps.setString(3, abean.getStudentName());
			ps.setString(4, abean.getBranchName());
			ps.setString(5, abean.getMonth());
			ps.setInt(6, abean.getYear());
			ps.setString(7, abean.getSubject1());
			ps.setString(8, abean.getSubject2());
			ps.setString(9, abean.getSubject3());
			ps.setString(10, abean.getSubject4());
			ps.setString(11, abean.getSubject5());
			ps.setString(12, abean.getSubject6());
			ps.setString(13, abean.getSubject7());
			ps.setString(14, abean.getSubject8());
			ps.setString(15, abean.getSubject9());
			ps.setString(16, abean.getSubject10());
			ps.setInt(17, abean.getAttendence1());
			ps.setInt(18, abean.getAttendence2());
			ps.setInt(19, abean.getAttendence3());
			ps.setInt(20, abean.getAttendence4());
			ps.setInt(21, abean.getAttendence5());
			ps.setInt(22, abean.getAttendence6());
			ps.setInt(23, abean.getAttendence7());
			ps.setInt(24, abean.getAttendence8());
			ps.setInt(25, abean.getAttendence9());
			ps.setInt(26, abean.getAttendence10());
			ps.setString(27, abean.getCreatedBy());
			ps.setString(28, abean.getModifiedBy());
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("AttendenceBean Exception :" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return 0;
	}

	/**
	 * @param abean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long delete(AttendenceBean abean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = abean.getId();
		if (findByPk(abean) == null) {
			throw new NoRecordFoundException("AttendenceBean Exception : no record found");
		}
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM st_attendence WHERE ID=?");
			ps.setLong(1, pk);
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return pk;
	}

	/**
	 * @param abean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static long update(AttendenceBean abean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		long pk = abean.getId();
		if (findByPk(abean) == null) {
			throw new NoRecordFoundException("AttendenceBean Exception : no record found");
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("UPDATE ST_ATTENDENCE  SET STUDENT_ID=?,"
					+ "STUDENT_NAME=?,BRANCH_NAME=?,MONTH=?,YEAR=?,SUBJECT1=?,"
					+ "SUBJECT2=?,SUBJECT3=?,SUBJECT4=?,SUBJECT5=?,SUBJECT6=?,"
					+ "SUBJECT7=?,SUBJECT8=?,SUBJECT9=?,SUBJECT10=?,ATTENDENCE1=?,"
					+ "ATTENDENCE2=?,ATTENDENCE3=?,ATTENDENCE4=?,ATTENDENCE5=?,"
					+ "ATTENDENCE6=?,ATTENDENCE7=?,ATTENDENCE8=?," + "ATTENDENCE9=?,ATTENDENCE10=?,created_by=?,modified_by=? WHERE ID=?");

			ps.setLong(1, abean.getStudentId());
			ps.setString(2, abean.getStudentName());
			ps.setString(3, abean.getBranchName());
			ps.setString(4, abean.getMonth());
			ps.setInt(5, abean.getYear());
			ps.setString(6, abean.getSubject1());
			ps.setString(7, abean.getSubject2());
			ps.setString(8, abean.getSubject3());
			ps.setString(9, abean.getSubject4());
			ps.setString(10, abean.getSubject5());
			ps.setString(11, abean.getSubject6());
			ps.setString(12, abean.getSubject7());
			ps.setString(13, abean.getSubject8());
			ps.setString(14, abean.getSubject9());
			ps.setString(15, abean.getSubject10());
			ps.setInt(16, abean.getAttendence1());
			ps.setInt(17, abean.getAttendence2());
			ps.setInt(18, abean.getAttendence3());
			ps.setInt(19, abean.getAttendence4());
			ps.setInt(20, abean.getAttendence5());
			ps.setInt(21, abean.getAttendence6());
			ps.setInt(22, abean.getAttendence7());
			ps.setInt(23, abean.getAttendence8());
			ps.setInt(24, abean.getAttendence9());
			ps.setInt(25, abean.getAttendence10());
			ps.setString(26, abean.getCreatedBy());
			ps.setString(27, abean.getModifiedBy());
			ps.setLong(28, pk);
			ps.executeUpdate();
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("AttendenceBean Exception" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return 0;
	}

	/**
	 * @param abean
	 * @return
	 * @throws ApplicationException
	 */
	public static AttendenceBean findByPk(AttendenceBean abean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long pk = abean.getId();
		abean = null;
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("SELECT * FROM st_attendence WHERE ID=?");
			ps.setLong(1, pk);
			rs = ps.executeQuery();
			while (rs.next()) {
				abean = new AttendenceBean();
				abean.setId(rs.getLong(1));
				abean.setStudentId(rs.getLong(2));
				abean.setStudentName(rs.getString(3));
				abean.setBranchName(rs.getString(4));
				abean.setMonth(rs.getString(5));
				abean.setYear(rs.getInt(6));
				abean.setSubject1(rs.getString(7));
				abean.setSubject2(rs.getString(8));
				abean.setSubject3(rs.getString(9));
				abean.setSubject4(rs.getString(10));
				abean.setSubject5(rs.getString(11));
				abean.setSubject6(rs.getString(12));
				abean.setSubject7(rs.getString(13));
				abean.setSubject8(rs.getString(14));
				abean.setSubject9(rs.getString(15));
				abean.setSubject10(rs.getString(16));
				abean.setAttendence1(rs.getInt(17));
				abean.setAttendence2(rs.getInt(18));
				abean.setAttendence3(rs.getInt(19));
				abean.setAttendence4(rs.getInt(20));
				abean.setAttendence5(rs.getInt(21));
				abean.setAttendence6(rs.getInt(22));
				abean.setAttendence7(rs.getInt(23));
				abean.setAttendence8(rs.getInt(24));
				abean.setAttendence9(rs.getInt(25));
				abean.setAttendence10(rs.getInt(26));

			}
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("AttendenceBean Exception" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return abean;
	}

	/**
	 * @param abean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public static ArrayList<AttendenceBean> search(AttendenceBean abean, int pageNo, int pageSize)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<AttendenceBean> list = new ArrayList<AttendenceBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM st_attendence WHERE 1=1 ");
		if (abean != null) {
			if (abean.getId() > 0) {
				query.append(" AND ID = " + abean.getId());
			}
			if (abean.getStudentId() > 0) {
				query.append(" AND STUDENT_ID =" + abean.getStudentId());
			}
			if (abean.getStudentName() != null && abean.getStudentName().length() > 0) {
				query.append(" AND STUDENT_NAME LIKE '" + abean.getStudentName() + "%'");
			}
		}

		if (pageSize > 0) {

			pageNo = (pageNo - 1) * (pageSize-1);
			query.append(" Limit " + pageNo + ", " + pageSize);

		}
	

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());

			rs = ps.executeQuery();
			while (rs.next()) {
				abean = new AttendenceBean();
				abean.setId(rs.getLong(1));
				abean.setStudentId(rs.getLong(2));
				abean.setStudentName(rs.getString(3));
				abean.setBranchName(rs.getString(4));
				abean.setMonth(rs.getString(5));
				abean.setYear(rs.getInt(6));
				abean.setSubject1(rs.getString(7));
				abean.setSubject2(rs.getString(8));
				abean.setSubject3(rs.getString(9));
				abean.setSubject4(rs.getString(10));
				abean.setSubject5(rs.getString(11));
				abean.setSubject6(rs.getString(12));
				abean.setSubject7(rs.getString(13));
				abean.setSubject8(rs.getString(14));
				abean.setSubject9(rs.getString(15));
				abean.setSubject10(rs.getString(16));
				abean.setAttendence1(rs.getInt(17));
				abean.setAttendence2(rs.getInt(18));
				abean.setAttendence3(rs.getInt(19));
				abean.setAttendence4(rs.getInt(20));
				abean.setAttendence5(rs.getInt(21));
				abean.setAttendence6(rs.getInt(22));
				abean.setAttendence7(rs.getInt(23));
				abean.setAttendence8(rs.getInt(24));
				abean.setAttendence9(rs.getInt(25));
				abean.setAttendence10(rs.getInt(26));
				list.add(abean);

			}
			con.commit();
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("AttendenceBean Exception" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return list;
	}

}
