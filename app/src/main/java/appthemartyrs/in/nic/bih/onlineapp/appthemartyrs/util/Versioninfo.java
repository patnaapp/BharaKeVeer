package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class Versioninfo implements KvmSerializable{

	// <IsUpdated>1</IsUpdated>
	// <Priority>1</Priority>
	// <AdminMsg>aa</AdminMsg>
	// <AdminTitle>abc</AdminTitle>
	// <UpdateTile>NA</UpdateTile>
	// <UpdateMsg>NA</UpdateMsg>
	// <Role/>
	// <AppUrl>onlineapp.bih.nic.in</AppUrl>
	// <IsValidDevice>true</IsValidDevice>
	// <IMEI>352996066621734</IMEI>
	public static Class<Versioninfo> Versioninfo_CLASS = Versioninfo.class;
	private String adminTitle;
	private String adminMsg;
	private String updateTile;
	private String updateMsg;
	private String appUrl;
	private String role;
	private String imei;
	private boolean isVerUpdated;
	private boolean isValidDevice;
	private int priority;
	
	
	public Versioninfo() {

	}

	public Versioninfo(SoapObject obj) {

		this.isVerUpdated = Boolean.valueOf(obj.getProperty("IsUpdated")
				.toString().toLowerCase());
		this.isValidDevice = Boolean.valueOf(obj.getProperty("IsValidDevice")
				.toString().toLowerCase());
		this.priority = Integer.valueOf(obj.getProperty("Priority")
				.toString().trim());
		this.adminTitle = obj.getProperty("AdminTitle").toString();
		this.adminMsg = obj.getProperty("AdminMsg").toString();
		this.updateTile = obj.getProperty("UpdateTile").toString();
		this.updateMsg = obj.getProperty("UpdateMsg").toString();
		this.appUrl = obj.getProperty("AppUrl").toString();
		
		//this.role = obj.getProperty("Role").toString();
		//this.imei = obj.getProperty("IMEI").toString();

	}

	public String getAdminMsg() {
		return adminMsg;
	}

	public void setAdminMsg(String adminMsg) {
		this.adminMsg = adminMsg;
	}

	public String getAdminTitle() {
		return adminTitle;
	}

	public void setAdminTitle(String adminTitle) {
		this.adminTitle = adminTitle;
	}

	public String getUpdateTile() {
		return updateTile;
	}

	public void setUpdateTile(String updateTile) {
		this.updateTile = updateTile;
	}

	public String getUpdateMsg() {
		return updateMsg;
	}

	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public boolean isVerUpdated() {
		return isVerUpdated;
	}

	public void setVerUpdated(boolean isVerUpdated) {
		this.isVerUpdated = isVerUpdated;
	}

	public boolean isValidDevice() {
		return isValidDevice;
	}

	public void setValidDevice(boolean isValidDevice) {
		this.isValidDevice = isValidDevice;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
