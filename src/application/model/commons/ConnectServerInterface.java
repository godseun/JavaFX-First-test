package application.model.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConnectServerInterface {

	private static CloseableHttpClient client = HttpClients.createDefault();
	private static String DB_ADR = "121.181.156.200:8080";
	
	private static String sendPost(String url, List<NameValuePair> postParams) throws Exception {

		HttpPost post = new HttpPost(url);
		UrlEncodedFormEntity ur = new UrlEncodedFormEntity(postParams, "UTF-8");
		post.setEntity(ur);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.setHeader("user-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
		CloseableHttpResponse response = client.execute(post);
		int responseCode = response.getStatusLine().getStatusCode();
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
//			System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
			String name = header.getName().trim();
			if (name.equals("Set-Cookie")) {

				String[] s = header.getValue().split(";");
//				System.out.println("Set-Cookie:" + s[0]);

			}
		}

//		System.out.println("\nSending2 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + postParams);
//		System.out.println("Response Code : " + responseCode);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);

		}
		return result.toString();
	}
	
    static public boolean creJ(String itemno) {
        List<NameValuePair> nameValuePair=new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("itemcd",itemno));
        nameValuePair.add(new BasicNameValuePair("calqty","1"));
        try {
            String s=sendPost("http://"+DB_ADR+"/Product/JaegoInfo.do",nameValuePair);
//            System.out.println(s);
            JSONParser jsonParse = new JSONParser();
            JSONArray jsonObj = (JSONArray)jsonParse.parse(s);
            if (jsonObj.size()>0) {
                String  JAEGO1= (String) ((JSONObject)(jsonObj.get(0))).get("JAEGO1");
                String  JAEGO2= (String) ((JSONObject)(jsonObj.get(0))).get("JAEGO2");
//                System.out.println("itemno "+itemno+" . "+JAEGO1+" : "+JAEGO2);
                if(JAEGO1.trim().equals("유") || JAEGO2.trim().equals("유")) {
                	return true;
                }
                //판매중지
                //현재 상태 비교
                //판매재개 걸어준다
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }
        
	static public String ExecuteSql(String sql) {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

		String json = "";
		String reSt = "";
		
		nameValuePair.add(new BasicNameValuePair("params", "sp_SqlDynamic▤SEARCH▤SqlDynamic▤S▤1000▤600▥SQL▤" + sql + "▥TRANYN▤Y"));

		try {

			String s = sendPost("http://"+DB_ADR+"/Controller/SqlProcedure.do", nameValuePair);
			String[] s1 = s.split("▥");
			
			reSt = s1[0];
			
//			System.out.println(s);

		} catch (Exception e) {
			e.printStackTrace();
			json = "Error: 파라메타가 정의되지 않았습니다";
		} finally {

		}
		
		return reSt;
	}

	static public String serachDB() {
		String json = "";
		String DB = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			/* IP 주소 가져오기 */
			String ipAddr = addr.getHostAddress();

			/* 호스트명 가져오기 */
			String hostname = addr.getHostName();

			/* NetworkInterface를 이용하여 현재 로컬 서버에 대한 하드웨어 어드레스를 가져오기 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			String macAddr = "";
			for (int i = 0; i < mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "");
			}

			String sql = "SELECT DB" + "  FROM USEAUT.MACADDR" + "  where mac like '%" + macAddr + "%' ;";
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("params", "sp_SqlDynamic▤SEARCH▤SqlDynamic▤S▤1000▤600▥SQL▤" + sql + "▥TRANYN▤N"));
			String s = sendPost("http://"+DB_ADR+"/Controller/SqlProcedure.do", nameValuePair);
			String[] s1 = s.split("▥");
			JSONParser jsonParse = new JSONParser();
			JSONArray jsonObj = (JSONArray) jsonParse.parse(s1[0]);
			if (jsonObj.size() > 0) {
				DB = (String) ((JSONObject) (jsonObj.get(0))).get("DB");
			}
			
			System.out.println("ipAddr : "+ipAddr);
			System.out.println("hostname : "+hostname);
			System.out.println("macAddr : "+macAddr);
			
		} catch (Exception e) {
			e.printStackTrace();
			json = "Error: 파라메타가 정의되지 않았습니다";
		} finally {
		}
		System.out.println("DB search : "+DB);
		
		return DB;
	}
}
