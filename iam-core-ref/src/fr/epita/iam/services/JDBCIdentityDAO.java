/**
 * 
 */
package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.epita.iam.datamodel.Identity;

/**
 * @author tbrou
 *
 */
public class JDBCIdentityDAO {

	
	
	private Connection connection;
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public JDBCIdentityDAO() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/IAM;create=true","TOM","TOM");
	}
	
	
	public void writeIdentity(Identity identity) throws SQLException {
		String insertStatement = "insert into IDENTITIES (IDENTITIES_DISPLAYNAME, IDENTITIES_EMAIL, IDENTITIES_BIRTHDATE) "
				+ "values(?, ?, ?)";
		PreparedStatement pstmt_insert = connection.prepareStatement(insertStatement);
		pstmt_insert.setString(1, identity.getDisplayName());
		pstmt_insert.setString(2, identity.getEmail());
		Date now = new Date();
		pstmt_insert.setDate(3, new java.sql.Date(now.getTime()));

		pstmt_insert.execute();

	}

	public List<Identity> readAll() throws SQLException {
		List<Identity> identities = new ArrayList<Identity>();

		PreparedStatement pstmt_select = connection.prepareStatement("select * from IDENTITIES");
		ResultSet rs = pstmt_select.executeQuery();
		while (rs.next()) {
			String displayName = rs.getString("IDENTITIES_DISPLAYNAME");
			String uid = String.valueOf(rs.getString("IDENTITIES_UID"));
			String email = rs.getString("IDENTITIES_EMAIL");
			Date birthDate = rs.getDate("IDENTITIES_BIRTHDATE");
			Identity identity = new Identity(uid, displayName, email);
			identities.add(identity);
		}
		return identities;

	}

	public void delete(String uid) throws SQLException {
		

		PreparedStatement statement = connection.prepareStatement("DELETE FROM IDENTITIES  WHERE IDENTITIES_EMAIL=? ");
		statement.setString(1,uid);

	statement.execute();
			
	}
	
	public void update(Identity identity) throws SQLException {
	
		PreparedStatement statement = connection.prepareStatement("update IDENTITIES set IDENTITIES_EMAIL=?, IDENTITIES_DISPLAYNAME=? WHERE IDENTITIES_UID=? ");
		statement.setString(1,identity.getEmail());
		statement.setString(2,identity.getDisplayName());
		statement.setString(3,identity.getUid());
		
	statement.execute();
			
	}
	
	
	public Identity fetch(String email) throws SQLException {
	

		PreparedStatement statement = connection.prepareStatement("select * from IDENTITIES where IDENTITIES_EMAIL=?");
		statement.setString(1, email);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			int uid = rs.getInt("IDENTITIES_UID");
			String displayName = rs.getString("IDENTITIES_DISPLAYNAME");
			String email1 = rs.getString("IDENTITIES_EMAIL");
			return(new Identity(String.valueOf(uid), displayName, email1));
		
		}
		return null;
		
	}

	public boolean authenticate(String user, String password) throws SQLException {


		PreparedStatement statement = connection.prepareStatement("select * from ADMIN where admin_user=? AND admin_pass=?");
		statement.setString(1,user);
		statement.setString(2,password);

		ResultSet rs = statement.executeQuery();
	return (rs.next());
	}
	
}
