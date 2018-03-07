package nunet.rbsk.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class JSON_Helper {
	static String TAG = "data:";

	public static String putMethodUrl(final Context context,
			List<NameValuePair> params, final String URL) {

		// public static String putMethodUrl(final Context context, byte[] data,
		// final String URL) {
		String res = "";
		try {
			Log.e("url", URL);
			Log.e("params", params.toString());

			HttpPost httppost = new HttpPost(URL);
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = 180000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 300000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			httpclient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httppost.addHeader("Accept-Encoding", "gzip, deflate");

			/*
			 * httppost.setHeader("Content-type", "application/json");
			 * JSONObject jo = new JSONObject(); jo.put("DeviceID", "25");
			 * jo.put("TokenID", "50"); jo.put("DataFile", data); StringEntity
			 * se = new StringEntity(jo.toString()); httppost.setEntity(se);
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			// ResponseHandler<String> resHandler = new BasicResponseHandler();
			HttpResponse responce = httpclient.execute(httppost);
			res = "" + getIfCompressed(responce);
			Log.e("res from post method", res);
			httpclient.getConnectionManager().shutdown();

		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			Log.e(TAG, "network connection Exception");
			res = "neterror";
		} catch (SocketTimeoutException socketExaception) {
			Log.e(TAG, "Socket Exception");
			socketExaception.printStackTrace();
			res = "serverDown";
		} catch (ConnectTimeoutException connectionTimeOut) {
			Log.e(TAG, "connectin timeout");
			connectionTimeOut.printStackTrace();
			res = "connectinTimeOut";
		} catch (UnknownHostException e) {
			e.printStackTrace();
			res = "neterror";
		} catch (ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException in valid  responce");
			e.printStackTrace();
			res = "neterror";
		} catch (IOException e) {
			Log.e(TAG, "IOException");
			e.printStackTrace();
			res = "neterror";
		} catch (Exception e) {
			e.printStackTrace();
			res = "serverIssue";
		}
		return res;
	}

	public static String getIfCompressed(HttpResponse response) {
		if (response == null)
			return null;
		try {
			InputStream is = AndroidHttpClient.getUngzippedContent(response
					.getEntity());
			return convertStreamToString(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
