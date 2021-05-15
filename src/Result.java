class Result{
	private String Path;
	private long Num;
	private double Cost;
	private long Millis;
	public Result(String Path, long Num, double Cost, long Millis){
		this.Path = Path;
		this.Num = Num;
		this.Cost = Cost;
		this.Millis = Millis;
	}
	@Override
	public String toString() {
		return 	Path + "\r\n" +
				"Num: " + this.Num + "\r\n" +
				"Cost: " + this.Cost + "\r\n" +
				(double)Millis/1000 + " seconds";
	}
	public String getPath() {
		return Path;
	}
	public long getNum() {
		return Num;
	}
	public double getCost() {
		return Cost;
	}
	public long getMillis() {
		return Millis;
	}
	
}