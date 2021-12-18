import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;
import java.io.File;
import java.io.FileWriter;



public class kaisernjRegEx
{
  public static void main(String[] args) throws FileNotFoundException  {
    
    try {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String everything = sb.toString();
        br.close();      
        String regex = "((\"[a-z0-9!#$%&*+-\\/\\=?^_‘{|}~.]\")|(?<!\\.)(?!.*\\.\\.)[a-z0-9!#$%&*+-\\/\\=?^_‘{|}~.]+(?<!\\.))@(((?<!\\-)[a-z0-9]+.[a-z0-9]+(?<!\\-))|((?<!\\-)^[//[//]^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}[//]//](?<!\\-)))";
        Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(everything);
        int totalEmails = 0;
        HashMap <String, Integer> emailList = new HashMap<String, Integer>();
        while(m.find()) {
          String address = m.group(1);
          String domain = m.group(3);
          String email = m.group(0);
          if (address.length() <= 64 &&
              domain.length() <= 255 && 
              email.length() <= 254) {
            Integer numEmails = emailList.containsKey(m.group(0)) ? emailList.get(m.group(0)) : 0;
            emailList.put(m.group(0), numEmails + 1);
            totalEmails++;
          }
          
        }
        
        String escapedRegex = "((\\\"[a-z0-9!#$%&*+-\\\\/\\\\=?^_‘{|}~.]\")|(?<!\\\\.)(?!.*\\\\.\\\\.)[a-z0-9!#$%&*+-\\\\/\\\\=?^_‘{|}~.]+(?<!\\\\.))@(((?<!\\-)[a-z0-9]+.[a-z0-9]+(?<!\\\\-))|((?<!\\\\-)^[//[//]^(?:[0-9]{1,3}\\\\.){3}[0-9]{1,3}[//]//](?<!\\\\-)))";
        System.out.println("Regex Expression: " + escapedRegex);
        System.out.println("Explanation: ((a|b)@(c|b))");
        System.out.println("Group a handles quoted local adresses, b handles unquoted local adresess. C handles domain adresses and d handles ip adresses.");
        System.out.println("(?<!\\\\\\\\.) checks for trailing periods before and after the local address if the local address is not quoted");
        System.out.println("(?!.*\\\\\\\\.\\\\\\\\.) checks for consecutive periods in an unquoted address");
        System.out.println("(?<!\\-) checks for hyphens at the start and end of the adresses");
        System.out.println("^[//[//]^(?:[0-9]{1,3}\\\\\\\\.){3}[0-9]{1,3}[//]//] checks for a valid ipv4 address with enclosed escaped brackets.");
        System.out.println("Number of emails: " + totalEmails);
        System.out.println("Number of unique emails: " + emailList.size());
        
        LinkedHashMap<String, Integer> sortedEmailList = new LinkedHashMap<String, Integer>();
        emailList.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
        .forEachOrdered(x -> sortedEmailList.put(x.getKey(), x.getValue()));
        
        
        File file = new File("C:\\Users\\Noah\\Desktop\\kaisernj_all_emailaddresses.txt");
        FileWriter fw = new FileWriter(file, false);
       
        for (Map.Entry<String, Integer> entry : sortedEmailList.entrySet()) {
          String email = entry.getKey();
          fw.write(email + "\r\n");
        }
        
        fw.close();
        
        file = new File("C:\\Users\\Noah\\Desktop\\kaisernj_sorted_emailaddresses.txt");
        fw = new FileWriter(file, false);
        
        for (Map.Entry<String, Integer> entry : sortedEmailList.entrySet()) {
          String email = entry.getKey();
          Integer value = entry.getValue();
          fw.write(email + " " + value + "\r\n");
        }
        
        fw.close();
    
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
