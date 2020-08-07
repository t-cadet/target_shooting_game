package model;

public enum Modificateurs {
	//ATTRIBUTS
	HEAL_10("HEAL +10",0,10,0,0,0),
	HEAL_50("HEAL +50",0,50,0,0,0),
	HEAL_100("HEAL +100",0,100,0,0,0),
	
	FIRERATE_50("FIRERATE +50%",15000,0,0.5,0,0),
	FIRERATE_100("FIRERATE +100%",15000,0,1,0,0),

	PORTEE_50("PORTEE +50%",15000,0,0,0,0.5),
	PORTEE_100("PORTEE +100%",15000,0,0,0,1),

	DAMAGE_50("DAMAGE +50%",15000,0,0,0.5,0),
	DAMAGE_100("DAMAGE +100%",15000,0,0,1,0);
	
	//ATTRIBUTS
	String name;
	long duree;
	int heal;
	double modFireRate;
	double modDamage;
	double modR;
	
	//CONSTRUCTEUR
	private Modificateurs(String name, long duree, int heal, double modCadence, double modDegat, double modAOE) {
		this.name = name; 
		this.duree = duree;
		this.heal = heal;
		this.modFireRate = modCadence;
		this.modDamage = modDegat;
		this.modR = modAOE;
	}
	//METHODES
	public boolean equals(Modificateurs m){
		return this.name().equals(m.getName());
	}
	public static Modificateurs getRandomModificateur(){
		Modificateurs[] mod = Modificateurs.values();
		final int index = (int) (Math.random()*mod.length);
		return mod[index];
	}
	public String toString(){
		return name;
	}
	//GETTERS & SETTERS
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDuree() {
		return duree;
	}
	public void setDuree(long duree) {
		this.duree = duree;
	}
	public int getHeal() {
		return heal;
	}
	public void setHeal(int heal) {
		this.heal = heal;
	}
	public double getModFireRate() {
		return modFireRate;
	}
	public void setModFireRate(double modCadence) {
		this.modFireRate = modCadence;
	}
	public double getModDamage() {
		return modDamage;
	}
	public void setModDamage(double modDamage) {
		this.modDamage = modDamage;
	}
	public double getModR() {
		return modR;
	}
	public void setModR(double modR) {
		this.modR = modR;
	}
	
}
