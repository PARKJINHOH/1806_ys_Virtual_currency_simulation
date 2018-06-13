package  com.example.jinhoh.coinsimulation;

import java.util.HashMap;

public class Main {
    public static void main(String args[]) {
		Api_Client api = new Api_Client("28b31952f3b54f2e12f79f6708409eea",
			"b842f1aa4e5618fc8c861936ab4e5b2b");
	
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("order_currency", "BTC");
		rgParams.put("payment_currency", "KRW");
	
	
		try {
		    String result = api.callApi("/info/balance", rgParams);
		    System.out.println(result);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
    }
}

