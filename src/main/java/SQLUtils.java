import entity.Law;
import mapper.LawMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class SQLUtils {

    public static void main(String[] args) {


        try{
            findUserByID();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void findUserByID() throws Exception{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        //---------------
        LawMapper lawMapper = session.getMapper(LawMapper.class);
        Law law = lawMapper.findLawById(2);
        updateTitle(law,lawMapper);
        int count = lawMapper.getCount();
        for(int i = 0;i < count;i++){

        }
        //--------------
        session.close();
    }

    public static void updateTitle(Law law,LawMapper lawMapper){
        String title = law.getTitle();
        int index = title.indexOf(';');
        title = title.substring(0,index);
        law.setTitle(title);
        lawMapper.updateTitleById(law);
    }

}
