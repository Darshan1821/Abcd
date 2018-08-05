package com.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.beans.BabySitter;
import com.beans.Toddler;
import com.dao.ManagementDAO;

public class Service {

	ManagementDAO managementDAO = new ManagementDAO();
	
	public ArrayList<BabySitter> getBabySitterNames() throws SQLException{
		return managementDAO.getBabySitterNames();	
	}
	
	public long tagToddler(Toddler toddler) throws SQLException{
		if(managementDAO.tagBabySitterWithToddler(toddler.getBabySitter().getBsId())){
			return managementDAO.registerToddler(toddler);
		}
		return -1;
	}
	
	public ArrayList<Toddler> getAllToddlers() throws SQLException{
		return managementDAO.getAllToddlers();
	}
	
	public long registerBabySitter(BabySitter babySitter) throws SQLException{
		return managementDAO.registerBabySitter(babySitter);
	}
	
	public Toddler getToddler(long tdId) throws SQLException{
		return managementDAO.getToddler(tdId);
	}
	
	public boolean updateToddler(Toddler toddler) throws SQLException{
		
		Toddler tr = getToddler(toddler.getToddlerId()); 
		
		if(managementDAO.unTagBabySitterWithToddler(tr.getBabySitter().getBsId())){
			if(managementDAO.tagBabySitterWithToddler(toddler.getBabySitter().getBsId())){
				return managementDAO.updateToddler(toddler);
			}
		}
		return false;
	}
}
