package com.ws.kafka.common;

/**
 * @ProjectName: BigdataProject
 * @Package: com.yssh.bigdata.common.kafka.IProcessorHandler
 * @ClassName: IProcessorHandler
 * @Description:
 * @Author: Jackie Yang
 * @Date: 2017/8/7
 * @ModifyBy: jk
 * @UpdateUser: jk
 * @UpdateDate: 2017/8/7 11:17
 * @UpdateRemark: 说明本次修改内容
 * @Version: [v1.0]
 * @Copyright: 北京中燕信息技术有限公司
 */
public interface IProcessorHandler {

    void process(Object record);
}

