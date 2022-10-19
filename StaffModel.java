package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import in.sterling.bean.StaffBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.DuplicateRecordException;
import in.sterling.exception.NoRecordFoundException;

public class StaffModel extends BaseModel {

	/**
	 * @param stfbean
	 */
	public static long add(StaffBean stfbean) {

		if (StaffModel.findByPk(stfbean) != null) {
			throw new DuplicateRecordException("Exception : Id(pk) already exitst");
		}
		if (StaffModel.findByUserId(stfbean) != null) {
			throw new DuplicateRecordException("Exception : UserId already exitst");
		}

		Connection con = null;
		PreparedStatement ps = null;
		long UserId = stfbean.getUserId();
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("INSERT INTO ST_STAFF (ID,FIRST_NAME,LAST_NAME,"
					+ "FATHER_NAME,MOTHER_NAME,COLLEGE_ID,DEPARTEMENT,"
					+ "SEMESTER,YEAR,DATE_OF_BIRTH,GENDER,MOBILE_NO,ADDRESS,USER_ID) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			ps.setLong(1, nextNonBusinessPK("ST_STAFF"));
			ps.setString(2, stfbean.getFirstName());
			ps.setString(3, stfbean.getLastName());
			ps.setString(4, stfbean.getFatherName());
			ps.setString(5, stfbean.getMotherName());
			ps.setLong(6, stfbean.getCollegeId());
			ps.setString(7, stfbean.getDepartement());
			ps.setInt(8, stfbean.getSemester());
			ps.setInt(9, stfbean.getYear());
			ps.setDate(10, stfbean.getDateOfBirth());
			ps.setString(11, stfbean.getGender());
			ps.setString(12, stfbean.getMobileNo());
			ps.setString(13, stfbean.getAddress());
			ps.setLong(14, stfbean.getUserId());

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
	 * @param stfbean
	 * @return
	 */

	public static long delete(StaffBean stfbean) {

		if (StaffModel.findByPk(stfbean) == null) {
			throw new NoRecordFoundException("Exception : no record found");
		}

		long id = stfbean.getId();
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_STAFF WHERE ID=?");
			ps.setLong(1, id);
			ps.executeUpdate();
			con.commit();
			ps.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in delete Student" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		
			return id;
		
	}

	/**
	 * 
	 * 
	 * @param stfbean
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static StaffBean findByPk(StaffBean stfbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long id = stfbean.getId();
		stfbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_STAFF WHERE ID=?");
			ps.setLong(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				stfbean = new StaffBean();
				stfbean.setId(rs.getLong(1));
				stfbean.setFirstName(rs.getString(2));
				stfbean.setLastName(rs.getString(3));
				stfbean.setFatherName(rs.getString(4));
				stfbean.setMotherName(rs.getString(5));
				stfbean.setCollegeId(rs.getLong(6));
				stfbean.setDepartement(rs.getString(7));
				stfbean.setSemester(rs.getInt(8));
				stfbean.setYear(rs.getInt(9));
				stfbean.setDateOfBirth(rs.getDate(10));
				stfbean.setGender(rs.getString(11));
				stfbean.setMobileNo(rs.getString(12));
				stfbean.setAddress(rs.getString(13));
				stfbean.setUserId(rs.getLong(14));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return stfbean;

	}

	public static StaffBean findByUserId(StaffBean stfbean) throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long userId = stfbean.getUserId();
		stfbean = null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_STAFF WHERE USER_ID=?");
			ps.setLong(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				stfbean = new StaffBean();
				stfbean.setId(rs.getLong(1));
				stfbean.setFirstName(rs.getString(2));
				stfbean.setLastName(rs.getString(3));
				stfbean.setFatherName(rs.getString(4));
				stfbean.setMotherName(rs.getString(5));
				stfbean.setCollegeId(rs.getLong(6));
				stfbean.setDepartement(rs.getString(7));
				stfbean.setSemester(rs.getInt(8));
				stfbean.setYear(rs.getInt(9));
				stfbean.setDateOfBirth(rs.getDate(10));
				stfbean.setGender(rs.getString(11));
				stfbean.setMobileNo(rs.getString(12));
				stfbean.setAddress(rs.getString(13));
				stfbean.setUserId(rs.getLong(14));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return stfbean;

	}

	/**
	 * 
	 * 
	 * @param stfbean
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 * @throws NoRecordFoundException
	 */
	public static ArrayList<StaffBean> search(StaffBean stfbean, int pageNo, int pageSize)
			throws ApplicationException, NoRecordFoundException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<StaffBean> list = new ArrayList<StaffBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_STAFF WHERE 1=1");

		/* logic for dynamic search or for creating query */
		if (stfbean != null) {
			if (stfbean.getId() > 0) {
				query.append(" AND id =" + stfbean.getId());
			}
			if (stfbean.getFirstName() != null && stfbean.getFirstName().trim().length() > 0) {
				query.append(" AND first_name like '" + stfbean.getFirstName() + "%'");
			}
			if (stfbean.getLastName() != null && stfbean.getLastName().trim().length() > 0) {
				query.append(" AND last_name like '" + stfbean.getLastName() + "%'");
			}
			if (stfbean.getFatherName() != null && stfbean.getFatherName().trim().length() > 0) {
				query.append(" AND father_name like '" + stfbean.getFatherName() + "%'");
			}
			if (stfbean.getMotherName() != null && stfbean.getMotherName().trim().length() > 0) {
				query.append(" AND mother_name like '" + stfbean.getMotherName() + "%'");
			}
			if (stfbean.getMobileNo() != null && stfbean.getMobileNo().trim().length() > 0) {
				query.append(" AND mobile_no like '" + stfbean.getMobileNo() + "%'");
			}
			if (stfbean.getGender() != null && stfbean.getGender().trim().length() > 0) {
				query.append(" AND gender like '" + stfbean.getGender() + "%'");
			}
			if (stfbean.getSemester() > 0) {
				query.append(" AND semester = " + stfbean.getSemester());
			}
			if (stfbean.getYear() > 0) {
				query.append(" AND year = " + stfbean.getYear());
			}
			if (stfbean.getDepartement() != null && stfbean.getDepartement().trim().length() > 0) {
				query.append(" AND departement like '" + stfbean.getDepartement() + "%'");
			}
			if (stfbean.getCollegeId() > 0) {
				query.append(" AND college_id = " + stfbean.getCollegeId());
			}
			if (stfbean.getUserId() > 0) {
				query.append(" AND user_id = " + stfbean.getUserId());
			}

		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append(" limit " + pageNo + "," + pageSize);
		}
	System.out.println(query);
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				stfbean = new StaffBean();
				stfbean.setId(rs.getLong(1));
				stfbean.setFirstName(rs.getString(2));
				stfbean.setLastName(rs.getString(3));
				stfbean.setFatherName(rs.getString(4));
				stfbean.setMotherName(rs.getString(5));
				stfbean.setCollegeId(rs.getLong(6));
				stfbean.setDepartement(rs.getString(7));
				stfbean.setSemester(rs.getInt(8));
				stfbean.setYear(rs.getInt(9));
				stfbean.setDateOfBirth(rs.getDate(10));
				stfbean.setGender(rs.getString(11));
				stfbean.setMobileNo(rs.getString(12));
				stfbean.setAddress(rs.getString(13));
				stfbean.setUserId(rs.getLong(14));
				list.add(stfbean);

			}

			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception from StaffModel");
		}
		return list;

	}

	/**
	 * @param stfbean
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public static long update(StaffBean stfbean) throws NoRecordFoundException, ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		String query = "UPDATE ST_STAFF SET FIRST_NAME=?,LAST_NAME=?,FATHER_NAME=?,MOTHER_NAME=?,"
				+ "COLLEGE_ID=?,DEPARTEMENT=?,SEMESTER=?,YEAR=?,date_of_birth=?,GENDER=?,MOBILE_NO=?,ADDRESS=? WHERE ID=?";

		long pk = stfbean.getId();

		if (StaffModel.findByPk(stfbean) == null) {
			throw new NoRecordFoundException("Exception : No record found");
		}
		if (StaffModel.findByUserId(stfbean) == null) {
			throw new NoRecordFoundException("Exception : No record found");
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1, stfbean.getFirstName());
			ps.setString(2, stfbean.getLastName());
			ps.setString(3, stfbean.getFatherName());
			ps.setString(4, stfbean.getMotherName());
			ps.setLong(5, stfbean.getCollegeId());
			ps.setString(6, stfbean.getDepartement());
			ps.setInt(7, stfbean.getSemester());
			ps.setInt(8, stfbean.getYear());
			ps.setDate(9, stfbean.getDateOfBirth());
			ps.setString(10, stfbean.getGender());
			ps.setString(11, stfbean.getMobileNo());
			ps.setString(12, stfbean.getAddress());
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
