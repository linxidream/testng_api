package test.com.wmk.api.login;

import com.mysql.jdbc.StringUtils;
import com.wmk.api.apiBeans.ApiDataBean;
import com.wmk.api.configs.ApiConfig;
import com.wmk.api.listeners.AutoTestListener;
import com.wmk.api.listeners.RetryListener;
import com.wmk.api.pojo.User;
import com.wmk.api.utils.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import test.com.wmk.api.TestBase;

import java.nio.file.Paths;
import java.util.*;

@Listeners({ AutoTestListener.class, RetryListener.class })
public class AddUserTest extends TestBase {
    private static String rootUrl;
    private static boolean rooUrlEndWithSlash = false;
    private static Header[] publicHeaders;
    private static boolean requestByFormData = false;
    private static ApiConfig apiConfig;
    protected List<ApiDataBean> dataList = new ArrayList<ApiDataBean>();
    private static HttpClient client;
    /**
     * 初始化测试数据
     *
     * @throws Exception
     */
    @Parameters("envName")
    @BeforeSuite
    public void init(@Optional("api-config-add.xml") String envName) throws Exception {
        String configFilePath = Paths.get(System.getProperty("user.dir")+ "/src/main/resources", envName).toString();
        ReportUtil.log("api config path:" + configFilePath);
        apiConfig = new ApiConfig(configFilePath);
        // 获取基础数据
        rootUrl = apiConfig.getRootUrl();
        rooUrlEndWithSlash = rootUrl.endsWith("/");
        // 读取 param，并将值保存到公共数据map
        Map<String, String> params = apiConfig.getParams();
        setSaveDates(params);
        List<Header> headers = new ArrayList<Header>();
        apiConfig.getHeaders().forEach((key, value) -> {
            Header header = new BasicHeader(key, value);
            if(!requestByFormData && key.equalsIgnoreCase("content-type") && value.toLowerCase().contains("form-data")){
                requestByFormData=true;
            }
            headers.add(header);
        });
        publicHeaders = headers.toArray(new Header[headers.size()]);
        client = new SSLClient();
        client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000); // 读取超时
    }

    @Parameters({ "excelPath", "sheetName" })
    @BeforeTest
    public void readData(@Optional("case/api-data.xls") String excelPath, @Optional("insert_order") String sheetName) throws DocumentException {
        dataList = readExcelData(ApiDataBean.class, excelPath.split(";"),
                sheetName.split(";"));
    }

    /**
     * 过滤数据，run标记为Y的执行。
     *
     * @return
     * @throws DocumentException
     */
    @DataProvider(name = "runDatas")
    public Iterator<Object[]> getApiData(ITestContext context)
            throws DocumentException {
        List<Object[]> dataProvider = new ArrayList<Object[]>();
        for (ApiDataBean data : dataList) {
            if (data.isRun()) {
                dataProvider.add(new Object[] { data });
            }
        }
        return dataProvider.iterator();
    }

    @Test(dataProvider = "runDatas")
    public void apiTest(ApiDataBean apiDataBean) throws Exception {
        ReportUtil.log("--- test start ---");
        if (apiDataBean.getSleep() > 0) {
            // sleep休眠时间大于0的情况下进行暂停休眠
            ReportUtil.log(String.format("sleep %s seconds",
                    apiDataBean.getSleep()));
            Thread.sleep(apiDataBean.getSleep() * 1000);
        }
        String apiParam = (String) buildRequestParam(apiDataBean);
        // 封装请求方法
        HttpUriRequest method = parseHttpRequest(apiDataBean.getUrl(),
                apiDataBean.getMethod(), apiParam, publicHeaders,rooUrlEndWithSlash,rootUrl);
        String responseData;
        try {
            // 执行
            HttpResponse response = client.execute(method);
            int responseStatus = response.getStatusLine().getStatusCode();
            ReportUtil.log("返回状态码："+responseStatus);
            if (apiDataBean.getStatus()!= 0) {
                Assert.assertEquals(responseStatus, apiDataBean.getStatus(),
                        "返回状态码与预期不符合!");
            }
            HttpEntity respEntity = response.getEntity();
            Header respContentType = response.getFirstHeader("Content-Type");
            responseData=EntityUtils.toString(respEntity, "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            method.abort();
        }
        // 输出返回数据log
        ReportUtil.log("resp:" + responseData);
        // 验证预期信息
        verifyResult(responseData, apiDataBean.getVerify(),
                apiDataBean.isContains());
        // 对返回结果进行提取保存。
        saveResult(responseData, apiDataBean.getSave());
        //比对数据库信息
        User user = new DBTools().getSqlSession().selectOne("selectUserById",saveDatas.get("user_id"));
        Assert.assertTrue(StringUtil.isNotEmpty(user.getUser_name()));
        ReportUtil.log("新增用户 ： "+ user.getUser_name());
    }

}
