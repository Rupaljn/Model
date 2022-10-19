package in.sterling.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;

import in.sterling.bean.StaffBean;
import in.sterling.bean.StudentBean;
import in.sterling.bean.UserBean;
import in.sterling.datasource.JDBCDataSource;
import in.sterling.exception.ApplicationException;
import in.sterling.exception.DuplicateRecordException;
import in.sterling.exception.NoRecordFoundException;

/**
 * @author hp
 *
 */
public class UserModel extends BaseModel {

	/**
	 * @param ubean
	 * @return
	 * @throws ApplicationException
	 */
	public static long add(UserBean ubean) throws ApplicationException, DuplicateRecordException {

		long pk = nextNonBusinessPK("st_user");
		String firstName = ubean.getFirstName();
		String lastName = ubean.getLastName();
		String father = ubean.getFatherName();
		String mother = ubean.getMotherName();
		String login = ubean.getLogin();
		String password = ubean.getPassword();
		long collegeId = ubean.getCollegeId();
		String departement = ubean.getDepartement();
		int semester = ubean.getSemester();
		int year = ubean.getYear();
		Date dob = new java.sql.Date(ubean.getDateOfBirth().getTime());
		String gender = ubean.getGender();
		String mobileNo = ubean.getMobileNo();
		String address = ubean.getAddress();
		Date lastLogin = (Date) ubean.getLastLogin();
		String userLock = ubean.getUserLock();
		String registeredIp = ubean.getRegisteredIp();
		String lastLoginIp = ubean.getLastLoginIp();
		long roleId = ubean.getRoleId();
		int unSuccessfulLogin = ubean.getUnsuccessfulLogin();
		String createBy = ubean.getCreatedBy();

		if (UserModel.FindByLogin(login) != null) {
			throw new DuplicateRecordException("Exception : Record already exitst");
		}

		int rowCount = 0;
		Connection con = null;
		PreparedStatement ps = null;
		String query = "INSERT INTO  `st_user` (id,first_name,last_name,father_name,mother_name,"
				+ "login,password,college_id,departement,semester,year," + "dob,gender,mobile_no,address,registered_ip,"
				+ "last_login,user_lock,last_login_ip,unsuccessful_login,created_by,role_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setLong(1, pk);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, father);
			ps.setString(5, mother);
			ps.setString(6, login);
			ps.setString(7, password);
			ps.setLong(8, collegeId);
			ps.setString(9, departement);
			ps.setInt(10, semester);
			ps.setInt(11, year);
			ps.setDate(12, dob);
			ps.setString(13, gender);
			ps.setString(14, mobileNo);
			ps.setString(15, address);
			ps.setString(16, registeredIp);
			ps.setDate(17, (java.sql.Date) lastLogin);
			ps.setString(18, userLock);
			ps.setString(19, lastLoginIp);
			ps.setInt(20, unSuccessfulLogin);
			ps.setString(21, createBy);
			ps.setLong(22, roleId);

			rowCount = ps.executeUpdate();
			con.setAutoCommit(true);
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		}

		if (roleId == AppRole.STUDENT) {
			StudentBean sbean = new StudentBean();
			sbean.setUserId(pk);
			sbean.setFirstName(firstName);
			sbean.setLastName(lastName);
			sbean.setFatherName(father);
			sbean.setMotherName(mother);
			sbean.setCollegeId(collegeId);
			sbean.setDepartement(departement);
			sbean.setSemester(semester);
			sbean.setYear(year);
			sbean.setDateOfBirth((java.sql.Date) dob);
			sbean.setGender(gender);
			sbean.setMobileNo(mobileNo);
			sbean.setAddress(address);

			/*
			 * Call for Student model addStudent method to add student info
			 * simultaneously in student table
			 */
			StudentModel.addStudent(sbean);

		} else if (roleId == AppRole.STAFF) {
			StaffBean stfbean = new StaffBean();
			stfbean.setUserId(pk);
			stfbean.setFirstName(firstName);
			stfbean.setLastName(lastName);
			stfbean.setFatherName(father);
			stfbean.setMotherName(mother);
			stfbean.setCollegeId(collegeId);
			stfbean.setDepartement(departement);
			stfbean.setSemester(semester);
			stfbean.setYear(year);
			stfbean.setDateOfBirth((java.sql.Date) dob);
			stfbean.setGender(gender);
			stfbean.setMobileNo(mobileNo);
			stfbean.setAddress(address);

			// call for staff model addStaff method
			StaffModel.add(stfbean);
		}

		if (rowCount > 0) {
			return pk;
		} else {
			return -1;
		}
	}

	/**
	 * @param ubean
	 * @return
	 * @throws ApplicationException
	 */
	public static long delete(UserBean ubean) throws ApplicationException,NoRecordFoundException {

		int rowCount = 0;
		Connection con = null;
		PreparedStatement ps = null;
		long id=ubean.getId();
		if (UserModel.findByPk(id) == null) {
			throw new NoRecordFoundException("Exception : No Corresponding record ");
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("DELETE FROM ST_USER WHERE ID=?");
			ps.setLong(1, id);
			rowCount = ps.executeUpdate();
			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
return id;
	}

	/**
	 * 
	 * findByPK method used to find record on given pk
	 * 
	 * @param id
	 * @return
	 */
	public static UserBean findByPk(long id)throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ubean = null;

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_USER WHERE ID=?");
			ps.setLong(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));
				ubean.setLogin(rs.getString(6));
				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(15));
				ubean.setRoleId(rs.getLong(20));

			}
			ps.close();

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return ubean;
	}


	public static UserBean findByRoleId(long id)throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ubean = null;

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_USER WHERE role_id=?");
			ps.setLong(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));
				ubean.setLogin(rs.getString(6));
				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(15));
				ubean.setRoleId(rs.getLong(20));

			}
			ps.close();

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return ubean;
	}

	
	
	
	public static UserBean FindByLogin(String login) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ubean = null;

		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_USER WHERE login=?");
			ps.setString(1, login);
			rs = ps.executeQuery();

			while (rs.next()) {
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));

				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(14));

			}

		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return ubean;
	}

	/**
	 * 
	 * 
	 * 
	 * @param ubean
	 * @return
	 */
	public static long update(UserBean ubean) throws NoRecordFoundException, ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		int rowCount = 0;
		
		Date dob = new java.sql.Date(ubean.getDateOfBirth().getTime());
		
		String query = "UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,"
				+ "FATHER_NAME=?,MOTHER_NAME=?,LOGIN=?,"
				+ "COLLEGE_ID=?,DEPARTEMENT=?,SEMESTER=?,YEAR=?,DOB=?," + "GENDER=?,MOBILE_NO=?,ADDRESS=?,LAST_LOGIN=?,"
				+ "USER_LOCK=?,REGISTERED_IP=?,LAST_LOGIN_IP=?," + "ROLE_ID=?,UNSUCCESSFUL_LOGIN=? WHERE ID=?";

		long pk = ubean.getId();

		if (UserModel.findByPk(pk) == null) {
			throw new NoRecordFoundException("Exception : No record found");

		}
		/*if (UserModel.FindByLogin(ubean.getLogin()) != null) {
			throw new DuplicateRecordException("Exception : Login_id already exists");
		}*/

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1, ubean.getFirstName());
			ps.setString(2, ubean.getLastName());
			ps.setString(3, ubean.getFatherName());
			ps.setString(4, ubean.getMotherName());
			ps.setString(5, ubean.getLogin());
			ps.setLong(6, ubean.getCollegeId());
			ps.setString(7, ubean.getDepartement());
			ps.setInt(8, ubean.getSemester());
			ps.setInt(9, ubean.getYear());
			ps.setDate(10, dob);
			ps.setString(11, ubean.getGender());
			ps.setString(12, ubean.getMobileNo());
			ps.setString(13, ubean.getAddress());
			ps.setDate(14, (java.sql.Date)ubean.getLastLogin());
			ps.setString(15, ubean.getUserLock());
			ps.setString(16, ubean.getRegisteredIp());
			ps.setString(17, ubean.getLastLoginIp());
			ps.setLong(18, ubean.getRoleId());
			ps.setInt(19, ubean.getUnsuccessfulLogin());
			ps.setLong(20, ubean.getId());

			rowCount = ps.executeUpdate();
			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("User bean Exception"+e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}
System.out.println("pk from bean="+pk);
		return pk;
	}

	/**
	 * 
	 * 
	 * 
	 * @param ubean
	 * @return
	 * @throws ApplicationException
	 */

	
	public static UserBean authenticate(UserBean ubean) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		String password = ubean.getPassword();
		String login = ubean.getLogin();
		ubean=null;
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement("SELECT * FROM ST_USER WHERE LOGIN=? AND  PASSWORD =?");
			ps.setString(1, login);
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));
				ubean.setLogin(rs.getString(6));
				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(15));
				ubean.setLastLogin(rs.getDate(16));
				ubean.setRoleId(rs.getLong(20));
				ubean.setCreatedBy(rs.getString(22));
				ubean.setModifiedBy(rs.getString(23));
				ubean.setCreatedDateTime(rs.getTimestamp(24));
				ubean.setModifiedDateTime(rs.getTimestamp(25));
			}

			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		return ubean;
	}

	/**
	 * 
	 * 
	 * 
	 * @param ubean
	 * @return
	 * @throws ApplicationException
	 */
	public static ArrayList<UserBean> search(UserBean ubean, int pageNo, int pageSize) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_USER WHERE 1=1 ");

		/* logic for dynamic search or for creating query */
		if (ubean != null) {
			if (ubean.getId() > 0) {
				query.append(" AND id =" + ubean.getId());
			}
			if (ubean.getFirstName() != null && ubean.getFirstName().trim().length() > 0) {
				query.append(" AND first_name like '" + ubean.getFirstName() + "%'");
			}
			if (ubean.getLastName() != null && ubean.getLastName().trim().length() > 0) {
				query.append(" AND last_name like '" + ubean.getLastName() + "%'");
			}
			if (ubean.getFatherName() != null && ubean.getFatherName().trim().length() > 0) {
				query.append(" AND father_name like '" + ubean.getFatherName() + "%'");
			}
			if (ubean.getMotherName() != null && ubean.getMotherName().trim().length() > 0) {
				query.append(" AND mother_name like '" + ubean.getMotherName() + "%'");
			}
			if (ubean.getRoleId() == 1/* for Student */) {
				query.append(" AND role_id = " + ubean.getRoleId());
			}
			if (ubean.getLogin() != null && ubean.getLogin().trim().length() > 0) {
				query.append(" AND login like '" + ubean.getLogin() + "%'");
			}
			if (ubean.getMobileNo() != null && ubean.getMobileNo().trim().length() > 0) {
				query.append(" AND mobile_no like '" + ubean.getMobileNo() + "%'");
			}
			if (ubean.getGender() != null && ubean.getGender().trim().length() > 0) {
				query.append(" AND gender like '" + ubean.getGender() + "%'");
			}
			if (ubean.getSemester() > 0) {
				query.append(" AND semester = " + ubean.getSemester());
			}
			if (ubean.getYear() > 0) {
				query.append(" AND year = " + ubean.getYear());
			}
			if (ubean.getDepartement() != null && ubean.getDepartement().trim().length() > 0) {
				query.append(" AND departement like '" + ubean.getDepartement() + "%'");
			}
			if (ubean.getCollegeId() > 0) {
				query.append(" AND college_id = " + ubean.getCollegeId());
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			query.append("limit " + pageNo + "," + pageSize);
		}

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));
				ubean.setLogin(rs.getString(6));
				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(15));
				ubean.setLastLogin(rs.getDate(16));
				ubean.setUserLock(rs.getString(17));
				ubean.setLastLoginIp(rs.getString(19));
				ubean.setRoleId(rs.getLong(20));

				list.add(ubean);

			}

			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception from usermodel");
		}
		return list;
	}


	
	
	
	
	public static ArrayList<UserBean> getList(UserBean ubean, int pageNo, int pageSize) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		StringBuffer query = new StringBuffer("SELECT * FROM ST_USER WHERE 1=1 ");

		/* logic for dynamic search or for creating query */
		if (ubean != null) {
			if (ubean.getId() > 0) {
				query.append(" AND id =" + ubean.getId());
			}
			if (ubean.getFirstName() != null && ubean.getFirstName().trim().length() > 0) {
				query.append(" AND first_name like '" + ubean.getFirstName() + "%'");
			}
			if (ubean.getLastName() != null && ubean.getLastName().trim().length() > 0) {
				query.append(" AND last_name like '" + ubean.getLastName() + "%'");
			}
			if (ubean.getLogin() != null && ubean.getLogin().trim().length() > 0) {
				query.append(" AND login like '" + ubean.getLogin() + "%'");
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
				ubean = new UserBean();
				ubean.setId(rs.getLong(1));
				ubean.setFirstName(rs.getString(2));
				ubean.setLastName(rs.getString(3));
				ubean.setFatherName(rs.getString(4));
				ubean.setMotherName(rs.getString(5));
				ubean.setLogin(rs.getString(6));
				ubean.setCollegeId(rs.getLong(8));
				ubean.setDepartement(rs.getString(9));
				ubean.setSemester(rs.getInt(10));
				ubean.setYear(rs.getInt(11));
				ubean.setDateOfBirth(rs.getDate(12));
				ubean.setGender(rs.getString(13));
				ubean.setMobileNo(rs.getString(14));
				ubean.setAddress(rs.getString(15));
				ubean.setLastLogin(rs.getDate(16));
				ubean.setUserLock(rs.getString(17));
				ubean.setLastLoginIp(rs.getString(19));
				ubean.setRoleId(rs.getLong(20));

				list.add(ubean);

			}

			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception from usermodel");
		}
		return list;
	}

	
	/**
	 * @param login
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public static boolean changePassword(String login, String oldPassword, String newPassword)
			throws NoRecordFoundException, ApplicationException {
		UserBean ubean = new UserBean();
		ubean.setLogin(login);
		ubean.setPassword(oldPassword);
		if (UserModel.authenticate(ubean) == null) {
			throw new NoRecordFoundException("Not a valid user");
		}
		Connection con = null;
		PreparedStatement ps = null;
		int rowCount = 0;
		String query = "UPDATE ST_USER SET PASSWORD=? WHERE LOGIN=?";

		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1, newPassword);
			ps.setString(2, login);
			rowCount = ps.executeUpdate();
			con.setAutoCommit(true);
			ps.close();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(con);
			e.printStackTrace();
			throw new ApplicationException("Exception from changePassword");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 
	 * @param login
	 * @return
	 * @throws NoRecordFoundException
	 * @throws ApplicationException
	 */
	public boolean forgetPassword(String login) throws NoRecordFoundException, ApplicationException {
		if (UserModel.FindByLogin(login) == null) {
			throw new NoRecordFoundException("Not a valid login id ");
		}
		/**
		 * 
		 * After mail api
		 * 
		 */
		return false;
	}

}
