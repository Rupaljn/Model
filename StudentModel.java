package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import in.sterling.bean.StudentBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.DuplicateRecordException;
import in.sterling.exception.NoRecordFoundException;

public class StudentModel extends BaseModel {

	/**
	 * @param sbean
	 */
	public static long addStudent(StudentBean sbean) {

		if (StudentModel.findByPk(sbean) != null) {
			throw new DuplicateRecordException("Exception : Id(pk) already exitst");
		}
		if (StudentModel.findByUserId(sbean) != null) {
			throw new DuplicateRecordException("Exception : UserId already exitst");
		}

		Connection con = null;
		PreparedStatement ps = null;
		long UserId = sbean.getUserId();
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("INSERT INTO ST_STUDENT (ID,FIRST_NAME,LAST_NAME,"
					+ "FATHER_NAME,MOTHER_NAME,COLLEGE_ID,DEPARTEMENT,"
					+ "SEMESTER,YEAR,DATE_OF_BIRTH,GENDER,MOBILE_NO,ADDRESS,USER_ID) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			ps.setLong(1, nextNonBusinessPK("st_student"));
			ps.setString(2, sbean.getFirstName());
			ps.setString(3, sbean.getLastName());
			ps.setString(4, sbean.getFatherName());
			ps.setString(5, sbean.getMotherName());
			ps.setLong(6, sbean.getCollegeId());
			ps.setString(7, sbean.getDepartement());
			ps.setInt(8, sbean.getSemester());
			ps.setInt(9, sbean.getYear());
			ps.setDate(10, sbean.getDateOfBirth());
			ps.setString(11, sbean.getGender());
			ps.setString(12, sbean.getMobileNo());
			ps.setString(13, sbean.getAddress());
			ps.setLong(14, sbean.getUserId());

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		return UserId;
	}

	/**
	 * 
	 * 
	 * @param sbean
	 * @return
	 */

	public static long delete(StudentBean sbean) {

		if (StudentModel.findByPk(sbean) == null) {
			throw new NoRecordFoundException("Exception : no record found");
		}

		long id = sbean.getId();
		Connection con = null;
		PreparedStatement ps = null;
		int rowCount = 0;
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_STUDENT WHERE ID=?");
			ps.setLong(1, id);
			rowCount = ps.executeUpdate();
			con.commit();
			ps.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in delete Student" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		if (rowCount > 0) {
			return id;
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * 
	 * @param sbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static StudentBean findByPk(StudentBean sbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long id = sbean.getId();
		sbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_STUDENT WHERE ID=?");
			ps.setLong(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				sbean = new StudentBean();
				sbean.setId(rs.getLong(1));
				sbean.setFirstName(rs.getString(2));
				sbean.setLastName(rs.getString(3));
				sbean.setFatherName(rs.getString(4));
				sbean.setMotherName(rs.getString(5));
				sbean.setCollegeId(rs.getLong(6));
				sbean.setDepartement(rs.getString(7));
				sbean.setSemester(rs.getInt(8));
				sbean.setYear(rs.getInt(9));
				sbean.setDateOfBirth(rs.getDate(10));
				sbean.setGender(rs.getString(11));
				sbean.setMobileNo(rs.getString(12));
				sbean.setAddress(rs.getString(13));
				sbean.setUserId(rs.getLong(14));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return sbean;

	}

	public static StudentBean findByUserId(StudentBean sbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long userId = sbean.getUserId();
		sbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_STUDENT WHERE USER_ID=?");
			ps.setLong(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				sbean = new StudentBean();
				sbean.setId(rs.getLong(1));
				sbean.setFirstName(rs.getString(2));
				sbean.setLastName(rs.getString(3));
				sbean.setFatherName(rs.getString(4));
				sbean.setMotherName(rs.getString(5));
				sbean.setCollegeId(rs.getLong(6));
				sbean.setDepartement(rs.getString(7));
				sbean.setSemester(rs.getInt(8));
				sbean.setYear(rs.getInt(9));
				sbean.setDateOfBirth(rs.getDate(10));
				sbean.setGender(rs.getString(11));
				sbean.setMobileNo(rs.getString(12));
				sbean.setAddress(rs.getString(13));
				sbean.setUserId(rs.getLong(14));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return sbean;

	}

	/**
	 * 
	 * 
	 * @param sbean
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static ArrayList<StudentBean> search(StudentBean sbean, int pageNo, int pageSize)
			throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<StudentBean> list = new ArrayList<StudentBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_STUDENT WHERE 1=1");

		/* logic for dynamic search or for creating query */
		if (sbean != null) {
			if (sbean.getId() > 0) {
				query.append(" AND id =" + sbean.getId());
			}
			if (sbean.getFirstName() != null && sbean.getFirstName().trim().length() > 0) {
				query.append(" AND first_name like '" + sbean.getFirstName() + "%'");
			}
			if (sbean.getLastName() != null && sbean.getLastName().trim().length() > 0) {
				query.append(" AND last_name like '" + sbean.getLastName() + "%'");
			}
			if (sbean.getFatherName() != null && sbean.getFatherName().trim().length() > 0) {
				query.append(" AND father_name like '" + sbean.getFatherName() + "%'");
			}
			if (sbean.getMotherName() != null && sbean.getMotherName().trim().length() > 0) {
				query.append(" AND mother_name like '" + sbean.getMotherName() + "%'");
			}
			if (sbean.getMobileNo() != null && sbean.getMobileNo().trim().length() > 0) {
				query.append(" AND mobile_no like '" + sbean.getMobileNo() + "%'");
			}
			if (sbean.getGender() != null && sbean.getGender().trim().length() > 0) {
				query.append(" AND gender like '" + sbean.getGender() + "%'");
			}
			if (sbean.getSemester() > 0) {
				query.append(" AND semester = " + sbean.getSemester());
			}
			if (sbean.getYear() > 0) {
				query.append(" AND year = " + sbean.getYear());
			}
			if (sbean.getDepartement() != null && sbean.getDepartement().trim().length() > 0) {
				query.append(" AND departement like '" + sbean.getDepartement() + "%'");
			}
			if (sbean.getCollegeId() > 0) {
				query.append(" AND college_id = " + sbean.getCollegeId());
			}
			if (sbean.getUserId() > 0) {
				query.append(" AND user_id = " + sbean.getUserId());
			}

		}
		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("test =" + query);
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				sbean = new StudentBean();
				sbean.setId(rs.getLong(1));
				sbean.setFirstName(rs.getString(2));
				sbean.setLastName(rs.getString(3));
				sbean.setFatherName(rs.getString(4));
				sbean.setMotherName(rs.getString(5));
				sbean.setCollegeId(rs.getLong(6));
				sbean.setDepartement(rs.getString(7));
				sbean.setSemester(rs.getInt(8));
				sbean.setYear(rs.getInt(9));
				sbean.setDateOfBirth(rs.getDate(10));
				sbean.setGender(rs.getString(11));
				sbean.setMobileNo(rs.getString(12));
				sbean.setAddress(rs.getString(13));
				sbean.setUserId(rs.getLong(14));
				list.add(sbean);

			}

			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception from Studentmodel");
		}
		return list;

	}

	/**
	 * @param sbean
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public static long update(StudentBean sbean) throws NoRecordFoundException, ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		String query = "UPDATE ST_STUDENT SET FIRST_NAME=?,LAST_NAME=?,FATHER_NAME=?,MOTHER_NAME=?,"
				+ "COLLEGE_ID=?,DEPARTEMENT=?,SEMESTER=?,YEAR=?,date_of_birth=?,GENDER=?,MOBILE_NO=?,ADDRESS=? WHERE ID=?";
System.out.println(query);
		long pk = sbean.getId();

		if (StudentModel.findByPk(sbean) == null) {
			throw new NoRecordFoundException("Exception : No record found");
		}
		if (StudentModel.findByUserId(sbean) == null) {
			throw new NoRecordFoundException("Exception : No record found");
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1, sbean.getFirstName());
			ps.setString(2, sbean.getLastName());
			ps.setString(3, sbean.getFatherName());
			ps.setString(4, sbean.getMotherName());
			ps.setLong(5, sbean.getCollegeId());
			ps.setString(6, sbean.getDepartement());
			ps.setInt(7, sbean.getSemester());
			ps.setInt(8, sbean.getYear());
			ps.setDate(9, sbean.getDateOfBirth());
			ps.setString(10, sbean.getGender());
			ps.setString(11, sbean.getMobileNo());
			ps.setString(12, sbean.getAddress());
			ps.setLong(13, pk);
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

}
