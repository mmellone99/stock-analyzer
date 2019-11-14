import java.io.*;
import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import yahoofinance.*;

public class myStock {

    
	
	private static class stockInfo {
		private String name;
		private BigDecimal price;
		public stockInfo(String nameIn, BigDecimal priceIn) {
			name = nameIn;
			price = priceIn;
		}
		public String toString() {
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(name + " " + price.toString());
			return stockInfoString.toString();
		}
	}
	public class NewComparator implements Comparator<Map.Entry<String, stockInfo>>{

		@Override
		public int compare(Entry<String, stockInfo> s1, Entry<String, stockInfo> s2) {
			stockInfo stock1 = s1.getValue();
			stockInfo stock2 = s2.getValue();
			BigDecimal price1 = stock1.price;
			BigDecimal price2 = stock2.price;
			return price2.compareTo(price1);
		}
	}
	private HashMap <String, stockInfo> stocks;
	private TreeSet<Map.Entry<String, stockInfo>> stocktree;
	public myStock () {
		stocks = new HashMap<String,stockInfo>();
		stocktree = new TreeSet<Map.Entry<String, stockInfo>>(new NewComparator());
		
	}
    
	public void insertOrUpdate(String symbol, stockInfo stock) { 
		stocks.put(symbol, stock);
		AbstractMap.SimpleEntry<String, stockInfo> entry = new AbstractMap.SimpleEntry<String, stockInfo>(symbol, stock);
		stocktree.add(entry);
		
	}
	
	public stockInfo get(String symbol) {
		return stocks.get(symbol);
	}
	
	public List<Map.Entry<String, stockInfo>> top(int k) { 
		List<Map.Entry<String, stockInfo>> topList = new ArrayList<Map.Entry<String, stockInfo>>();
		Iterator<Map.Entry<String, stockInfo>> it = stocktree.iterator();
		for(int i=0; i<k; i++) {
			topList.add(it.next());
		}
		return topList;
	}
	

    public static void main(String[] args) throws IOException {   	
    	myStock techStock = new myStock();
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] var = line.split(":");
				
				// YahooFinance API is used and make sure the lib file is included in the project build path
				Stock stock = YahooFinance.get(var[0]);
				
				// test the insertOrUpdate operation
				techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice())); 
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 1;
		System.out.println("===========Top 10 stocks===========");
		
		// test the top operation
		for (Map.Entry<String, stockInfo> element : techStock.top(10)) {
		    System.out.println("[" + i + "]" +element.getKey() + " " + element.getValue());
		    i++;
		}
		
		// test the get operation
		System.out.println("===========Stock info retrieval===========");
    	System.out.println("VMW" + " " + techStock.get("VMW"));
    	System.out.println("CHL" + " " + techStock.get("CHL"));
    }
}
