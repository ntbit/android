package vn.ava.mobilereader.model;

public class SubscribeForm {

	private String name;
	private String email;
	private String emailConfirm;
	private String job;
	private String city;
	private String country;

	public SubscribeForm() {

	}

	public SubscribeForm(String name, String email, String emailconfirm,
			String job, String city, String country) {

		this.name = name;
		this.email = email;
		this.emailConfirm = emailconfirm;
		this.job = job;
		this.city = city;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailConfirm() {
		return emailConfirm;
	}

	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	
	
}
