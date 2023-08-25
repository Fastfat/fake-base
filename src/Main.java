import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        QuestMaker qm = new QuestMaker();
        List<Map<String,Object>> result1 = qm.getList("INSERT VALUES 'lastNaMe' = 'null' , 'id'=3, 'age' = 40 , 'active'=true");
        List<Map<String,Object>> result4 = qm.getList("INSERT VALUES 'lastName' = 'Феdoor' ,  'cost'=10.22, 'id'=32, 'age'=45, 'active'=true");
        System.out.println(result1);
        System.out.println(result4);
        List<Map<String,Object>> result2 = qm.getList("UPDATE VALUES 'active'=null, 'cost'=10.1 WhErE 'id'=3");
        System.out.println(result2);
        //List<Map<String,Object>> result3 = qm.getList("SELECT");
        List<Map<String,Object>> result3 = qm.getList("SELECT WHERE 'age'>=30 and 'lastName' ilike '%d%'");
        //List<Map<String,Object>> result3 = qm.getList("DELETE WHERE 'id'=3 OR 'id' > 10");
        //List<Map<String,Object>> result3 = qm.getList("DELETE");
        System.out.println(result3);
        System.out.println(qm.getList("SELECT"));
    }
}