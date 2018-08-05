package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beans.BabySitter;
import com.beans.Toddler;
import com.util.DBUtil;

public class ManagementDAO {

	public ArrayList<BabySitter> getBabySitterNames() throws SQLException {

		Connection conn = DBUtil.getConnection();
		ArrayList<BabySitter> babySitters = new ArrayList<>();
		PreparedStatement pstmt;
		pstmt = conn.prepareStatement("SELECT * FROM BABYSITTER_1418811 WHERE BSSTATUS=?");
		pstmt.setString(1, "UNTAGGED");

		ResultSet rst = pstmt.executeQuery();

		while (rst.next()) {
			BabySitter babySitter = new BabySitter();
			babySitter.setBsId(rst.getLong(1));
			babySitter.setBsName(rst.getString(2));
			babySitter.setBsAge(rst.getInt(3));
			babySitter.setBseEperience(rst.getInt(4));
			babySitter.setBsType(rst.getString(5));
			babySitter.setBsStatus(rst.getString(6));
			babySitters.add(babySitter);
		}

		DBUtil.closeSet(rst);
		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return babySitters;
	}

	public long registerBabySitter(BabySitter babySitter) throws SQLException {

		Connection conn = DBUtil.getConnection();
		long bsId = -1;

		PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO BABYSITTER_1418811 VALUES (bsSequence.nextval,?,?,?,?,?)");
		pstmt.setString(1, babySitter.getBsName());
		pstmt.setInt(2, babySitter.getBsAge());
		pstmt.setInt(3, babySitter.getBseEperience());
		pstmt.setString(4, babySitter.getBsType());
		pstmt.setString(5, "UNTAGGED");

		if (pstmt.executeUpdate() > 0) {
			PreparedStatement getId = conn.prepareStatement("SELECT MAX(BSID) FROM BABYSITTER_1418811");

			ResultSet rst = getId.executeQuery();

			while (rst.next()) {
				bsId = rst.getLong(1);
			}

			DBUtil.closeSet(rst);
			DBUtil.closeStatement(getId);
		}

		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return bsId;
	}

	public long registerToddler(Toddler toddler) throws SQLException {

		Connection conn = DBUtil.getConnection();
		long tdId = -1;

		PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO TODDLER_1418811 VALUES (tdSequence.nextval,?,?,?)");
		pstmt.setString(1, toddler.getToodlerName());
		pstmt.setInt(2, toddler.getToddlerAge());
		pstmt.setLong(3, toddler.getBabySitter().getBsId());

		if (pstmt.executeUpdate() > 0) {
			PreparedStatement getId = conn.prepareStatement("SELECT MAX(TODDLERID) FROM TODDLER_1418811");

			ResultSet rst = getId.executeQuery();

			while (rst.next()) {
				tdId = rst.getLong(1);
			}

			DBUtil.closeSet(rst);
			DBUtil.closeStatement(getId);
		}

		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);
		return tdId;
	}

	public boolean tagBabySitterWithToddler(long id) throws SQLException {

		boolean isSuccess = false;
		Connection conn = DBUtil.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("UPDATE BABYSITTER_1418811 SET BSSTATUS='TAGGED' WHERE BSID=?");
		pstmt.setLong(1, id);

		if (pstmt.executeUpdate() > 0) {
			isSuccess = true;
		}

		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return isSuccess;
	}

	public boolean unTagBabySitterWithToddler(long id) throws SQLException {

		boolean isSuccess = false;
		Connection conn = DBUtil.getConnection();

		PreparedStatement pstmt = conn
				.prepareStatement("UPDATE BABYSITTER_1418811 SET BSSTATUS='UNTAGGED' WHERE BSID=?");
		pstmt.setLong(1, id);

		if (pstmt.executeUpdate() > 0) {
			isSuccess = true;
		}

		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);
		return isSuccess;
	}

	public ArrayList<Toddler> getAllToddlers() throws SQLException {

		Connection conn = DBUtil.getConnection();
		ArrayList<Toddler> toddlers = new ArrayList<>();

		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT * FROM BABYSITTER_1418811, TODDLER_1418811 WHERE BABYSITTER_1418811.BSID = TODDLER_1418811.BSID");

		ResultSet rst = pstmt.executeQuery();

		while (rst.next()) {
			Toddler toddler = new Toddler();
			toddler.setToddlerId(rst.getLong(7));
			toddler.setToodlerName(rst.getString(8));
			toddler.setToddlerAge(rst.getInt(9));

			BabySitter babySitter = new BabySitter();
			babySitter.setBsId(rst.getLong(1));
			babySitter.setBsName(rst.getString(2));
			babySitter.setBsAge(rst.getInt(3));
			babySitter.setBseEperience(rst.getInt(4));
			babySitter.setBsType(rst.getString(5));
			babySitter.setBsStatus(rst.getString(6));

			toddler.setBabySitter(babySitter);

			toddlers.add(toddler);
		}
		DBUtil.closeSet(rst);
		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return toddlers;
	}

	public Toddler getToddler(long tdId) throws SQLException {

		Connection conn = DBUtil.getConnection();
		Toddler toddler = new Toddler();

		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT * FROM BABYSITTER_1418811, TODDLER_1418811 WHERE BABYSITTER_1418811.BSID = TODDLER_1418811.BSID AND TODDLER_1418811.TODDLERID=?");
		pstmt.setLong(1, tdId);

		ResultSet rst = pstmt.executeQuery();

		while (rst.next()) {

			toddler.setToddlerId(rst.getLong(7));
			toddler.setToodlerName(rst.getString(8));
			toddler.setToddlerAge(rst.getInt(9));

			BabySitter babySitter = new BabySitter();
			babySitter.setBsId(rst.getLong(1));
			babySitter.setBsName(rst.getString(2));
			babySitter.setBsAge(rst.getInt(3));
			babySitter.setBseEperience(rst.getInt(4));
			babySitter.setBsType(rst.getString(5));
			babySitter.setBsStatus(rst.getString(6));

			toddler.setBabySitter(babySitter);

		}
		DBUtil.closeSet(rst);
		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return toddler;
	}

	public boolean updateToddler(Toddler toddler) throws SQLException {

		Connection conn = DBUtil.getConnection();
		boolean isSuccess = false;

		PreparedStatement pstmt = conn
				.prepareStatement("UPDATE TODDLER_1418811 SET TODDLERNAME = ?, BSID = ? WHERE TODDLERID = ?");
		pstmt.setString(1, toddler.getToodlerName());
		pstmt.setLong(2, toddler.getBabySitter().getBsId());
		pstmt.setLong(3, toddler.getToddlerId());

		if (pstmt.executeUpdate() > 0) {
			isSuccess = true;
		}

		DBUtil.closeStatement(pstmt);
		DBUtil.closeConnection(conn);

		return isSuccess;
	}
}
