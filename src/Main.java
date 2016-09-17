import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        //planetaObolon("http://planetaobolon.com.ua");
        avisoKiev("http://www.aviso.ua/kiev/list.php?r=101");
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
        properties.forEach(System.out::println);
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
