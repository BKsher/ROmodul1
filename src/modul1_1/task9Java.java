package modul1_1;

import java.util.Scanner;

class Visitor extends Thread {
	private volatile int number = 0;
	private volatile int currentStake = 0;
	private volatile boolean isSuspended = false;
	
	Visitor(int number) {
		this.number = number;
	}
	
	public int getStake() {
		return currentStake;
	}
	
	public boolean isSuspended() {
		return isSuspended;
	}

	public void setSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public void newStake(int stake) {
		if(isSuspended) System.out.println("Visitor " + number + " is suspended");
		else if(stake > currentStake) {
			System.out.println("Stake of visitor number " + number + " is " + stake);
			currentStake = stake;
		}
	}
	
	public void setStake(int stake) {
		currentStake = stake;
	}
	
	@Override
	public void run() {
		while(true) {
			if(isSuspended) {
				try {
					sleep(50000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isSuspended = false;
			}
		}
	}
}

public class task9Java {
	
	static Visitor visitor1, visitor2, visitor3;
	static boolean hasPayed = false;
	
	static void pay() {
		hasPayed = true;
	}
	
	public static void main(String[] args) {
		visitor1 = new Visitor(1);
		visitor2 = new Visitor(2);
		visitor3 = new Visitor(3);
		
		Visitor[] visitors = new Visitor[] {visitor1, visitor2, visitor3};
		
		Thread bidHandler = new Thread(
				() -> {
					Scanner sc = new Scanner(System.in);
					while (true) {
						int visitor = sc.nextInt();
						
						if(visitor == 0) pay();
						else {
							int stake = sc.nextInt();
							if(visitor > 3 || visitor < 1) 
								System.out.println("Visitor " + visitor + " doesn't exist");
							else {
								visitors[visitor-1].newStake(stake);
							}
						}
					}
		});
		
		System.out.println("Starting auction");
		
		bidHandler.setDaemon(true);
		bidHandler.start();
		for(Visitor visitor : visitors)
			visitor.start();
		
		for(int i = 1; i <= 5; i++) {
			System.out.println("Starting lot number " + i);
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Auction for lot number " + i + " has ended\n");
			System.out.println("Stake of visitor number 1 is " + visitor1.getStake());
			System.out.println("Stake of visitor number 2 is " + visitor2.getStake());
			System.out.println("Stake of visitor number 3 is " + visitor3.getStake());
			System.out.println();
			int winner = 0;
			
			if(visitor1.getStake() > visitor2.getStake())
				winner = visitor1.getStake() > visitor3.getStake() ? 1 : 3;
			else
				winner = visitor2.getStake() > visitor3.getStake() ? 2 : 3;
				
			System.out.println("The winner is visitor number " + winner);
			
			System.out.println("You have 10 seconds to make payment");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(hasPayed) {
				System.out.println("Payment was successful");
			} else {
				System.out.println("Payment not recieved. Visitor " + winner + " will be suspended for 2 lots");
				visitors[winner-1].setSuspended(true);
			}
			
			hasPayed = false;
			
			for(Visitor visitor : visitors)
				visitor.setStake(0);
		}
		
		System.out.println("Auction has ended");
	}
	
}