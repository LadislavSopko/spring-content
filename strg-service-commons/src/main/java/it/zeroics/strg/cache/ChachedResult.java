package it.zeroics.strg.cache;

public class ChachedResult {
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public ChachedResult(int a, String c) {
		super();
		this.a = a;
		this.c = c;
	}
	int a;
	String c;
}
