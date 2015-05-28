package callInterface;

public class MyMathCaller {
	
	public void calculate(int n1,int n2,MyMath myMath){
		
		myMath.sum(n1+n2);
	}
	
	public static void main(String [] args){
		
		MyMathCaller caller = new MyMathCaller();
		MyMath callback = new MyMathImp();
		
		caller.calculate(5, 6, new MyMath() {
			
			@Override
			public void sum(int total) {
				System.out.println("Total: "+total);
				
			}
		});
	}

}
