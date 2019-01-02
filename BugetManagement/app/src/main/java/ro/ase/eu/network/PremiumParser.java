package ro.ase.eu.network;

import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PremiumParser {

    public static Premium fromJson(String json) throws JSONException {
        if (json == null) {
            return null;
        }

        JSONObject object = new JSONObject(json);
        Item bronze = getItemFromJsonObject(
                object.getJSONObject("bronze"));
        Item silver = getItemFromJsonObject(
                object.getJSONObject("silver"));
        List<Item> gold = getItemListFromJsonArray(
                object.getJSONArray("gold"));

        return new Premium(bronze, silver, gold);
    }

    public static Premium fromXml(String xml) {

        if (xml == null) {
            return null;
        }

        Premium result = new Premium();

        int event;
        String text = null;

        try {
            Item item = new Item();
            ArrayList<Product> products = new ArrayList<>();
            Product product = null;
            String type = null;
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(new StringReader(xml));
            event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("item")) {
                            type = xmlParser.getAttributeValue(null, "type");
                            products = new ArrayList<>();
                            item = new Item();

                        } else if (name.equals("product")) {
                            product = new Product();
                        }

                        break;
                    case XmlPullParser.TEXT:
                        text = xmlParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("category")) {
                            item.setCategory(text);
                        } else if (name.equals("name") && product!=null) {
                            product.setName(text);
                        } else if (name.equals("price") && text != null && !text.isEmpty() && product!=null) {
                            product.setPrice(Double.parseDouble(text));
                        } else if (name.equals("product")) {
                            products.add(product);
                        } else if (name.equals("item")) {
                            item.setProducts(products);
                            if (type!=null &&
                                    type.equalsIgnoreCase("bronze")) {
                                result.setBronze(item);
                            } else if (type!=null &&
                                    type.equalsIgnoreCase("silver")) {
                                result.setSilver(item);
                            } else if(type!=null &&
                                    type.equalsIgnoreCase("gold")){
                                result.setGold(item);
                            }
                        }
                }
                event = xmlParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Item getItemFromJsonObject(JSONObject object) throws JSONException {

        if (object == null) {
            return null;
        }

        String category = object.getString("category");
        List<Product> products = new ArrayList<>();
        JSONArray array = object.getJSONArray("products");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject productJson = array.getJSONObject(i);
                if (productJson != null) {
                    String name = productJson.getString("name");
                    Double price = productJson.getDouble("price");
                    products.add(new Product(name, price));
                }
            }
        }


        return new Item(category, products);
    }

    private static List<Item> getItemListFromJsonArray(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }

        List<Item> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            Item item = getItemFromJsonObject(array.getJSONObject(i));
            if (item != null) {
                results.add(item);
            }
        }

        return results;
    }
}
