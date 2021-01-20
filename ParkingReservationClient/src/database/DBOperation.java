package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Model;
import model.Reservation;
import model.Reservation;

public class DBOperation {
	private DBConnection db;

	public DBOperation() {
		db = DBConnection.getInstance();
	}

	public boolean insert(Model m) {
		Connection conn = null;
		String sql = null;
		boolean success = true;
		
		if(m instanceof Reservation) 
			sql = "insert into reservation values(?, ?, ?, ?)";

		PreparedStatement pstmt = null;
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			if(m instanceof Reservation) {
				pstmt.setString(1, ((Reservation) m).getPlatenumber());
				pstmt.setString(2, ((Reservation) m).getDate());
				pstmt.setString(3, ((Reservation) m).getTimeslot());
				pstmt.setString(3, ((Reservation) m).getLocationID());
			}

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}

	public Model select(String model, String id) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Model m = null;
		String sql = null;
		
		if(model.equals("reservation"))
			sql = "select * from reservation where platenumber=?";
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(model.equals("reservation")) {
					return new Reservation(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return null;
	}

	public void update(Model m) {
		Connection conn = null;
		String sql = null;
		
		if(m instanceof Reservation) 
			sql = "update reservation set date=?, timeslot=?, locationID=? where platenumber=?";

		PreparedStatement pstmt = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);

			if(m instanceof Reservation) {
				pstmt.setString(1, ((Reservation) m).getDate());
				pstmt.setString(3, ((Reservation) m).getTimeslot());
				pstmt.setString(10, ((Reservation) m).getLocationID());
				pstmt.setString(10, ((Reservation) m).getPlatenumber());
			}
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public void delete(String model, String id) {
		Connection conn = null;
		String sql = null;
		
		if(model.equals("reservation"))
			sql = "delete reservation where platenumber=?";
		
		PreparedStatement pstmt = null;
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
