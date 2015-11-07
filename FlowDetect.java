package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class TimerTT extends TimerTask
{
	DecimalFormat df = new DecimalFormat("#0.00");
	public void run() {
		try {
			URL url = new URL("http://192.168.31.4:8080/quota?url=http%3A%2F%2Fso.csdn.net%2Fso%2Fsearch%2Fs.do%3Fref%3Dtoolbar%26q%3Dphp%25E4%25B8%258Ehtml%26ref%3Dtoolbar%26q%3Dphp%25E4%25B8%258Ehtml");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line=br.readLine())!=null)
			{
				sb.append(line);
			}
			String temp= sb.substring(550,570);
			int num = temp.indexOf("<");
			String temp1 = temp.substring(0,num);
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(temp1);   
			String mrp=m.replaceAll("").trim();
			
			double d = Double.parseDouble(mrp)/(1024*1024);
			String temp2 = df.format(d);
			System.out.println(temp2);
		} catch (SocketException se){System.out.println("Empty");}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
public class FlowDetect
{
	ScheduledExecutorService ser1= Executors.newScheduledThreadPool(1);//自动切换账号
	
	public FlowDetect()
	{
		ser1.scheduleAtFixedRate(new TimerTT(), 0, 1000, TimeUnit.MILLISECONDS);
	}
	public static void main(String[] args) {
		new FlowDetect();
	}
}
