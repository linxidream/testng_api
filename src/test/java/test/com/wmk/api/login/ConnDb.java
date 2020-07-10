package test.com.wmk.api.login;

import com.wmk.api.pojo.User;
import com.wmk.api.utils.DBTools;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Reader;

public class ConnDb {

    @Test
    public void test_conn_db(){
        Reader reader=null;
        try {
            reader= Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化mybatis,创建SqlSessionFactory类的实例
        SqlSessionFactory sqlMapper=new SqlSessionFactoryBuilder().build(reader);
         //创建session实例
        SqlSession session=sqlMapper.openSession();
        //传入参数查询，返回结果
        User user=session.selectOne("selectUserById",1);
        //输出结果
        System.out.println(user.getUser_name());
        //关闭session
        session.close();
    }

    @Test
    public void test_conndb(){
        User user = new DBTools().getSqlSession().selectOne("selectUserById",1);
        System.out.println(user.getUser_name());
    }

    @Test
    public void test_conn(){
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        User user = sessionFactory.openSession().selectOne("selectUserById",1);
        System.out.println(user.getUser_name());
    }
}
