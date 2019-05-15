
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**  
 *  
 * @author Burak Bayram - burakbyrm99@gmail.com, Alperen Örsdemir - alpne@hotmail.com
 * @since 01.03.2019 
 * <p>  
 *  Programlama Dillerinin Prensipleri dersi 1.Java ödevi 
 * </p>  
 */ 

public class Odev1 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Dosya okuma için gerekli değişkenler
        File file = new File("Program.c");
        FileReader fileReader = new FileReader(file);
        String line;

        BufferedReader br = new BufferedReader(fileReader);
        
        //Regex ile string kontrolleri
        Pattern operatorler = Pattern.compile("[+]|[-]|[/]|[*]|[&]|[=]|[<]|[>]|[!]");
        Pattern operatorler2 = Pattern.compile("[+]{2,2}|[-]{2,2}|[/]{2,2}|[*]{2,2}|[&]{2,2}|[=]{2,2}|[<]{2,2}|[>]{2,2}|[!]{2,2}");
        Pattern fonksiyonlar = Pattern.compile("\\s{0,}[a-z]\\s([a-zA-Z]{0,})[(]([[a-z]{0,}\\s[[*][&]a-zA-Z[,]]{0,}]{0,}[)])\\s{0,}[{]{0,}");
        Pattern parametreler = Pattern.compile("[,]");
        
        //Arama yapacak değişkenler
        Matcher matcher;
        Matcher matcher1;
        Matcher matcher2;
        Matcher matcher3;
        
        int operatorSayac = 0;
        int fonksiyonSayac = 0;
        int parametreSayac = 0;                      
        int sayac=0,satir=0;
        
        //Dosyayı satır satır okuma döngüsü.
        while ((line = br.readLine()) != null) {
            for (int i = 0; i < line.length()-1; i++) {
                //YORUM SATIRI KONTROLLERİ
                if (line.charAt(i)=='/'&&line.charAt(i+1)=='*') {
                    sayac++;
                }
                if (line.charAt(i)=='/'&&line.charAt(i+1)=='/') {
                    break;
                }
                //BURAYA GİRERSE YORUM SATIRI DEĞİLDİR
                if (sayac==0 && (i==line.length()-2)) {  
                    //Matcherin arayacağı string burada verilir
                    matcher = operatorler.matcher(line);
                    
                    //Kurala göre operator sayısını arttıran döngü, burada iki aynı operator olsa bile 2 kere arttırır.
                    while(matcher.find()){                
                        operatorSayac++;
                    }
                    
                    matcher = operatorler.matcher(line);
                    matcher3 = operatorler2.matcher(line);
                    
                    //2 kere arka arkaya gelen operatorleri tespit eder ve azaltır.
                    while(matcher.find() && matcher3.find()){  
                        if (satir!=0) {
                            operatorSayac=operatorSayac-2;
                        }
                        operatorSayac--;
                    }

                    matcher1 = fonksiyonlar.matcher(line);
                    String a;
                    
                    //Fonksiyon ve parametre sayımını kurallarına göre gerçekleştiren döngü
                    while(matcher1.find()){
                        a = line;
                        matcher2 = parametreler.matcher(a);
                        while(matcher2.find()){
                            parametreSayac++;
                        }
                        parametreSayac++;
                        if ((a.charAt(a.indexOf("("))== '(') == (a.charAt(a.indexOf("(")+1)==')')) {
                            parametreSayac--;
                        }
                        fonksiyonSayac++;
                    }                    
                    break;
                }
                if (line.charAt(i)=='*'&&line.charAt(i+1)=='/') {
                    sayac=0;
                    satir++;
                }
            }
            satir=0;
        }                       
        br.close();
        
        System.out.println("Toplam Operatör Sayısı:"+operatorSayac);
        System.out.println("Toplam Fonksiyon Sayısı:"+fonksiyonSayac);
        System.out.println("Toplam Parametre Sayısı:"+parametreSayac);
        
        //Fonksiyon isimleri kısmı yazılması için bir kere daha dosya okunur.
        File file1 = new File("Program.c");
        FileReader fileReader1 = new FileReader(file1);
        String line1;

        BufferedReader br1 = new BufferedReader(fileReader1);
        
        System.out.println("Fonksiyon İsimleri:");
        sayac=0;
        
        //Dosya okuma döngüsü
        while ((line1 = br1.readLine()) != null) {            
            matcher1 = fonksiyonlar.matcher(line1);
            int space=0;
            
            //Yorum satırı kontrolü yapar
            for (int j = 0; j < line1.length()-1; j++) {
                if (line1.charAt(j)=='/'&&line1.charAt(j+1)=='*') {
                    sayac++;
                }
                if (line1.charAt(j)=='/'&&line1.charAt(j+1)=='/') {
                    break;
                }
                //Yorum satırı olmayınca buraya girer
                if (sayac==0&&(j==line1.length()-2)) {
                    while(matcher1.find()){
                        //Regexte gruplanan fonksiyon isimleri yazdırılır
                        System.out.print(matcher1.group(1) + " - Parametreler:");        
                        
                        //Regexte gruplanan parametre isimlerini değişken tipi olmadan yazar.
                        for (int i = 0; i < matcher1.group(2).length(); i++) {
                            if (matcher1.group(2).charAt(i)==' ') {
                                space=i;
                            }
                            if (matcher1.group(2).charAt(i)==','||matcher1.group(2).charAt(i)==')') {
                                String parametreAd = matcher1.group(2).substring(space,i);
                                System.out.print(parametreAd);
                                if (matcher1.group(2).charAt(i)!=')') {
                                    System.out.print(",");
                                }
                            }
                            if (matcher1.group(2).charAt(i)==')') {
                                System.out.println("");
                            }
                        }              
                    }
                    break;
                }
                if (line1.charAt(j)=='*'&&line1.charAt(j+1)=='/') {
                    sayac=0;
                }
            }            
        }
        br1.close();        
    }    
}