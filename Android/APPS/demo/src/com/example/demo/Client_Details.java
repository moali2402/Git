package com.example.demo;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class Client_Details {
	String title = "";
	String f_name= "";
	String m_name= "";
	String s_name= "";
	String address= "";
	String pcode= "";
	String suburb= "";
	String state= "";
	String tel= "";
	String mob= "";
	String work= "";
	String DOB= "";
	String id= "";
	String notes= "";
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the f_name
	 */
	public String getF_name() {
		return f_name;
	}
	/**
	 * @param f_name the f_name to set
	 */
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	/**
	 * @return the m_name
	 */
	public String getM_name() {
		return m_name;
	}
	/**
	 * @param m_name the m_name to set
	 */
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	/**
	 * @return the s_name
	 */
	public String getS_name() {
		return s_name;
	}
	/**
	 * @param s_name the s_name to set
	 */
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the pcode
	 */
	public String getPcode() {
		return pcode;
	}
	/**
	 * @param pcode the pcode to set
	 */
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	/**
	 * @return the suburb
	 */
	public String getSuburb() {
		return suburb;
	}
	/**
	 * @param suburb the suburb to set
	 */
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the mob
	 */
	public String getMob() {
		return mob;
	}
	/**
	 * @param mob the mob to set
	 */
	public void setMob(String mob) {
		this.mob = mob;
	}
	/**
	 * @return the work
	 */
	public String getWork() {
		return work;
	}
	/**
	 * @param work the work to set
	 */
	public void setWork(String work) {
		this.work = work;
	}
	/**
	 * @return the dOB
	 */
	public String getDOB() {
		return DOB;
	}
	/**
	 * @param dOB the dOB to set
	 */
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isFilled(View c) {
		getFields(c);
		return ( title != null && !title.isEmpty() &&
			f_name != null && !f_name.isEmpty() &&
			m_name != null && !m_name.isEmpty() &&
			s_name != null && !s_name.isEmpty() &&
			address != null && !address.isEmpty() &&
			pcode != null && !pcode.isEmpty() &&
			suburb != null && !suburb.isEmpty() &&
			state != null && !state.isEmpty() &&
			tel != null && !tel.isEmpty() &&
			mob != null && !mob.isEmpty() &&
			work != null && !work.isEmpty() &&
			DOB != null && !DOB.isEmpty() &&
			id != null && !id.isEmpty() &&
			notes != null && !notes.isEmpty());

	}
	
	
	void getFields(View c){
		title = ((EditText) c.findViewById(R.id.client_details_title)).getText().toString();
		f_name = ((EditText) c.findViewById(R.id.client_details_first_name)).getText().toString();
		m_name= ((EditText) c.findViewById(R.id.client_details_middle_name)).getText().toString();
		s_name = ((EditText) c.findViewById(R.id.client_details_surname)).getText().toString();
		 
		address = ((EditText) c.findViewById(R.id.client_details_address)).getText().toString();
		suburb = ((EditText) c.findViewById(R.id.client_details_suburb)).getText().toString();
		pcode = ((EditText) c.findViewById(R.id.client_details_pcode)).getText().toString();
		state = ((EditText) c.findViewById(R.id.client_details_state)).getText().toString();
		 
		mob = ((EditText) c.findViewById(R.id.client_details_mobile)).getText().toString();
		tel = ((EditText) c.findViewById(R.id.client_details_home)).getText().toString();
		work = ((EditText) c.findViewById(R.id.client_details_work)).getText().toString();
		DOB = ((EditText) c.findViewById(R.id.client_details_dob)).getText().toString();
		 
		id = ((EditText) c.findViewById(R.id.client_details_id)).getText().toString();
		notes = ((EditText) c.findViewById(R.id.client_details_notes)).getText().toString();
	 }
	
	void resetFields(View c){
		((EditText) c.findViewById(R.id.client_details_title)).setText("");
		((EditText) c.findViewById(R.id.client_details_first_name)).setText("");
		((EditText) c.findViewById(R.id.client_details_middle_name)).setText("");
		((EditText) c.findViewById(R.id.client_details_surname)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_address)).setText("");
		((EditText) c.findViewById(R.id.client_details_suburb)).setText("");
		((EditText) c.findViewById(R.id.client_details_pcode)).setText("");
		((EditText) c.findViewById(R.id.client_details_state)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_mobile)).setText("");
		((EditText) c.findViewById(R.id.client_details_home)).setText("");
		((EditText) c.findViewById(R.id.client_details_work)).setText("");
		((EditText) c.findViewById(R.id.client_details_dob)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_id)).setText("");
		((EditText) c.findViewById(R.id.client_details_notes)).setText("");
		
		((EditText) c.findViewById(R.id.Quote_Title)).setText("");
		((EditText) c.findViewById(R.id.Quote_First_name)).setText("");
		((EditText) c.findViewById(R.id.Quote_Middle_Name)).setText("");
		((EditText) c.findViewById(R.id.Quote_surname)).setText("");		 
		((EditText) c.findViewById(R.id.Quote_tel)).setText("");

		((EditText) c.findViewById(R.id.Quote_address)).setText("");
		((EditText) c.findViewById(R.id.Quote_suburb)).setText("");
		((EditText) c.findViewById(R.id.Quote_state)).setText("");
		
		 title = "";
		 f_name= "";
		 m_name= "";
		 s_name= "";
		 address= "";
		 pcode= "";
		 suburb= "";
		 state= "";
		 mob= "";
		 tel= "";
		 work= "";
		 DOB= "";
		 id= "";
		 notes= "";
	 }
	
	public void setClientDetailsFields(View c) {
		((EditText) c.findViewById(R.id.client_details_title)).setText(title);
		((EditText) c.findViewById(R.id.client_details_first_name)).setText(f_name);
		((EditText) c.findViewById(R.id.client_details_middle_name)).setText(m_name);
		((EditText) c.findViewById(R.id.client_details_surname)).setText(s_name);
		 
		((EditText) c.findViewById(R.id.client_details_address)).setText(address);
		((EditText) c.findViewById(R.id.client_details_suburb)).setText(suburb);
		((EditText) c.findViewById(R.id.client_details_pcode)).setText(pcode);
		((EditText) c.findViewById(R.id.client_details_state)).setText(state);
		 
		((EditText) c.findViewById(R.id.client_details_mobile)).setText(mob);
		((EditText) c.findViewById(R.id.client_details_home)).setText(tel);
		((EditText) c.findViewById(R.id.client_details_work)).setText(work);
		((EditText) c.findViewById(R.id.client_details_dob)).setText(DOB);
		 
		((EditText) c.findViewById(R.id.client_details_id)).setText(id);
		((EditText) c.findViewById(R.id.client_details_notes)).setText(notes);		
	}
	
	
	public void setQuoteClientFields(View c) {
		((EditText) c.findViewById(R.id.Quote_Title)).setText(title);
		((EditText) c.findViewById(R.id.Quote_First_name)).setText(f_name);
		((EditText) c.findViewById(R.id.Quote_Middle_Name)).setText(m_name);
		((EditText) c.findViewById(R.id.Quote_surname)).setText(s_name);		 
		((EditText) c.findViewById(R.id.Quote_tel)).setText(mob.isEmpty() ? (tel.isEmpty() ? work: tel ) :mob);

		((EditText) c.findViewById(R.id.Quote_address)).setText(address);
		((EditText) c.findViewById(R.id.Quote_suburb)).setText(suburb);
		((EditText) c.findViewById(R.id.Quote_state)).setText(state);
	}
	
	void getFieldsQ(View c){
		title = ((EditText) c.findViewById(R.id.Quote_Title) ).getText().toString();
		f_name = ((EditText) c.findViewById(R.id.Quote_First_name)).getText().toString();
		m_name= ((EditText) c.findViewById(R.id.Quote_Middle_Name)).getText().toString();
		s_name = ((EditText) c.findViewById(R.id.Quote_surname)).getText().toString();
		 
		address = ((EditText) c.findViewById(R.id.Quote_address)).getText().toString();
		suburb = ((EditText) c.findViewById(R.id.Quote_suburb)).getText().toString();
		state = ((EditText) c.findViewById(R.id.Quote_state)).getText().toString();
		 
		mob = ((EditText) c.findViewById(R.id.Quote_tel)).getText().toString();
	 }
	
	
	public boolean isFilled(Quote_Activity c) {
		getFields(c);
		return ( title != null && !title.isEmpty() &&
			f_name != null && !f_name.isEmpty() &&
			m_name != null && !m_name.isEmpty() &&
			s_name != null && !s_name.isEmpty() &&
			address != null && !address.isEmpty() &&
			pcode != null && !pcode.isEmpty() &&
			suburb != null && !suburb.isEmpty() &&
			state != null && !state.isEmpty() &&
			tel != null && !tel.isEmpty() &&
			mob != null && !mob.isEmpty() &&
			work != null && !work.isEmpty() &&
			DOB != null && !DOB.isEmpty() &&
			id != null && !id.isEmpty() &&
			notes != null && !notes.isEmpty());

	}
	
	
	void getFields(Activity c){
		title = ((EditText) c.findViewById(R.id.client_details_title) ).getText().toString();
		f_name = ((EditText) c.findViewById(R.id.client_details_first_name)).getText().toString();
		m_name= ((EditText) c.findViewById(R.id.client_details_middle_name)).getText().toString();
		s_name = ((EditText) c.findViewById(R.id.client_details_surname)).getText().toString();
		 
		address = ((EditText) c.findViewById(R.id.client_details_address)).getText().toString();
		suburb = ((EditText) c.findViewById(R.id.client_details_suburb)).getText().toString();
		pcode = ((EditText) c.findViewById(R.id.client_details_pcode)).getText().toString();
		state = ((EditText) c.findViewById(R.id.client_details_state)).getText().toString();
		 
		mob = ((EditText) c.findViewById(R.id.client_details_mobile)).getText().toString();
		tel = ((EditText) c.findViewById(R.id.client_details_home)).getText().toString();
		work = ((EditText) c.findViewById(R.id.client_details_work)).getText().toString();
		DOB = ((EditText) c.findViewById(R.id.client_details_dob)).getText().toString();
		 
		id = ((EditText) c.findViewById(R.id.client_details_id)).getText().toString();
		notes = ((EditText) c.findViewById(R.id.client_details_notes)).getText().toString();
	 }
	
	
	
	void resetFields(Activity c){
		((EditText) c.findViewById(R.id.client_details_title)).setText("");
		((EditText) c.findViewById(R.id.client_details_first_name)).setText("");
		((EditText) c.findViewById(R.id.client_details_middle_name)).setText("");
		((EditText) c.findViewById(R.id.client_details_surname)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_address)).setText("");
		((EditText) c.findViewById(R.id.client_details_suburb)).setText("");
		((EditText) c.findViewById(R.id.client_details_pcode)).setText("");
		((EditText) c.findViewById(R.id.client_details_state)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_mobile)).setText("");
		((EditText) c.findViewById(R.id.client_details_home)).setText("");
		((EditText) c.findViewById(R.id.client_details_work)).setText("");
		((EditText) c.findViewById(R.id.client_details_dob)).setText("");
		 
		((EditText) c.findViewById(R.id.client_details_id)).setText("");
		((EditText) c.findViewById(R.id.client_details_notes)).setText("");
		
		((EditText) c.findViewById(R.id.Quote_Title)).setText("");
		((EditText) c.findViewById(R.id.Quote_First_name)).setText("");
		((EditText) c.findViewById(R.id.Quote_Middle_Name)).setText("");
		((EditText) c.findViewById(R.id.Quote_surname)).setText("");		 
		((EditText) c.findViewById(R.id.Quote_tel)).setText("");

		((EditText) c.findViewById(R.id.Quote_address)).setText("");
		((EditText) c.findViewById(R.id.Quote_suburb)).setText("");
		((EditText) c.findViewById(R.id.Quote_state)).setText("");
		
		 title = "";
		 f_name= "";
		 m_name= "";
		 s_name= "";
		 address= "";
		 pcode= "";
		 suburb= "";
		 state= "";
		 mob= "";
		 tel= "";
		 work= "";
		 DOB= "";
		 id= "";
		 notes= "";
	 }
	
	public void setClientDetailsFields(Activity c) {
		((EditText) c.findViewById(R.id.client_details_title)).setText(title);
		((EditText) c.findViewById(R.id.client_details_first_name)).setText(f_name);
		((EditText) c.findViewById(R.id.client_details_middle_name)).setText(m_name);
		((EditText) c.findViewById(R.id.client_details_surname)).setText(s_name);
		 
		((EditText) c.findViewById(R.id.client_details_address)).setText(address);
		((EditText) c.findViewById(R.id.client_details_suburb)).setText(suburb);
		((EditText) c.findViewById(R.id.client_details_pcode)).setText(pcode);
		((EditText) c.findViewById(R.id.client_details_state)).setText(state);
		 
		((EditText) c.findViewById(R.id.client_details_mobile)).setText(mob);
		((EditText) c.findViewById(R.id.client_details_home)).setText(tel);
		((EditText) c.findViewById(R.id.client_details_work)).setText(work);
		((EditText) c.findViewById(R.id.client_details_dob)).setText(DOB);
		 
		((EditText) c.findViewById(R.id.client_details_id)).setText(id);
		((EditText) c.findViewById(R.id.client_details_notes)).setText(notes);		
	}
	
	
	public void setQuoteClientFields(Activity c) {
		((EditText) c.findViewById(R.id.Quote_Title)).setText(title);
		((EditText) c.findViewById(R.id.Quote_First_name)).setText(f_name);
		((EditText) c.findViewById(R.id.Quote_Middle_Name)).setText(m_name);
		((EditText) c.findViewById(R.id.Quote_surname)).setText(s_name);		 
		((EditText) c.findViewById(R.id.Quote_tel)).setText(mob.isEmpty() ? (tel.isEmpty() ? work: tel ) :mob);

		((EditText) c.findViewById(R.id.Quote_address)).setText(address);
		((EditText) c.findViewById(R.id.Quote_suburb)).setText(suburb);
		((EditText) c.findViewById(R.id.Quote_state)).setText(state);
	}
	
	void getFieldsQ(Activity c){
		title = ((EditText) c.findViewById(R.id.Quote_Title) ).getText().toString();
		f_name = ((EditText) c.findViewById(R.id.Quote_First_name)).getText().toString();
		m_name= ((EditText) c.findViewById(R.id.Quote_Middle_Name)).getText().toString();
		s_name = ((EditText) c.findViewById(R.id.Quote_surname)).getText().toString();
		 
		address = ((EditText) c.findViewById(R.id.Quote_address)).getText().toString();
		suburb = ((EditText) c.findViewById(R.id.Quote_suburb)).getText().toString();
		state = ((EditText) c.findViewById(R.id.Quote_state)).getText().toString();
		 
		mob = ((EditText) c.findViewById(R.id.Quote_tel)).getText().toString();
	 }

}
