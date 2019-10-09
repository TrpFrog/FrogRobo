package net.trpfrog.frogrobo.reminder;

public class Remind {
	private long addedTime;
	private long alartTime;
	private long userId;
	private String text;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (addedTime ^ (addedTime >>> 32));
		result = prime * result + (int) (alartTime ^ (alartTime >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Remind other = (Remind) obj;
		if (addedTime != other.addedTime)
			return false;
		if (alartTime != other.alartTime)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	public Remind(long addedTime, long alartTime, long userId, String text) {
		super();
		this.addedTime = addedTime;
		this.alartTime = alartTime;
		this.userId = userId;
		this.text = text;
	}

	public Remind(String dataSet) {
		String[] data = dataSet.split(" ");
		this.addedTime = Long.parseLong(data[0]);
		this.alartTime = Long.parseLong(data[1]);
		this.userId = Long.parseLong(data[2]);
		//this.text = //TODO:やれ
	}

	public long getAlartTime() {
		return alartTime;
	}
	public void setAlartTime(long alartTime, long userId) throws UnauthrizedException {
		if(this.userId != userId){
			throw new UnauthrizedException();
		}
		this.alartTime = alartTime;
	}
	public String getText() {
		return text;
	}
	public void setText(String text, long userId) throws UnauthrizedException {
		if(this.userId != userId){
			throw new UnauthrizedException();
		}
		this.text = text;
	}
	public long getAddedTime() {
		return addedTime;
	}
	public long getUserId() {
		return userId;
	}


}
class UnauthrizedException extends Exception{
}
