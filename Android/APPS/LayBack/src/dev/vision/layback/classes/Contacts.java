package dev.vision.layback.classes;

public class Contacts {
    ContactsMap map = new ContactsMap();
    ContactsList list = new ContactsList();
    
    public Contact getContact(String number){
		return map.get(number);
	}
    
    public Contact getContact(int pos){
		return list.get(pos);
	}
    
    public ContactsList getList(){
		return list;
	}

	public void addContact(Contact r) {
		list.add(r);
		for(Number n : r.getNumbers()){
			map.put(n.number, r);
		}
	}
}
