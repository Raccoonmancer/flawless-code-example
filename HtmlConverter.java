import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HtmlConverter {
	public static void main(String[] args) {
		System.out.println(getHtmlString(retrieveJsonFromFile()));
	}

	public static String getHtmlString(String stringifiedJsonData) {
		return convertJsonToHtml(new JSONObject(stringifiedJsonData + "</html>"));
	}

	private static String convertJsonToHtml(Object object) {
		StringBuilder html = new StringBuilder();

		try {
			if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) object;
				String[] keys = JSONObject.getNames(jsonObject);

				if (keys.length > 0) {
					for (String key : keys) {
						switch (key) {
						case "doctype":
							appendHtmlDoctypeStrategy(key, jsonObject, html);
							break;
						case "meta":
							appendHtmlMetaStrategy(key, new JSONObject(jsonObject.get(key).toString()), html);
							break;
						default:
							appendHtmlDefaultStrategy(key, jsonObject, html);
						}
					}
				}
			} else if (object instanceof JSONArray) {
				JSONArray array = (JSONArray) object;
				for (int i = 0; i < array.length(); i++) {
					html.append(convertJsonToHtml(array.get(i)));
				}
			} else {
				html.append(object);
			}
		} catch (JSONException e) {
			return e.getLocalizedMessage();
		}
		return html.toString();
	}

	private static String retrieveJsonFromFile() {
		StringBuilder jsonString = new StringBuilder();
		try {
			File jsonFile = new File("mockedData.json");

			Scanner myReader = new Scanner(jsonFile);
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				jsonString.append(line);

			}
			myReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return jsonString.toString();
	}

	private static StringBuilder appendHtmlDefaultStrategy(String key, JSONObject jsonObject, StringBuilder html) {
		html.append("<" + key + ">");

		Object val = jsonObject.get(key);

		html.append(convertJsonToHtml(val));

		html.append("</" + key + ">");
		return html;
	}

	private static StringBuilder appendHtmlMetaStrategy(String key, JSONObject jsonObject, StringBuilder html) {
		String[] keys = JSONObject.getNames(jsonObject);

		if (keys.length > 0) {
			for (String metaKey : keys) {
				switch (metaKey) {
				case "charset":
					html.append("<" + key + " " + metaKey + "=" + "\"" + jsonObject.get(metaKey) + "\"" + ">");
					break;
				case "viewport":
					html.append("<" + key + " " + "name" + "=" + "\"" + metaKey + "\"" + " ");

					JSONObject viewportObject = new JSONObject(jsonObject.get(metaKey).toString());
					String[] viewportKeys = JSONObject.getNames(viewportObject);

					if (viewportKeys.length > 0) {
						int i = 0;
						for (String viewportKey : viewportKeys) {

							html.append(viewportKey + "=" + "\"" + viewportObject.get(viewportKey) + "\"");

							if (i++ != viewportKeys.length - 1) {
								html.append(", ");
							} else {
								html.append(">");
							}
						}
					}
					break;
				default:
					html.append("<" + key + ">" + metaKey + "=" + jsonObject.get(metaKey));
				}
			}
		}
		return html;
	}

	private static StringBuilder appendHtmlDoctypeStrategy(String key, JSONObject jsonObject, StringBuilder html) {
		html.append("<!" + key.toUpperCase() + " " + jsonObject.get(key) + ">" + "<html" + " lang=" + "\""
				+ jsonObject.get("language") + "\"" + ">");
		return html;
	}
}
