package com.shellming.zhihu.utils;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ruluo1992 on 11/2/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnsiTest {

//    @Test
//    public void testFenci() throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader("F:\\Code\\intellij\\zhihu\\38522649.txt"));
//        StringBuilder sb = new StringBuilder();
//        while (true) {
//            String line = reader.readLine();
//            if (line == null) {
//                break;
//            } else {
//                sb.append(line).append("\r\n");
//            }
//        }
//        reader.close();
//        Result result = IndexAnalysis.parse(sb.toString());
//        Map<String, Integer> tf = new HashMap<>(result.size());
//        for (Term term : result) {
//            int freq = 0;
//            String name = term.getName();
//            name = name.replace("\r", "");
//            name = name.replace("\n", "");
//            if (tf.containsKey(name)) {
//                freq = tf.get(name);
//            }
//            freq++;
//            tf.put(name, freq);
//        }
//        List<Map.Entry<String, Integer>> list =
//                new ArrayList<>(tf.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
//        for (Map.Entry<String, Integer> entry : list) {
//            if (entry.getKey().length() > 1) {
//                System.out.println(entry.getKey() + " " + entry.getValue());
//            }
//        }
//    }
}
