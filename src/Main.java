import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main {
    public static void main(String[] args) throws IOException {
//        List<InfoProperty> properties=kvartiriKiev("http://kvartiri.kiev.ua/index.php?id_tran=3");
//        properties.forEach(System.out::println);
//        System.out.println("------------------------------------------------------------------------------------------");
//        properties=kvartiriKiev("http://kvartiri.kiev.ua/index.php?id_tran=2");
//        properties.forEach(System.out::println);
//        System.out.println("------------------------------------------------------------------------------------------");
//        properties=planetaObolon("http://planetaobolon.com.ua");
//        properties.forEach(System.out::println);
//        System.out.println("------------------------------------------------------------------------------------------");
//        properties=avisoKiev("http://www.aviso.ua/kiev/list.php?r=101");
//        properties.forEach(System.out::println);
//            realtUa("http://realt.ua/Db2/0Pr_Kv.php?cnt_all=5829&Obl=11&valt=2&sAkc=1&srtb0=19&srtby=19&rsrt=1");
            //ciUa("http://100realty.ua/realty_search/apartment/sale/cur_3?extended=0");
        //rieltorUa("http://rieltor.ua/flats-sale/?f-owners=1");
        OLX("http://www.olx.ua/nedvizhimost/prodazha-kvartir/");
    }
    public static List<newProperty> OLX(String siteUrl) throws IOException{
        String num="0987654321";
        List<String> urls=new ArrayList<>();
        List<String> id=new ArrayList<>();
        List<newProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Elements ad=doc.select("td.offer");
        String ids,u;
        for (Element el:ad){
            u=el.select("h3.x-large").first().select("a").first().attr("href");
            urls.add(u);
        }
        Document prop,ajax;
        String tel,address,description,json,au;
        String [] tels;
        int last;
        for (String url:urls){
            ids="";
            prop= Jsoup.connect(url).get();
            address=prop.select("strong.c2b").first().text();
            description=prop.select("h1.lheight28").first().text();
            for (int i=url.indexOf("ID")+2;i<url.indexOf(".html");i++){
                ids+=url.charAt(i);
            }
            au="http://www.olx.ua/ajax/misc/contact/phone/"+ids+"/white/";
            try {
                ajax = Jsoup.connect(au).ignoreContentType(true).get();
                au = ajax.toString();
                json = "";
                for (int i = au.indexOf("<body>") + 6; i < au.indexOf("</body>"); i++) {
                    json += au.charAt(i);
                }
                if (!json.contains("span>")) tel = checkNumber(json).trim();
                else {
                    last = 0;
                    tel = "";
                    tels = json.split("/span");
                    for (int i = 0; i < tels.length; i++) {
                        if (checkNumber(tels[i]) != "")
                            tel += checkNumber(tels[i]).trim() + ", ";
                    }
                    au = "";
                    for (int i = 0; i < tel.length(); i++) {
                        if (num.contains(tel.charAt(i) + "")) {
                            last = i;
                        }
                    }
                    for (int i = 0; i <= last; i++) {
                        au += tel.charAt(i);
                    }
                    tel = au;
                }
                properties.add(new newProperty(tel, address, description));
            } catch (HttpStatusException e){
                System.out.println("Нет доступа к базе OLX");
            }
        }
        properties.forEach(System.out::println);
        return properties;
    }
    public static List<newProperty> rieltorUa(String siteUrl) throws IOException {
        List<String> urls=new ArrayList<>();
        List<newProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Elements ad=doc.select("h2.catalog-item__title");
        Element div;
        for (Element el:ad){
            urls.add("http://rieltor.ua"+el.select("a").first().attr("href"));
        }
        Document prop;
        String tel,address,description;
        Element el;
        Elements a;
        for (String url:urls){
            prop=Jsoup.connect(url).get();
            tel=prop.select("div.ov-author__phone").first().text();
            address=prop.select("dd.description-text").last().text();
            description=prop.select("h1.catalog-view-header__title").first().text();
            properties.add(new newProperty(tel,address,description));
        }
        properties.forEach(System.out::println);
        return properties;
    }
    public static List<newProperty> ciUa(String siteUrl) throws IOException {
        List<String> urls=new ArrayList<>();
        List<newProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Elements ad=doc.select("div.realty-object-card");
        Element div;
        for (Element el:ad){
            div=el.select("div.object-address").first().select("a").first();
            urls.add("http://100realty.ua"+div.attr("href"));
        }
        Document prop;
        String tel,address,description;
        for (String url:urls){
            prop=Jsoup.connect(url).get();
            address=prop.getElementById("object-address").text();
            description=prop.getElementById("squeeze").select("h1").first().text();
            tel=prop.select("span.object-contacts-one-phone").last().text();
            properties.add(new newProperty(tel,address,description));
        }
        properties.forEach(System.out::println);
        return properties;
    }
    public static List<InfoProperty> realtUa(String siteUrl) throws IOException{
        List<String> urls=new ArrayList<>();
        List<InfoProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Elements table=doc.select("table.Forma");
        int k=1;
        for (Element el:table){
            for (Element e:el.select("a")){
                if (k%2!=0) urls.add(e.attr("href"));
                k++;
            }
        }
        urls.forEach(System.out::println);
        Document prop;
        for (String url:urls){
            //prop=Jsoup.connect(url).get();

        }
        return properties;
    }
    public static List<InfoProperty> avisoKiev(String siteUrl) throws IOException{
        List<String> urls=new ArrayList<>();
        List<InfoProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Elements div=doc.select("div.creame");
        Element a;
        for (Element el:div){
            a=el.select("a").first();
            urls.add(a.attr("href"));
        }
        Document prop;
        Element info;
        String inf;
        String address,tel;
        for (String u:urls){
            prop=Jsoup.connect(u).get();
            info=prop.select(".phone").first();
            inf=info.text();
            address="";
            for (int i=inf.indexOf("Район:")+6;i<inf.indexOf("Комнат:");i++){
                address+=inf.charAt(i);
            }
            address=address.trim();
            tel="";
            for (int i = inf.indexOf("Тел:") + 4;i<inf.indexOf("Email:") || i<inf.length();i++) {
                tel+=inf.charAt(i);
            }
            tel=tel.trim();
            properties.add(new InfoProperty(tel,address));
        }
        return properties;
    }
    public static List<InfoProperty> planetaObolon(String siteUrl) throws IOException{
        List<String> urls=new ArrayList<>();
        List<InfoProperty> properties=new ArrayList<>();
        Document doc=Jsoup.connect(siteUrl).get();
        Element ul=doc.select("ul.thumbnails").first();
        Elements li=ul.select("li.span3");
        String url;
        for (Element el:li){
            url=el.select("a").first().attr("href");
            urls.add(siteUrl+url);
        }
        int n=urls.size();
        String [] s=new String[n];
        int j=0;
        for (String el:urls){
            s[j]=el;
            j++;
        }
        Document prop;
        String tel;
        String address;
        String [] tels;
        String [] mail;
        String email;
        int check;
        for (int i=0;i<n;i++){
            prop=Jsoup.connect(s[i]).get();
            Element info=prop.select("div.user-info").first();
            Element p=info.select("p").last();
            tel=p.text().trim();
            tels=tel.split("\\+");
            tel="";
            email="";
            Elements amail=p.select("a");
            for (Element el:amail){
                email+=el.text()+"abruityuiopwefwe";
            }
            for (int k=0;k<tels.length;k++){
                tel+=" "+tels[k].trim();
            }
            mail=email.split("abruityuiopwefwe");
            for (int k=0;k<mail.length;k++){
                tel=tel.replaceAll(mail[k],"");
            }
            tel=tel.trim();
            check=0;
            email="";
            for (int k=0;k<tel.length();k++){
                if (tel.charAt(k)==' '){
                    check++;
                }
                if (tel.charAt(k)==' ' && check%2!=0){

                } else
                if (tel.charAt(k)!='-') email+=tel.charAt(k);
            }
            email=email.trim();
            tels=email.split(" ");
            tel="";
            for (int k=0;k<tels.length-1;k++){
                tel+=tels[k]+", ";
            }
            tel+=tels[tels.length-1];
            address=prop.select("address").first().text();
            properties.add(new InfoProperty(tel,address));
        }
        return properties;
    }
    public static List<InfoProperty> kvartiriKiev(String siteUrl) throws IOException {
        List<Property> property=new ArrayList<>();
        List<InfoProperty> properties=new ArrayList<>();
        Document doc = Jsoup.connect(siteUrl).get();
        Elements table=doc.getElementsByAttributeValue("class", "city");
        for (Element tableTr:table){
            Elements a = tableTr.getElementsByTag("a");
            for (Element href:a){
                String url=href.attr("href");
                String text=href.text();
                property.add(new Property(url,text));
            }
        }
        int n=property.size();
        String [] urls=new String[n];
        int j=0;
        for (Property p:property){
            urls[j]=p.url;
            j++;
        }
        Document prop;
        Elements contact;
        String adress="";
        String num="";
        int k=0;
        for (int i=0;i<n;i++){
            prop=Jsoup.connect(urls[i]).get();
            contact=prop.getElementsByAttributeValue("class", "contact");
            for (Element el:contact){
                num=el.text();
                num=checkNumber(num);
                Elements tab=prop.select("td");
                for (Element element:tab){
                    if ("city".equals(element.attr("class"))) adress=element.text();
                }
                adress+=", ";
                for (Element element:tab){
                    if ("district".equals(element.attr("class"))) adress+=element.text();
                }
                adress+=", ";
                for (Element element:tab){
                    if ("street".equals(element.attr("class"))) adress+=element.text();
                }
                adress+=", ";
                for (Element element:tab){
                    if ("number".equals(element.attr("class"))) adress+=element.text();
                }
            }
            properties.add(new InfoProperty(num,adress));
        }
        return properties;
    }
    public static String checkNumber(String num){
        String number="";
        String validation="()+0987654321 -";
        for (int i=0;i<num.length();i++){
            if (validation.indexOf(num.charAt(i))!=-1) number+=num.charAt(i);
        }
        return number.trim();
    }
}
class Property{
     String url;
     String text;

    public Property(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
class InfoProperty{
    String num;
    String adress;

    public InfoProperty(String num, String adress) {
        this.num = num;
        this.adress = adress;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Override
    public String toString() {
        return "InfoProperty{" +
                "num='" + num + '\'' +
                ", adress='" + adress + '\'' +
                '}';
    }
}
class newProperty{
    String tel;
    String address;
    String description;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public newProperty(String tel, String address, String description) {
        this.tel = tel;
        this.address = address;
        this.description = description;
    }

    @Override
    public String toString() {
        return "newProperty{" +
                "tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
